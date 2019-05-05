(ns client.views.ticker
  (:require [re-frame.core :as re-frame]
            [client.utils :as u]
            [client.views.tooltip :as tooltip]
            [styles.constants :as c]))


(defn view [{:keys [lines circles overlays tooltip-active? tooltip-container instant-axis close-axis]}
            {:keys [tooltip]}
            {:keys [on-mouse-enter on-mouse-leave]}]
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
       :viewBox (u/view-box (:width c/ticker-plot) (:height c/ticker-plot))
       :class (u/bem [:ticker__plot])}
      [:g
       {:class (u/bem [:ticker__plot__lines])}
       (doall
        (for [{:keys [tick-id x1 x2 y1 y2]} lines]
          [:line
           {:key tick-id
            :x1 x1
            :y1 y1
            :x2 x2
            :y2 y2}]))]
      [:g
       {:class (u/bem [:ticker__plot__circles])}
       (doall
        (for [{:keys [tick-id cx cy]} circles]
          [:circle
           {:key tick-id
            :cx cx
            :cy cy
            :r (:circle-radius c/ticker-plot)}]))]]

     (doall
      (for [{:keys [tick-id left width]} overlays]
        [:div
         {:key tick-id
          :class (u/bem [:ticker__overlay])
          :on-mouse-enter (partial on-mouse-enter tick-id)
          :on-mouse-leave on-mouse-leave
          :style {:left left
                  :width width}}]))

     (when tooltip-active?
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
            tick-positions (u/tick-positions ticks)
            {:keys [tick-id]} (or @!hovered-tick @!clicked-tick)]
        [view
         {:lines (for [[initial final] (partition 2 1 tick-positions)]
                   {:tick-id (:tick-id initial)
                    :x1 (:x initial)
                    :y1 (:y initial)
                    :x2 (:x final)
                    :y2 (:y final)})
          :circles (for [{:keys [tick-id x y]} tick-positions]
                     {:tick-id tick-id
                      :cx x
                      :cy y})
          :overlays (for [tick-position tick-positions]
                      (select-keys tick-position [:tick-id :left :width]))
          :tooltip-active? (some? tick-id)
          :tooltip-container (let [{:keys [x y]} (u/tick-position tick-id ticks)]
                               {:tick-id tick-id
                                :top y
                                :left x})
          :instant-axis (u/instant-axis ticks)
          :close-axis (u/close-axis ticks)}
         {:tooltip tooltip/tooltip}
         {:on-mouse-enter #(re-frame/dispatch [:update-hovered-tick %])
          :on-mouse-leave #(re-frame/dispatch [:update-hovered-tick])}]))))
