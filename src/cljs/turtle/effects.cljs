(ns turtle.effects
  (:require [ajax.core :as ajax]
            [re-frame.core :as re-frame]))


(re-frame/reg-fx
 :query
 (fn [query]
   (ajax/POST "https://api.tickerize.keigo.io/query"
              {:params {:query query}
               :handler (fn [response] (re-frame/dispatch [:query-succeeded query response]))
               :error-handler (fn [response] (re-frame/dispatch [:query-failed query response]))
               :response-format :json
               :format :json
               :keywords? true})))


(re-frame/reg-fx
 :command
 (fn [command]
   (ajax/POST "https://api.tickerize.keigo.io/command"
              {:params {:command command}
               :handler (fn [response] (re-frame/dispatch [:command-succeeded command response]))
               :error-handler (fn [response] (re-frame/dispatch [:command-failed command response]))
               :response-format :json
               :format :json
               :keywords? true})))

