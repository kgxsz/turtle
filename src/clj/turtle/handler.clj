(ns turtle.handler
  (:require [cheshire.core :as cheshire]
            [taoensso.faraday :as faraday]
            [clojure.java.io :as io])
  (:import [com.amazonaws.services.lambda.runtime.RequestStreamHandler])
  (:gen-class
   :name turtle.Handler
   :implements [com.amazonaws.services.lambda.runtime.RequestStreamHandler]))


(def ddb-config {:access-key (System/getenv "ACCESS_KEY")
                 :secret-key (System/getenv "SECRET_KEY")
                 :endpoint "http://dynamodb.eu-west-1.amazonaws.com"})

(def table-name "turtle-notes")


(defmulti handle-query (comp keyword first))

(defmethod handle-query :notes [query]
  {:notes (faraday/scan ddb-config table-name)})

(defmethod handle-query :default [query]
  (throw (Exception.)))


(defmulti handle-command (comp keyword first))

#_(defmethod handle-command :add-checked-date [[_ id date]]
  (let [item (update (faraday/get-item ddb-config table-name {:id id}) :checked-dates #(-> % set (conj date) vec))]
    (faraday/update-item ddb-config table-name {:id id} {:update-map {:checked-dates [:put (:checked-dates item)]}})
    {}))

#_(defmethod handle-command :remove-checked-date [[_ id date]]
  (let [item (update (faraday/get-item ddb-config table-name {:id id}) :checked-dates #(-> % set (disj date) vec))]
    (faraday/update-item ddb-config table-name {:id id} {:update-map {:checked-dates [:put (:checked-dates item)]}})
    {}))

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
