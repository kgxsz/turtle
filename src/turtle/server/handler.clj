(ns server.handler
  (:require [cheshire.core :as cheshire]
            [taoensso.faraday :as faraday]
            [clojure.java.io :as io]
            [medley.core :as medley]
            [clj-http.client :as client]
            [clj-time.core :as time]
            [clj-time.coerce :as time.coerce]
            [clj-uuid :as uuid]
            [clojure.string :as string]
            [camel-snake-kebab.core :as camel-snake-kebab]
            [muuntaja.core :as muuntaja]
            [cognitect.transit :as transit])
  (:import [com.amazonaws.services.lambda.runtime.RequestStreamHandler]
           [java.io ByteArrayInputStream ByteArrayOutputStream])
  (:gen-class
   :name server.Handler
   :implements [com.amazonaws.services.lambda.runtime.RequestStreamHandler]))


(def ^:const +namespace+ #uuid "3aa21200-27b3-11e9-ab15-726ecdf58913")


(def config {:dynamodb {:access-key (System/getenv "ACCESS_KEY")
                        :secret-key (System/getenv "SECRET_KEY")
                        :endpoint "http://dynamodb.eu-west-1.amazonaws.com"
                        :batch-limit 25}
             ;; TODO - replace key
             :alpha-vantage {:api-key "KDZ8GOJFU5D9D2FL" #_(System/getenv "ALPHA_VANTAGE_API_KEY")
                             :endpoint "https://www.alphavantage.co/query"}})


(defn join [notes ticks]
  ;; TODO - do this in a more general manner eventually
  (let [tick-by-id (medley/index-by :tick-id ticks)]
    (map
     (fn [{:keys [tick-id] :as note}]
       (assoc note :tick (get tick-by-id tick-id)))
     notes)))


(defn cleanse-note [note]
  ;; TODO generalise the cleansing, inbound and outbound
  ;; java.math.BigDecimal (instance? clojure.lang.BigInt 1M)
  ;; clojure.lang.BigInt (instance? clojure.lang.BigInt 1N)
  (-> note
      (update :note-id uuid/as-uuid)
      (update :tick-id uuid/as-uuid)
      (update :added-at long)))


(defn cleanse-tick [tick]
  (-> tick
      (update :tick-id uuid/as-uuid)
      (update :symbol (comp keyword string/lower-case))
      (update :added-at long)
      (update :instant long)
      (update :volume long)
      (update :open double)
      (update :close double)
      (update :low double)
      (update :high double)
      (update :adjusted-close double)))


(defn get-notes []
  (map cleanse-note (faraday/scan (:dynamodb config) :turtle-notes)))

(defn get-ticks [{:keys [tick-ids symbol]}]
  (let [attr-conds (cond-> {}
                     ;; TODO - simpler cleansing and anti corruption
                     (seq tick-ids) (assoc :tick-id [:in (map str tick-ids)])
                     (some? symbol) (assoc :symbol [:in [symbol]]))]
    (map cleanse-tick (faraday/scan (:dynamodb config) :turtle-ticks {:attr-conds attr-conds}))))


(defmulti handle-query first)

(defmethod handle-query :notes [[_ symbol]]
  (let [notes (get-notes)
        ticks (get-ticks {:tick-ids (map :tick-id notes)
                          :symbol symbol})
        note-ids (->> (join notes ticks)
                      (filter :tick)
                      (sort-by #(get-in % [:tick :instant]) >)
                      (map :note-id))
        note-by-id (->> notes
                        (filter #(contains? (set note-ids) (:note-id %)))
                        (medley/index-by :note-id))
        tick-by-id (medley/index-by :tick-id ticks)]
    {:note-ids note-ids
     :note-by-id note-by-id
     :tick-by-id tick-by-id}))

(defmethod handle-query :ticks [[_ symbol]]
  (let [ticks (->> (get-ticks {:symbol symbol})
                   (sort-by :instant)
                   (take-last 100))
        tick-ids (map :tick-id ticks)]
    {:tick-by-id (zipmap tick-ids ticks)
     :tick-ids tick-ids}))

(defmethod handle-query :default [query]
  (throw (Exception.)))


(defmulti handle-command first)

(defmethod handle-command :cache-ticks [[_ symbol]]
  ;; AV.LON, GS, AAPL, GOOG, AMZN, LLOY.LON
  (let [request-options {:query-params {:function "TIME_SERIES_DAILY_ADJUSTED"
                                        :symbol symbol
                                        :apikey (get-in config [:alpha-vantage :api-key])}}
        {:keys [body]} (client/get (get-in config [:alpha-vantage :endpoint]) request-options)
        ;; TODO - move formatting into a two stage process
        format-tick (fn [[k v]]
                      (let [instant (time.coerce/to-long k)]
                        {:tick-id (str (uuid/v5 +namespace+ (str (name symbol) instant)))
                         :added-at (time.coerce/to-long (time/now))
                         :instant instant
                         :symbol symbol
                         :open (-> v (get "1. open") (Double.))
                         :high (-> v (get "2. high") (Double.))
                         :low (-> v (get "3. low") (Double.))
                         :close (-> v (get "4. close") (Double.))
                         :adjusted-close (-> v (get "5. adjusted close") (Double.))
                         :volume (-> v (get "6. volume") (Long.))}))
        ;; TODO - don't use cheshire
        ticks (->> (get (cheshire/parse-string body) "Time Series (Daily)")
                   (mapv format-tick))]
    ;; TODO - error handling
    (doseq [partitioned-ticks (partition-all (get-in config [:dynamodb :batch-limit]) ticks)]
      (faraday/batch-write-item (:dynamodb config) {:turtle-ticks {:put partitioned-ticks}}))
    {}))

(defmethod handle-command :add-note [[_ note]]
  (faraday/put-item
   (:dynamodb config)
   :turtle-notes
   ;; TODO - generalise this stuff into a translation layer
   (-> note
       (update :note-id str)
       (update :tick-id str)))
  {})

(defmethod handle-command :delete-note [[_ note-id]]
  (faraday/delete-item
   (:dynamodb config)
   :turtle-notes
   ;; TODO - generalise this stuff into a translation layer
   {:note-id (str note-id)})
  {})

(defmethod handle-command :default [command]
  (throw (Exception.)))


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
    (let [{:keys [body] :as request} (muuntaja/decode "application/json" input-stream)
          {:keys [query command]} (muuntaja/decode "application/transit+json" body)]
      (write-output output-stream 200 (or (some-> query handle-query)
                                          (some-> command handle-command)
                                          (throw (Exception.)))))
    (catch Exception e
      (.printStackTrace e)
      (write-output output-stream 500 {}))))
