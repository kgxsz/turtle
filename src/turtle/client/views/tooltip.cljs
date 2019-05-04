(ns client.views.tooltip
  (:require [re-frame.core :as re-frame]
            [client.utils :as u]))


(defn view [{:keys [close-label instant-label]}]
  [:div
   {:class (u/bem [:tooltip])}
   [:div
    {:class (u/bem [:tooltip__locus])}]
   [:div
    {:class (u/bem [:tooltip__pointer])}]
   [:div
    {:class (u/bem [:tooltip__backing])}]
   [:div
    {:class (u/bem [:tooltip__instant-label])}
    [:div
     {:class (u/bem [:text :font-size-xx-small :font-weight-bold :colour-white-one :align-center])}
     instant-label]]
   [:div
    {:class (u/bem [:tooltip__close-label])}
    [:div
     {:class (u/bem [:text :font-size-xx-tiny :font-weight-bold :colour-white-one])}
     "USD"]
    [:div
     {:class (u/bem [:text :font-size-medium :font-weight-bold :colour-white-one :padding-left-xx-tiny])}
     close-label]]])


(defn tooltip [tick-id]
  (let [!tick (re-frame/subscribe [:tick tick-id])]
    (fn []
      (let [{:keys [close instant]} @!tick]
        [view
         {:close-label (u/format-price close)
          :instant-label (u/format-compact-time instant)}]))))
