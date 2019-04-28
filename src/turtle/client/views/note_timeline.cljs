(ns client.views.note-timeline
  (:require [re-frame.core :as re-frame]
            [styles.constants :as c]
            [client.utils :as u]))


(defn view [{:keys [marker-positions]}]
  [:div
   {:class (u/bem [:note-timeline])}
   [:div
    {:class (u/bem [:note-timeline__markers])}
    (doall
     (for [{:keys [id left]} marker-positions]
       [:div
        {:key id
         :class (u/bem [:note-timeline__marker])
         :style {:left left}}]))]])


(defn note-timeline []
  (let [!ticks (re-frame/subscribe [:ticks])
        !notes (re-frame/subscribe [:notes])]
    (fn []
      (let [notes @!notes
            ticks @!ticks
            tick-positions (u/tick-positions ticks)]
        [view
         {:marker-positions (for [{:keys [id tick-id]} notes]
                              (assoc (u/get-by-id tick-id tick-positions)
                                     :id id))}]))))

