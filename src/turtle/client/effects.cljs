(ns client.effects
  (:require [client.routing :as routing]
            [ajax.core :as ajax]
            [re-frame.core :as re-frame]
            [domkm.silk :as silk]
            [pushy.core :as pushy]
            [medley.core :as medley]
            [clojure.string :as string]))


(re-frame/reg-fx
 :queries
 (fn [queries]
   (doseq [query queries]
     (ajax/POST "https://api.tickerize.keigo.io/query"
                {:params {:query query}
                 :handler (fn [response] (re-frame/dispatch [:query-success query response]))
                 :error-handler (fn [response] (re-frame/dispatch [:query-failure query response]))
                 :response-format :json
                 :format :json
                 :keywords? true}))))


(re-frame/reg-fx
 :commands
 (fn [commands]
   (doseq [command commands]
     (ajax/POST "https://api.tickerize.keigo.io/command"
                {:params {:command command}
                 :handler (fn [response] (re-frame/dispatch [:command-success command response]))
                 :error-handler (fn [response] (re-frame/dispatch [:command-failure command response]))
                 :response-format :json
                 :format :json
                 :keywords? true}))))


(re-frame/reg-fx
 :initialise-routing
 (fn []
   (reset! routing/!history
           (pushy/pushy
            #(re-frame/dispatch
              [:route-success {:route (::silk/name %)
                               :route-params {:symbol (some-> % :symbol string/lower-case keyword)}
                               :query-params (->> % ::silk/url :query (medley/map-keys keyword))}])
            (partial silk/arrive routing/routes)))
   (pushy/start! @routing/!history)))


(re-frame/reg-fx
 :route
 (fn [[route route-params]]
   (pushy/set-token! @routing/!history (silk/depart routing/routes route route-params))))
