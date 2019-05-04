(ns client.views.note-timeline
  (:require [re-frame.core :as re-frame]
            [styles.constants :as c]
            [client.utils :as u]))


(defn view [{:keys [notes tick-positions]}]
  [:div
   {:class (u/bem [:note-timeline])}
   [:div
    {:class (u/bem [:note-timeline__notes])}
    (doall
     (for [{:keys [note-id tick]} notes]
       [:div
        {:key note-id
         :class (u/bem [:note-timeline__note])
         :style {:left (:x (u/find-tick-position tick tick-positions))}}]))]])


(defn note-timeline []
  (let [!ticks (re-frame/subscribe [:ticks])
        !notes (re-frame/subscribe [:notes])]
    (fn []
      [view
       {:notes @!notes
        :tick-positions (u/tick-positions @!ticks)}])))
