(ns client.views.button
  (:require [re-frame.core :as re-frame]
            [client.utils :as u]))


(defn view [{:keys [type disabled? label]}
            {:keys [on-click]}]
  [:div
   {:class (u/bem [:button type (when disabled? :disabled)])
    :on-click (when-not disabled? on-click)}
   [:div
    {:class (u/bem [:button__label type])}
    [:div
     {:class (u/bem [:text])}
     label]]])


(defn button [properties behaviours]
  [view
   properties
   behaviours])


(defn primary-button [properties behaviours]
  [button
   (assoc properties
          :type :primary)
   behaviours])


(defn secondary-button [properties behaviours]
  [button
   (assoc properties
          :type :secondary)
   behaviours])
