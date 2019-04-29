(ns client.views.ticker
  (:require [re-frame.core :as re-frame]
            [client.utils :as u]
            [client.views.tooltip :refer [tooltip]]
            [styles.constants :as c]))


(defn view [{:keys [tick-positions focused-tick-position x-axis-labels y-axis-labels]}]
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
        (for [[initial final] (partition 2 1 tick-positions)]
          [:line
           {:key (:id initial)
            :x1 (:x initial)
            :y1 (:y initial)
            :x2 (:x final)
            :y2 (:y final)}]))]
      [:g
       {:class (u/bem [:ticker__plot__circles])}
       (doall
        (for [{:keys [id x y]} tick-positions]
          [:circle
           {:key id
            :cx x
            :cy y
            :r (:circle-radius c/plot)}]))]]

     (doall
      (for [{:keys [id left width]} tick-positions]
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

     (when (some? focused-tick-position)
       [:div
        {:class (u/bem [:ticker__tooltip-container])
         :style {:top (:y focused-tick-position)
                 :left (:x focused-tick-position)}}
        [tooltip]])

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
            y-axis-label]]))]]]]])


(defn ticker []
  (let [!ticks (re-frame/subscribe [:ticks])
        !focused-tick (re-frame/subscribe [:focused-tick])]
    (fn []
      (let [ticks @!ticks
            focused-tick @!focused-tick
            tick-positions (u/tick-positions ticks)
            closes (map :close ticks)
            instants (map :instant ticks)
            maximum-close (apply max closes)
            minimum-close (apply min closes)
            maximum-instant (apply max instants)
            minimum-instant (apply min instants)]
        [view
         {:tick-positions tick-positions
          :focused-tick-position (u/get-by-id (:id focused-tick) tick-positions)
          :x-axis-labels (let [n 7
                               length (:width c/plot)
                               spacing (/ length n)]
                           (->> (iterate (partial + spacing) (u/halve spacing))
                                (take n)
                                (map (partial * (/ (- maximum-instant minimum-instant) length)))
                                (map (partial + minimum-instant))
                                (map u/format-compact-time)))
          :y-axis-labels (let [n 5
                               length (:height c/plot)
                               spacing (/ length n)]
                           (->> (iterate (partial + spacing) (u/halve spacing))
                                (take n)
                                (reverse)
                                (map (partial * (/ (- maximum-close minimum-close) length)))
                                (map (partial + minimum-close))
                                (map u/format-price)))}]))))
