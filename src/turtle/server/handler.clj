(ns server.handler
  (:require [cheshire.core :as cheshire]
            [taoensso.faraday :as faraday]
            [clojure.java.io :as io]
            [medley.core :as medley]
            [clj-http.client :as client]
            [clj-time.core :as time]
            [clj-time.coerce :as time.coerce]
            [clj-uuid :as uuid]
            [camel-snake-kebab.core :as camel-snake-kebab])
  (:import [com.amazonaws.services.lambda.runtime.RequestStreamHandler])
  (:gen-class
   :name server.Handler
   :implements [com.amazonaws.services.lambda.runtime.RequestStreamHandler]))


(def ^:const +namespace+ #uuid "3aa21200-27b3-11e9-ab15-726ecdf58913")


(def config {:access-key (System/getenv "ACCESS_KEY")
             :secret-key (System/getenv "SECRET_KEY")
             :endpoint "http://dynamodb.eu-west-1.amazonaws.com"})


(def table-name "turtle-notes")


(defmulti handle-query (comp keyword first))

(defmethod handle-query :notes [query]
  {:notes (->> (faraday/scan config table-name)
               (sort-by :added-at))})

(defmethod handle-query :ticks [[_ symbol]]
  (let [request-options {:query-params {:function "TIME_SERIES_DAILY_ADJUSTED"
                                        :symbol symbol
                                        :apikey (System/getenv "ALPHA_VANTAGE_API_KEY")}}
        {:keys [body]} (client/get "https://www.alphavantage.co/query" request-options)
        format-tick (fn [[k v]]
                      (let [instant (time.coerce/to-long k)]
                        {:id (uuid/v5 +namespace+ instant)
                         :instant instant
                         :symbol symbol
                         :open (-> v (get "1. open") (Double.))
                         :close (-> v (get "5. adjusted close") (Double.))}))]
    ;; TODO - error handling
    {:ticks (->> (get (cheshire/parse-string body) "Time Series (Daily)")
                 (map format-tick)
                 (sort-by :instant))}))

(defmethod handle-query :default [query]
  (throw (Exception.)))


(defmulti handle-command (comp keyword first))

(defmethod handle-command :add-note [[_ note]]
  (faraday/put-item config table-name note)
  {})

(defmethod handle-command :default [command]
  (throw (Exception.)))



(defn read-input [reader]
  (let [{:keys [body]} (cheshire/parse-stream reader true)
        {:keys [query command]} (cheshire/parse-string body true)]
    {:query query :command command}))


(defn write-output [writer status-code body]
  (let [response {:statusCode status-code
                  :headers {"Access-Control-Allow-Origin" "*"}
                  :body (cheshire/generate-string body)}]
    (cheshire/generate-stream response writer)))


(defn -handleRequest
  [_ input-stream output-stream context]
  (with-open [reader (io/reader input-stream)
              writer (io/writer output-stream)]
    (try
      (let [{:keys [query command]} (read-input reader)]
        (write-output writer 200 (or (some-> query handle-query)
                                     (some-> command handle-command)
                                     (throw (Exception.)))))
      (catch Exception e
        (write-output writer 500 {})))))
