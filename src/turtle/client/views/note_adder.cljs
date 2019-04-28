(ns client.views.note-adder
  (:require [re-frame.core :as re-frame]
            [styles.constants :as c]
            [client.utils :as u]))


(defn view [{:keys [overlays left]}]
  [:div
   {:class (u/bem [:note-adder])}
   [:div
    {:class (u/bem [:note-adder__body])}
    (doall
     (for [{:keys [id left width]} overlays]
       [:div
        {:key id
         :class (u/bem [:note-adder__overlay])
         :on-click (fn [e]
                     (re-frame/dispatch [:add-note])
                     (.preventDefault e))
         :on-mouse-enter (fn [e]
                           (re-frame/dispatch [:update-focused-tick-id id])
                           (.preventDefault e))
         :on-mouse-leave (fn [e]
                           (re-frame/dispatch [:update-focused-tick-id nil])
                           (.preventDefault e))
         :style {:left left
                 :width width}}]))
    [:div
     {:class (u/bem [:note-adder__plus-button])
      :style {:left left}}
     [:div
      {:class (u/bem [:note-adder__plus-button__cross :vertical])}]
     [:div
      {:class (u/bem [:note-adder__plus-button__cross :horizontal])}]]]])


(defn note-adder []
  (let [!ticks (re-frame/subscribe [:ticks])
        !focused-tick (re-frame/subscribe [:focused-tick])]
    (fn []
      (let [ticks @!ticks
            focused-tick @!focused-tick
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
         {:overlays (as-> ticks $
                      (partition 3 1 $)
                      (map (fn [[a b c]]
                             (let [left (u/halve
                                         (+ (normalise-instant (:instant a))
                                            (normalise-instant (:instant b))))
                                   right (u/halve
                                          (+ (normalise-instant (:instant b))
                                             (normalise-instant (:instant c))))]
                               {:id (:id b)
                                :left left
                                :right right
                                :width (- right left)}))
                           $)
                      (concat [{:id (-> ticks first :id)
                                :left (- (:circle-radius c/plot)
                                         (u/halve (:x-large c/filling)))
                                :right (+ (:circle-radius c/plot)
                                          (u/halve (- (:x-large c/filling)))
                                          (:left (first $)))
                                :width (+ (u/halve (:x-large c/filling))
                                          (- (:circle-radius c/plot))
                                          (:left (first $)))}]
                              $
                              [{:id (-> ticks last :id)
                                :left (:right (last $))
                                :right (+ (u/halve (:x-large c/filling))
                                          (- (:circle-radius c/plot))
                                          (:width c/plot))
                                :width (- (+ (u/halve (:x-large c/filling))
                                             (:width c/plot))
                                          (:circle-radius c/plot)
                                          (:right (last $)))}]))
          :left (if focused-tick
                  (- (normalise-instant (:instant focused-tick))
                     (u/halve (:x-large c/filling)))
                  (- (:width c/plot)
                     (u/halve (:x-large c/filling))
                     (:circle-radius c/plot)))}]))))
