(ns client.views.note-timeline
  (:require [re-frame.core :as re-frame]
            [styles.constants :as c]
            [client.utils :as u]))


(defn view [markers]
  [:div
   {:class (u/bem [:note-timeline])}
   [:div
    {:class (u/bem [:note-timeline__markers])}
    (doall
     (for [marker markers]
       [:div
        {:key (:id marker)
         :class (u/bem [:note-timeline__marker])
         :style {:left (:left marker)}}]))]])


(defn left [instants instant]
  (let [minimum-instant (apply min instants)
        maximum-instant (apply max instants)
        circle-radius (:circle-radius c/plot)
        marker-radius (/ (:x-small c/filling) 2)]
    (- (+ circle-radius
          (* (- (:width c/plot)
                (* 2 circle-radius))
             (/ (- instant minimum-instant)
                (- maximum-instant minimum-instant))))
       marker-radius)))


(defn markers [ticks notes]
  (let [instants (map :instant ticks)]
    (map
     (fn [{:keys [id instant]}]
       {:id id
        :left (left instants instant)})
     notes)))


(defn note-timeline []
  (let [!notes (re-frame/subscribe [:notes])
        !ticks (re-frame/subscribe [:ticks])]
    (fn []
      (let [notes @!notes
            ticks @!ticks
            markers (markers ticks notes)]
        [view markers]))))

