(ns styles.components.ticker
  (:require [styles.constants :as c]
            [styles.utils :as u]
            [garden.def :refer [defstyles]]
            [garden.units :refer [px percent ms vh vw]]))


(defstyles ticker
  [:.ticker
   {:width (vw (:100 c/proportion))}

   [:&__body
    {:width (px (:width c/ticker-plot))}]

   [:&__plot
    {:height (px (:height c/ticker-plot))
     :width (px (:width c/ticker-plot))}

    [:&__circles
     {:fill (:black-two c/colour)}]

    [:&__lines
     {:stroke (:black-two c/colour)
      :stroke-width (px (:xx-tiny c/filling))}]]

   [:&__overlay
    {:top 0
     :bottom 0
     :cursor :crosshair
     :z-index 2}]

   [:&__instant-axis-runner
    {:top (px (:xx-large c/spacing))
     :left 0
     :right 0}]

   [:&__instant-axis-label
    {:z-index 1}]

   [:&__close-axis-runner
    {:height (px (:height c/ticker-plot))
     :left (px (:xx-large c/spacing))}]

   [:&__close-axis-labels
    {:height (-> c/ticker-plot :height px)}]

   [:&__close-axis-label
    {:z-index 1}]

   [:&__tooltip-container
    {:pointer-events :none
     :z-index 2}]])
