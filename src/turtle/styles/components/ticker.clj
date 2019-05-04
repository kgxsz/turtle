(ns styles.components.ticker
  (:require [styles.constants :as c]
            [styles.utils :as u]
            [garden.def :refer [defstyles]]
            [garden.units :refer [px percent ms vh vw]]))


(defstyles ticker
  [:.ticker
   {:display :flex
    :flex-direction :column
    :align-items :center
    :background-color (:white-two c/colour)}

   [:&__body
    {:display :flex
     :flex-direction :row
     :position :relative
     :width (px (:width c/plot))
     :margin-left (px (:xxx-large c/spacing))
     :margin-right (px (:x-huge c/spacing))}]

   [:&__section
    {:position :relative}]

   [:&__title
    {:display :flex
     :flex-direction :row
     :align-items :baseline
     :height (px (:xxx-large c/filling))}]

   [:&__plot
    {:display :block
     :height (px (:height c/plot))
     :width (px (:width c/plot))}

    [:&__circles
     {:fill (:black-two c/colour)}]

    [:&__lines
     {:stroke (:black-two c/colour)
      :stroke-width (px (:xx-tiny c/filling))}]]

   [:&__overlay
    {:position :absolute
     :top 0
     :bottom 0
     :cursor :crosshair
     :z-index 2}]

   [:&__x-axis
    {:position :relative
     :height (px (:huge c/filling))}

    [:&__runner
     {:position :absolute
      :height (px (:x-tiny c/filling))
      :top (px (:xx-large c/spacing))
      :left 0
      :right 0
      :background-color (:white-three c/colour)}]

    [:&__labels
     {:display :flex
      :flex-direction :row
      :justify-content :space-around}

     [:&__label
      {:width (px (:xxx-large c/filling))
       :margin-top (px (:x-large c/spacing))
       :padding (px (:tiny c/filling))
       :z-index 1
       :background-color (:white-two c/colour)}]]]

   [:&__y-axis
    {:position :relative
     :margin-top (px (:xxx-large c/filling))
     :width (px (:xxx-large c/filling))
     :height (px (:height c/plot))}

    [:&__runner
     {:position :absolute
      :width (px (:x-tiny c/filling))
      :height (px (:height c/plot))
      :left (px (:xx-large c/spacing))
      :background-color (:white-three c/colour)}]

    [:&__labels
     {:display :flex
      :flex-direction :column
      :justify-content :space-around
      :height (-> c/plot :height px)}

     [:&__label
      {:height (px (:small c/filling))
       :width (px (:x-large c/filling))
       :margin-left (px (:large c/spacing))
       :z-index 1
       :background-color (:white-two c/colour)}]]]

   [:&__tooltip-container
    {:position :absolute
     :pointer-events :none
     :z-index 2}]])
