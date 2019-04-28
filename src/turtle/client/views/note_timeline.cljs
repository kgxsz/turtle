(ns client.views.note-timeline
  (:require [re-frame.core :as re-frame]
            [styles.constants :as c]
            [client.utils :as u]))


(defn view [{:keys [markers]}]
  [:div
   {:class (u/bem [:note-timeline])}
   [:div
    {:class (u/bem [:note-timeline__markers])}
    (doall
     (for [{:keys [id left]} markers]
       [:div
        {:key id
         :class (u/bem [:note-timeline__marker])
         :style {:left left}}]))]])


(defn note-timeline []
  (let [!notes (re-frame/subscribe [:notes])
        !ticks (re-frame/subscribe [:ticks])]
    (fn []
      (let [notes @!notes
            ticks @!ticks
            markers (let [instants (map :instant ticks)
                          minimum-instant (apply min instants)
                          maximum-instant (apply max instants)
                          circle-radius (:circle-radius c/plot)
                          marker-radius (/ (:x-small c/filling) 2)]
                      (for [{:keys [id instant]} notes]
                        {:id id
                         :left (- (+ circle-radius
                                     (* (- (:width c/plot)
                                           (* 2 circle-radius))
                                        (/ (- instant minimum-instant)
                                           (- maximum-instant minimum-instant))))
                                  marker-radius)}))]
        [view
         {:markers markers}]))))

