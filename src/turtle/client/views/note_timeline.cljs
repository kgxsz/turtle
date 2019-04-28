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
            instants (map :instant ticks)
            maximum-instant (apply max instants)
            minimum-instant (apply min instants)
            normalise-instant (fn [instant]
                                (+ (:circle-radius c/plot)
                                   (* (- (:width c/plot)
                                         (u/double (:circle-radius c/plot)))
                                      (/ (- instant minimum-instant)
                                         (- maximum-instant minimum-instant)))))]
        [view
         {:markers (for [{:keys [id instant]} notes]
                     {:id id
                      :left (- (normalise-instant instant)
                               (u/halve (:x-small c/filling)))})}]))))

