(ns client.views.ticker
  (:require [re-frame.core :as re-frame]
            [client.utils :as u]
            [client.views.tooltip :refer [tooltip]]
            [styles.constants :as c]))


(defn view [{:keys [tick-positions focused-tick clicked-tick x-axis-labels y-axis-labels]}]
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
           {:key (:tick-id initial)
            :x1 (:x initial)
            :y1 (:y initial)
            :x2 (:x final)
            :y2 (:y final)}]))]
      [:g
       {:class (u/bem [:ticker__plot__circles])}
       (doall
        (for [{:keys [tick-id x y]} tick-positions]
          [:circle
           {:key tick-id
            :cx x
            :cy y
            :r (:circle-radius c/plot)}]))]]

     (doall
      (for [{:keys [tick-id left width]} tick-positions]
        [:div
         {:key tick-id
          :class (u/bem [:ticker__overlay])
          :on-mouse-enter #(re-frame/dispatch [:update-focused-tick tick-id])
          :on-mouse-leave #(re-frame/dispatch [:update-focused-tick])
          :style {:left left
                  :width width}}]))

     (when (or (some? focused-tick) (some? clicked-tick))
       (let [{:keys [tick-id] :as tick} (or focused-tick clicked-tick)
             {:keys [x y]} (u/find-tick-position tick tick-positions)]
         [:di v
          {:key tick-id
           :class (u/bem [:ticker__tooltip-container])
           :style {:top y
                   :left x}}
          [tooltip tick-id]]))

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
        !focused-tick (re-frame/subscribe [:focused-tick])
        !clicked-tick (re-frame/subscribe [:clicked-tick])]
    (fn []
      (let [ticks @!ticks
            sorted-instants (->> ticks (map :instant) (sort <))
            sorted-closes (->> ticks (map :close) (sort >))]
        [view
         {:tick-positions (u/tick-positions ticks)
          :focused-tick @!focused-tick
          :clicked-tick @!clicked-tick
          :x-axis-labels (u/axis-labels 7 (:width c/plot) sorted-instants u/format-compact-time)
          :y-axis-labels (u/axis-labels 5 (:height c/plot) sorted-closes u/format-price)}]))))
