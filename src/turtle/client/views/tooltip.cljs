(ns client.views.tooltip
  (:require [re-frame.core :as re-frame]
            [client.utils :as u]))


(defn view [{:keys [close instant]}]
  [:div
   {:class (u/bem [:tooltip]
                  [:cell :absolute])}
   [:div
    {:class (u/bem [:tooltip__locus]
                   [:cell :absolute :width-x-small :height-x-small :colour-black-two :opacity-20])}]
   [:div
    {:class (u/bem [:tooltip__pointer]
                   [:cell :absolute :opacity-40])}]
   [:div
    {:class (u/bem [:cell :absolute :width-huge :height-xx-large :colour-black-two :opacity-40])}]
   [:div
    {:class (u/bem [:cell :relative :margin-top-xx-small])}
    [:div
     {:class (u/bem [:text :font-size-xx-small :font-weight-bold :colour-white-one :align-center])}
     (u/format-compact-time instant)]]
   [:div
    {:class (u/bem [:cell :relative :row :align-baseline])}
    [:div
     {:class (u/bem [:text :font-size-xx-tiny :font-weight-bold :colour-white-one])}
     "USD"]
    [:div
     {:class (u/bem [:text :font-size-medium :font-weight-bold :colour-white-one :padding-left-xx-tiny])}
     (u/format-price close)]]])


(defn tooltip [tick-id]
  (let [!tick (re-frame/subscribe [:tick tick-id])]
    (fn []
      [view
       (select-keys @!tick [:close :instant])])))
