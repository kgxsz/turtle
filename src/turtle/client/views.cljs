(ns client.views
  (:require [re-frame.core :as re-frame]
            [client.utils :as u]
            [client.views.tooltip :refer [tooltip]]
            [client.views.note-adder :refer [note-adder]]
            [client.views.note-timeline :refer [note-timeline]]
            [client.views.notes :refer [notes]]
            [client.views.notification :refer [notification]]
            [styles.constants :as c]
            [cljs-time.core :as t]
            [cljs-time.format :as t.format]
            [cljs-time.coerce :as t.coerce]
            [client.schema :as schema]
            [reagent.format :as format]
            [cljs.spec.alpha :as spec]))


;; TODO - get this to utils
(def label-formatter (t.format/formatter "MMM do"))


(defn ticker []
  (let [!ticks (re-frame/subscribe [:ticks])
        !notes (re-frame/subscribe [:notes])
        !focused-tick (re-frame/subscribe [:focused-tick])]
    (fn []
      (let [ticks @!ticks
            notes @!notes
            focused-tick @!focused-tick
            closes (->> ticks (map :close) sort)
            instants (map :instant ticks)
            maximum-close (apply max closes)
            minimum-close (apply min closes)
            close-spread (- maximum-close minimum-close)
            maximum-instant (apply max instants)
            minimum-instant (apply min instants)
            instant-spread (- maximum-instant minimum-instant)
            normalise-instant (fn [instant]
                                (+ (:circle-radius c/plot)
                                   (/ (* (- (:width c/plot)
                                            (* 2 (:circle-radius c/plot)))
                                         (- instant minimum-instant))
                                      instant-spread)))
            normalise-close (fn [close]
                              (- (:height c/plot)
                                 (:circle-radius c/plot)
                                 (/ (* (- (:height c/plot)
                                          (* 2 (:circle-radius c/plot)))
                                       (- close minimum-close))
                                    close-spread)))
            x-axis-labels (let [n 7
                                length (:width c/plot)
                                spacing (/ length n)]
                            (->> (iterate (partial + spacing) (/ spacing 2))
                                 (take n)
                                 (map (partial * (/ instant-spread length)))
                                 (map (partial + minimum-instant))
                                 (map (partial t.coerce/from-long))
                                 (map (partial t.format/unparse label-formatter))))
            y-axis-labels (let [n 5
                                length (:height c/plot)
                                spacing (/ length n)]
                            (->> (iterate (partial + spacing) (/ spacing 2))
                                 (take n)
                                 (reverse)
                                 (map (partial * (/ close-spread length)))
                                 (map (partial + minimum-close))
                                 (map (partial format/format "%.1f"))))
            overlays (as-> ticks $
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
                                           (:right (last $)))}]))]
        [:div
         {:class (u/bem [:ticker])}
         [:div
          {:class (u/bem [:ticker__body])}
          [:div
           {:class (u/bem [:ticker__section])}
           [:div
            {:class (u/bem [:ticker__title])}
            [:div
             {:class (u/bem [:text :font-size-x-huge :font-weight-bold :colour-black-light])}
             "AAPL"]
            [:div
             {:class (u/bem [:text :font-size-large :colour-grey-medium :padding-left-small])}
             "daily USD close"]]
           [:svg
            {:xmlns "http://www.w3.org/2000/svg"
             :viewBox (u/view-box (:width c/plot) (:height c/plot))
             :class (u/bem [:ticker__plot])}
            [:g
             {:class (u/bem [:ticker__plot__lines])}
             (doall
              (for [[initial final] (partition 2 1 ticks)]
                [:line
                 {:key (:instant initial)
                  :x1 (normalise-instant (:instant initial))
                  :y1 (normalise-close (:close initial))
                  :x2 (normalise-instant (:instant final))
                  :y2 (normalise-close (:close final))}]))]
            [:g
             {:class (u/bem [:ticker__plot__circles])}
             (doall
              (for [{:keys [instant close]} ticks]
                [:circle
                 {:key instant
                  :cx (normalise-instant instant)
                  :cy (normalise-close close)
                  :r (:circle-radius c/plot)}]))]]

           (doall
            (for [{:keys [id left width]} overlays]
              [:div
               {:key id
                :class (u/bem [:ticker__overlay])
                :on-mouse-enter (fn [e]
                                  (re-frame/dispatch [:update-focused-tick-id id])
                                  (.preventDefault e))
                :on-mouse-leave (fn [e]
                                  (re-frame/dispatch [:update-focused-tick-id nil])
                                  (.preventDefault e))
                :style {:left left
                        :width width}}]))

           (when (some? focused-tick)
             [:div
              {:class (u/bem [:ticker__tooltip-container])
               :style {:top (+ (normalise-close (:close focused-tick))
                               (-> c/filling :xxx-large)
                               (-> c/tooltip :height -))
                       :left (- (normalise-instant (:instant focused-tick))
                                (/ (:width c/tooltip) 2))}}
              [tooltip]])

           ;; TODO extract x-axis
           [:div
            {:class (u/bem [:ticker__x-axis])}
            [:div
             {:class (u/bem [:ticker__x-axis__runner])}]
            [:div
             {:class (u/bem [:ticker__x-axis__labels])}
             (doall
              (for [x-axis-label x-axis-labels]
                [:div
                 {:key x-axis-label
                  :class (u/bem [:ticker__x-axis__labels__label])}
                 [:div
                  {:class (u/bem [:text :font-size-xx-small :font-weight-bold :colour-grey-medium :align-center])}
                  x-axis-label]]))]]]

          ;; TODO extract y-axis
          [:div
           {:class (u/bem [:ticker__section])}
           [:div
            {:class (u/bem [:ticker__y-axis])}
            [:div
             {:class (u/bem [:ticker__y-axis__runner])}]
            [:div
             {:class (u/bem [:ticker__y-axis__labels])}
             (doall
              (for [y-axis-label y-axis-labels]
                [:div
                 {:key y-axis-label
                  :class (u/bem [:ticker__y-axis__labels__label])}
                 [:div
                  {:class (u/bem [:text :font-size-xx-small :font-weight-bold :colour-grey-medium :align-center])}
                  y-axis-label]]))]]]]]))))


(defn app []
  (let [!initialising-routing? (re-frame/subscribe [:initialising-routing?])
        !initialising-ticks? (re-frame/subscribe [:initialising-ticks?])
        !initialising-notes? (re-frame/subscribe [:initialising-notes?])]
    (fn []
      [:div
       {:class (u/bem [:app])}
       [notification
        {:type :error
         :paragraph "This application requires a larger browser window."}]
       [:div
        {:class (u/bem [:page])}
        [:div
         {:class (u/bem [:page__header])}]
        [:div
         {:class (u/bem [:page__body])}
         (if (or @!initialising-routing?
                 @!initialising-ticks?
                 @!initialising-notes?)
           [:div
            {:class (u/bem [:page__sections])}
            [:div
             {:class (u/bem [:page__sections__section])}
             [:div
              {:class (u/bem [:text :font-size-medium :padding-top-xx-large :align-center])}
              "Loading"]]]
           [:div
            {:class (u/bem [:page__sections])}
            [:div
             {:class (u/bem [:page__sections__section :fixed])}
             [ticker]
             [note-timeline]
             [note-adder]]
            [:div
             {:class (u/bem [:page__sections__section])}
             [notes]]])]
        [:div
         {:class (u/bem [:page__footer])}]]])))

