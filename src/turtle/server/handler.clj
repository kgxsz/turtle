(ns server.handler
  (:require [cheshire.core :as cheshire]
            [taoensso.faraday :as faraday]
            [clojure.java.io :as io]
            [medley.core :as medley]
            [clj-http.client :as client]
            [clj-time.core :as time]
            [clj-time.coerce :as time.coerce]
            [camel-snake-kebab.core :as camel-snake-kebab])
  (:import [com.amazonaws.services.lambda.runtime.RequestStreamHandler])
  (:gen-class
   :name turtle.Handler
   :implements [com.amazonaws.services.lambda.runtime.RequestStreamHandler]))


(def config {:access-key (System/getenv "ACCESS_KEY")
             :secret-key (System/getenv "SECRET_KEY")
             :endpoint "http://dynamodb.eu-west-1.amazonaws.com"})

(def table-name "turtle-notes")


(defmulti handle-query (comp keyword first))

(defmethod handle-query :notes [query]
  {:notes (faraday/scan config table-name)})

(defmethod handle-query :ticker [query]
  (let [request-options {:query-params {:function "TIME_SERIES_DAILY_ADJUSTED"
                                        :symbol "AAPL"
                                        :apikey (System/getenv "ALPHA_VANTAGE_API_KEY")}}
        {:keys [body]} (client/get "https://www.alphavantage.co/query" request-options)]
    ;; TODO - error handling
    {:ticker (map
              (fn [[k v]]
                {:instant (time.coerce/to-long k)
                 :open (-> v (get "1. open") (Double.))
                 :close (-> v (get "5. adjusted close") (Double.))})
              (get (cheshire/parse-string body) "Time Series (Daily)"))}))

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
