(ns client.views.ticker
  (:require [re-frame.core :as re-frame]
            [client.utils :as u]
            [client.views.tooltip :as tooltip]
            [styles.constants :as c]))


(defn view [{:keys [symbol lines circles overlays tooltip-container instant-axis close-axis]}
            {:keys [tooltip]}
            {:keys [on-mouse-over on-mouse-out]}]
  [:div
   {:class (u/bem [:ticker]
                  [:cell :column :width-cover :colour-white-two])}
   [:div
    {:class (u/bem [:ticker__body]
                   [:cell :row :justify-start :align-start :relative :margin-left-xxx-large :margin-right-x-huge])}
    [:div
     {:class (u/bem [:cell :relative])}
     [:div
      {:class (u/bem [:cell :row :justify-start :align-baseline :height-xx-large :padding-top-small])}
      [:div
       {:class (u/bem [:text :font-size-x-huge :font-weight-bold :colour-black-two])}
       (u/format-symbol symbol)]
      [:div
       {:class (u/bem [:text :font-size-large :colour-grey-one :padding-left-small])}
       "daily close"]]
     [:svg
      {:xmlns "http://www.w3.org/2000/svg"
       :viewBox (u/view-box (:width c/ticker-plot) (:height c/ticker-plot))
       :class (u/bem [:ticker__plot]
                     [:cell :relative])}
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
          :class (u/bem [:ticker__overlay]
                        [:cell :absolute])
          :on-mouse-over (partial on-mouse-over tick-id)
          :on-mouse-out on-mouse-out
          :style {:left left
                  :width width}}]))

     (when-let [{:keys [tick-id top left]} tooltip-container]
       [:div
        {:key tick-id
         :class (u/bem [:ticker__tooltip-container]
                       [:cell :absolute])
         :style {:top top
                 :left left}}
        [tooltip tick-id]])

     [:div
      {:class (u/bem [:cell :relative :height-huge])}
      [:div
       {:class (u/bem [:ticker__instant-axis-runner]
                      [:cell :absolute :height-x-tiny :colour-white-three])}]
      [:div
       {:class (u/bem [:cell :row :justify-space-around])}
       (doall
        (for [instant instant-axis]
          [:div
           {:key instant
            :class (u/bem [:ticker__instant-axis-label]
                          [:cell :width-xxx-large :margin-top-x-large :padding-tiny :colour-white-two])}
           [:div
            {:class (u/bem [:text :font-size-x-small :font-weight-bold :colour-grey-one :align-center])}
            (u/format-compact-time instant)]]))]]]

    [:div
     {:class (u/bem [:cell :relative])}
     [:div
      {:class (u/bem [:cell :relative :margin-top-xxx-large])}
      [:div
       {:class (u/bem [:ticker__close-axis-runner]
                      [:cell :absolute :width-x-tiny :colour-white-three])}]
      [:div
       {:class (u/bem [:ticker__close-axis-labels]
                      [:cell :column :justify-space-around])}
       (doall
        (for [close close-axis]
          [:div
           {:key close
            :class (u/bem [:ticker__close-axis-label]
                          [:cell :width-x-large :height-small :margin-left-large :colour-white-two])}
           [:div
            {:class (u/bem [:text :font-size-x-small :font-weight-bold :colour-grey-one :align-center])}
            (u/format-price close)]]))]]]]])


(defn ticker []
  (let [!symbol (re-frame/subscribe [:symbol])
        !ticks (re-frame/subscribe [:ticks])
        !clicked-tick (re-frame/subscribe [:clicked-tick])
        !hovered-tick (re-frame/subscribe [:hovered-tick])
        !hovered-note (re-frame/subscribe [:hovered-note])]
    (fn []
      (let [ticks @!ticks
            tick-positions (u/tick-positions ticks)
            {:keys [tick-id]} (or @!hovered-tick (:tick @!hovered-note) @!clicked-tick)]
        [view
         {:symbol @!symbol
          :lines (map
                  (fn [[initial final]]
                    {:tick-id (:tick-id initial)
                     :x1 (:x initial)
                     :y1 (:y initial)
                     :x2 (:x final)
                     :y2 (:y final)})
                  (partition 2 1 tick-positions))
          :circles (map
                    (fn [{:keys [tick-id x y]}]
                      {:tick-id tick-id
                       :cx x
                       :cy y})
                    tick-positions)
          :overlays (map
                     (fn [tick-position]
                       (select-keys tick-position [:tick-id :left :width]))
                     tick-positions)
          :tooltip-container (when-let [{:keys [x y]} (u/tick-position tick-id ticks)]
                               {:tick-id tick-id
                                :top y
                                :left x})
          :instant-axis (u/instant-axis ticks)
          :close-axis (u/close-axis ticks)}
         {:tooltip tooltip/tooltip}
         {:on-mouse-over #(re-frame/dispatch [:update-hovered-tick %])
          :on-mouse-out #(re-frame/dispatch [:update-hovered-tick])}]))))
