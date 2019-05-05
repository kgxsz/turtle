(ns client.views.ticker
  (:require [re-frame.core :as re-frame]
            [client.utils :as u]
            [client.views.tooltip :refer [tooltip]]
            [styles.constants :as c]))


(defn view [{:keys [tick-positions tooltip-container-active? tooltip-container instant-axis close-axis]}]
  [:div
   {:class (u/bem [:ticker])}
   [:div
    {:class (u/bem [:ticker__body])}
    [:div
     {:class (u/bem [:ticker__section])}
     [:div
      {:class (u/bem [:ticker__title])}
      [:div
       {:class (u/bem [:text :font-size-x-huge :font-weight-bold :colour-black-two])}
       "AAPL"]
      [:div
       {:class (u/bem [:text :font-size-large :colour-grey-one :padding-left-small])}
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
          :on-mouse-enter #(re-frame/dispatch [:update-hovered-tick tick-id])
          :on-mouse-leave #(re-frame/dispatch [:update-hovered-tick])
          :style {:left left
                  :width width}}]))

     (when tooltip-container-active?
       (let [{:keys [tick-id top left]} tooltip-container]
         [:div
          {:key tick-id
           :class (u/bem [:ticker__tooltip-container])
           :style {:top top
                   :left left}}
          [tooltip tick-id]]))

     [:div
      {:class (u/bem [:ticker__instant-axis])}
      [:div
       {:class (u/bem [:ticker__instant-axis__runner])}]
      [:div
       {:class (u/bem [:ticker__instant-axis__labels])}
       (doall
        (for [instant instant-axis]
          [:div
           {:key instant
            :class (u/bem [:ticker__instant-axis__labels__label])}
           [:div
            {:class (u/bem [:text :font-size-xx-small :font-weight-bold :colour-grey-one :align-center])}
            (u/format-compact-time instant)]]))]]]

    [:div
     {:class (u/bem [:ticker__section])}
     [:div
      {:class (u/bem [:ticker__close-axis])}
      [:div
       {:class (u/bem [:ticker__close-axis__runner])}]
      [:div
       {:class (u/bem [:ticker__close-axis__labels])}
       (doall
        (for [close close-axis]
          [:div
           {:key close
            :class (u/bem [:ticker__close-axis__labels__label])}
           [:div
            {:class (u/bem [:text :font-size-xx-small :font-weight-bold :colour-grey-one :align-center])}
            (u/format-price close)]]))]]]]])


(defn ticker []
  (let [!ticks (re-frame/subscribe [:ticks])
        !clicked-tick (re-frame/subscribe [:clicked-tick])
        !hovered-tick (re-frame/subscribe [:hovered-tick])]
    (fn []
      (let [ticks @!ticks
            {:keys [tick-id] :as focused-tick} (or @!hovered-tick @!clicked-tick)]
        [view
         {:tick-positions (u/tick-positions ticks)
          :tooltip-container-active? (some? focused-tick)
          :tooltip-container (when-let [{:keys [x y]} (some-> tick-id (u/tick-position ticks))]
                               {:tick-id tick-id
                                :top y
                                :left x})
          :instant-axis (u/instant-axis ticks)
          :close-axis (u/close-axis ticks)}]))))
