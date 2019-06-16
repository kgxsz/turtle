(ns client.effects
  (:require [client.routing :as routing]
            [ajax.core :as ajax]
            [re-frame.core :as re-frame]
            [domkm.silk :as silk]
            [pushy.core :as pushy]
            [medley.core :as medley]
            [reagent.cookies :as cookies]
            [clojure.string :as string]))


(re-frame/reg-fx
 :query
 (fn [query]
   (ajax/POST "https://api.tickerize.keigo.io/query"
              {:params query
               :handler (fn [response]
                          (re-frame/dispatch [:query-success query response]))
               :error-handler (fn [response] (re-frame/dispatch [:query-failure query response]))
               :response-format (ajax/transit-response-format {:handlers {"u" ->UUID}})})))


(re-frame/reg-fx
 :command
 (fn [command]
   (ajax/POST "https://api.tickerize.keigo.io/command"
              {:params command
               :handler (fn [response] (re-frame/dispatch [:command-success command response]))
               :error-handler (fn [response] (re-frame/dispatch [:command-failure command response]))
               :response-format (ajax/transit-response-format {:handlers {"u" ->UUID}})})))


(re-frame/reg-fx
 :listen
 (fn []
   (reset! routing/!history
           (pushy/pushy
            #(re-frame/dispatch
              [:route {:route (::silk/name %)
                       :route-params {:symbol (some-> % :symbol string/lower-case keyword)}
                       :query-params (->> % ::silk/url :query (medley/map-keys keyword))}])
            (partial silk/arrive routing/routes)))
   (pushy/start! @routing/!history)))


(re-frame/reg-fx
 :route
 (fn [[route route-params]]
   (pushy/set-token! @routing/!history (silk/depart routing/routes route (or route-params {})))))


(re-frame/reg-fx
 :cookie
 (fn [[k v options]]
   (cookies/set! :authorised? true {:max-age 2419200})))
