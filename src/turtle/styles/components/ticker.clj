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
    :background-color (:white-medium c/colour)}

   [:&__body
    {:display :flex
     :flex-direction :row
     :position :relative
     :width (-> c/plot :width px)
     :margin-left (-> c/spacing :xxx-large px)
     :margin-right (-> c/spacing :x-huge px)}]

   [:&__section
    {:position :relative}]

   [:&__title
    {:display :flex
     :flex-direction :row
     :align-items :baseline
     :height (-> c/filling :xxx-large px)}]

   [:&__plot
    {:display :block
     :height (-> c/plot :height px)
     :width (-> c/plot :width px)}

    [:&__circles
     {:fill (:black-light c/colour)}]

    [:&__lines
     {:stroke (:black-light c/colour)
      :stroke-width (-> c/filling :xx-tiny)}]]

   [:&__overlay
    {:position :absolute
     :top 0
     :bottom 0
     :cursor :crosshair
     :z-index 2}]

   [:&__x-axis
    {:position :relative
     :height (-> c/filling :huge px)}

    [:&__runner
     {:position :absolute
      :height (-> c/filling :x-tiny px)
      :top (-> c/spacing :xx-large px)
      :left 0
      :right 0
      :background-color (:grey-light c/colour)}]

    [:&__labels
     {:display :flex
      :flex-direction :row
      :justify-content :space-around}

     [:&__label
      {:width (-> c/filling :xxx-large px)
       :margin-top (-> c/spacing :x-large px)
       :padding (-> c/filling :tiny px)
       :z-index 1
       :background-color (:white-medium c/colour)}]]]

   [:&__y-axis
    {:position :relative
     :margin-top (-> c/filling :xxx-large px)
     :width (-> c/filling :xxx-large px)
     :height (-> c/plot :height px)}

    [:&__runner
     {:position :absolute
      :width (-> c/filling :x-tiny px)
      :height (-> c/plot :height px)
      :left (-> c/spacing :xx-large px)
      :background-color (:grey-light c/colour)}]

    [:&__labels
     {:display :flex
      :flex-direction :column
      :justify-content :space-around
      :height (-> c/plot :height px)}

     [:&__label
      {:height (-> c/filling :small px)
       :width (-> c/filling :x-large px)
       :margin-left (-> c/spacing :large px)
       :z-index 1
       :background-color (:white-medium c/colour)}]]]

   [:&__tooltip-container
    {:position :absolute
     :pointer-events :none
     :z-index 2}]])
