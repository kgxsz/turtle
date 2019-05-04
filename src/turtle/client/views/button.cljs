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


(defn button [options]
  [view
   options])


(defn primary-button [options]
  [button
   (assoc options
          :type :primary)])


(defn secondary-button [options]
  [button
   (assoc options
          :type :secondary)])
