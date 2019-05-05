(ns client.views.button
  (:require [re-frame.core :as re-frame]
            [client.utils :as u]))


(defn view [{:keys [type disabled? label on-click]}]
  [:div
   {:class (u/bem [:button type (when disabled? :disabled)])
    :on-click (when-not disabled? on-click)}
   [:div
    {:class (u/bem [:button__label type])}
    [:div
     {:class (u/bem [:text])}
     label]]])


(defn button [properties]
  [view
   properties])


(defn primary-button [properties]
  [button
   (assoc properties
          :type :primary)])


(defn secondary-button [properties]
  [button
   (assoc properties
          :type :secondary)])
