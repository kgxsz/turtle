(ns client.effects
  (:require [ajax.core :as ajax]
            [re-frame.core :as re-frame]))


(re-frame/reg-fx
 :query
 (fn [query]
   (ajax/POST "https://api.tickerize.keigo.io/query"
              {:params {:query query}
               :handler (fn [response] (re-frame/dispatch [:query-success query response]))
               :error-handler (fn [response] (re-frame/dispatch [:query-failure query response]))
               :response-format :json
               :format :json
               :keywords? true})))


(re-frame/reg-fx
 :command
 (fn [command]
   (ajax/POST "https://api.tickerize.keigo.io/command"
              {:params {:command command}
               :handler (fn [response] (re-frame/dispatch [:command-success command response]))
               :error-handler (fn [response] (re-frame/dispatch [:command-failure command response]))
               :response-format :json
               :format :json
               :keywords? true})))
