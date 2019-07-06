(ns server.handler
  (:require [taoensso.faraday :as faraday]
            [clj-http.client :as client]
            [clj-uuid :as uuid]
            [medley.core :as medley]
            [clj-time.core :as time]
            [clj-time.coerce :as time.coerce]
            [muuntaja.core :as muuntaja])
  (:import [com.amazonaws.services.lambda.runtime.RequestStreamHandler]
           [java.io ByteArrayInputStream ByteArrayOutputStream])
  (:gen-class
   :name server.Handler
   :implements [com.amazonaws.services.lambda.runtime.RequestStreamHandler]))


(def ^:const +namespace+ #uuid "3aa21200-27b3-11e9-ab15-726ecdf58913")


(def config {:dynamodb {:access-key (System/getenv "ACCESS_KEY")
                        :secret-key (System/getenv "SECRET_KEY")
                        :endpoint "http://dynamodb.eu-west-1.amazonaws.com"
                        :batch-write-limit 25}
             :alpha-vantage {:api-key (System/getenv "ALPHA_VANTAGE_API_KEY")
                             :endpoint "https://www.alphavantage.co/query"}})


(defn query [entity symbol]
  (let [result (faraday/query
                (:dynamodb config)
                :turtle
                {:partition [:eq [(-> symbol name (str entity))]]}
                {:order :desc
                 :span-reqs {:max 1}
                 :limit 100})]
    {:notes (keep :note result)
     :ticks (keep :tick result)}))


(defn request-ticks [symbol]
  (let [request-options {:query-params {:function "TIME_SERIES_DAILY_ADJUSTED"
                                        :symbol (name symbol)
                                        :apikey (get-in config [:alpha-vantage :api-key])}}
        {:keys [body]} (client/get (get-in config [:alpha-vantage :endpoint]) request-options)
        format-tick (fn [[k v]]
                      (let [instant (-> k name time.coerce/to-long)]
                        {:tick-id (uuid/v5 +namespace+ (str (name symbol) instant))
                         :added-at (time.coerce/to-long (time/now))
                         :instant instant
                         :symbol symbol
                         :open (-> v (get (keyword "1. open")) (Double.))
                         :high (-> v (get (keyword "2. high")) (Double.))
                         :low (-> v (get (keyword "3. low")) (Double.))
                         :close (-> v (get (keyword "4. close")) (Double.))
                         :adjusted-close (-> v (get (keyword "5. adjusted close")) (Double.))
                         :volume (-> v (get (keyword "6. volume")) (Long.))}))]
    (->> (get (muuntaja/decode "application/json" body) (keyword "Time Series (Daily)"))
         (map format-tick)
         (sort-by :instant >))))


(defn serialise-tick [tick]
  {:partition (-> tick :symbol name (str ":tick"))
   :sort (:instant tick)
   :id (-> tick :tick-id str)
   :tick (faraday/freeze tick)})


(defn write-ticks [ticks]
  (let [batch-write-limit (get-in config [:dynamodb :batch-write-limit])]
    (doseq [batched-ticks (partition-all batch-write-limit ticks)]
      (faraday/batch-write-item (:dynamodb config) {:turtle {:put batched-ticks}}))))


(defn serialise-note [note tick]
  {:partition (-> tick :symbol name (str ":note"))
   :sort (:added-at note)
   :id (-> note :note-id str)
   :note (faraday/freeze note)
   :tick (faraday/freeze tick)})


(defn write-note [note]
  (faraday/put-item (:dynamodb config) :turtle note))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;; Query ;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defmulti handle-query first)

(defmethod handle-query :notes [[_ {:keys [symbol]}]]
  (let [{:keys [notes ticks]} (query :note symbol)
        note-ids (map :note-id notes)]
    {:note-ids note-ids
     :note-by-id (medley/index-by :note-id notes)
     :tick-by-id (medley/index-by :tick-id ticks)}))

(defmethod handle-query :ticks [[_ {:keys [symbol]}]]
  (let [{:keys [ticks]} (query :tick symbol)
        last-updated (-> ticks first :added-at time.coerce/from-long)
        update? (time/after? (time/minus (time/now) (time/hours 24)) last-updated)
        updated-ticks (when update? (request-ticks symbol))]
    (when update? (write-ticks (map serialise-tick updated-ticks)))
    {:tick-ids (map :tick-id (or updated-ticks ticks))
     :tick-by-id (medley/index-by :tick-id (or updated-ticks ticks))}))

(defmethod handle-query :default [query]
  (throw (Exception.)))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;; Command ;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defmulti handle-command first)

(defmethod handle-command :add-note [[_ {:keys [note tick]}]]
  (->> (serialise-note note tick)
       (write-note))
  {})

(defmethod handle-command :delete-note [[_ {:keys [note tick]}]]
  (faraday/delete-item
   (:dynamodb config)
   :turtle
   {:partition (-> tick :symbol name (str ":note"))
    :sort (:added-at note)})
  {})

(defmethod handle-command :default [command]
  (throw (Exception.)))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;; handler ;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn write-output [output-stream status-code body]
  (let [encoder (muuntaja/create (assoc muuntaja/default-options :return :bytes))
        response {:statusCode status-code
                  :headers {"Access-Control-Allow-Origin" "*"
                            "Content-Type" "application/transit+json"}
                  :body (slurp (muuntaja/encode "application/transit+json" body))}]
    (.write output-stream (muuntaja/encode encoder "application/json" response))))


(defn -handleRequest
  [_ input-stream output-stream context]
  (try
    (let [{:keys [body path] :as request} (muuntaja/decode "application/json" input-stream)
          response (->> (muuntaja/decode "application/transit+json" body)
                        (map (case path "/query" handle-query "/command" handle-command))
                        (apply medley/deep-merge))]
      (write-output output-stream 200 response))
    (catch Exception e
      (.printStackTrace e)
      (write-output output-stream 500 {}))))
