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
     {:class (u/bem [:text :font-size-xx-small :font-weight-bold :colour-white-light :align-center])}
     instant-label]]
   [:div
    {:class (u/bem [:tooltip__close-label])}
    [:div
     {:class (u/bem [:text :font-size-xx-tiny :font-weight-bold :colour-white-light])}
     "USD"]
    [:div
     {:class (u/bem [:text :font-size-medium :font-weight-bold :colour-white-light :padding-left-xx-tiny])}
     close-label]]])


(defn tooltip []
  (let [!focused-tick (re-frame/subscribe [:focused-tick])]
    (fn []
      (let [focused-tick @!focused-tick]
        [view
         {:close-label (u/format-price (:close focused-tick))
          :instant-label (u/format-compact-time (:instant focused-tick))}]))))
