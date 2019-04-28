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
                                   (/ (* (- (:width c/plot)
                                            (* 2 (:circle-radius c/plot)))
                                         (- instant minimum-instant))
                                      (- maximum-instant minimum-instant))))]
        [view
         {:overlays (as-> ticks $
                      (partition 3 1 $)
                      (map (fn [[a b c]]
                             (let [left (/ (+ (normalise-instant (:instant a))
                                              (normalise-instant (:instant b)))
                                           2)
                                   right (/ (+ (normalise-instant (:instant b))
                                               (normalise-instant (:instant c)))
                                            2)]
                               {:id (:id b)
                                :left left
                                :right right
                                :width (- right left)}))
                           $)
                      (concat [{:id (-> ticks first :id)
                                :left (- (-> c/plot :circle-radius)
                                         (-> c/filling :x-large (/ 2)))
                                :right (+ (-> c/plot :circle-radius)
                                          (-> c/filling :x-large (/ 2) -)
                                          (:left (first $)))
                                :width (+ (-> c/filling :x-large (/ 2))
                                          (-> c/plot :circle-radius -)
                                          (:left (first $)))}]
                              $
                              [{:id (-> ticks last :id)
                                :left (:right (last $))
                                :right (+ (-> c/filling :x-large (/ 2))
                                          (-> c/plot :circle-radius -)
                                          (:width c/plot))
                                :width (- (+ (-> c/filling :x-large (/ 2))
                                             (:width c/plot))
                                          (-> c/plot :circle-radius)
                                          (:right (last $)))}]))
          :left (if focused-tick
                  (- (normalise-instant (:instant focused-tick))
                     (-> c/filling :x-large (/ 2)))
                  (- (:width c/plot)
                     (-> c/filling :x-large (/ 2))
                     (:circle-radius c/plot)))}]))))
