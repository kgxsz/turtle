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
    :background-color (:white-medium c/colour)
    :border-bottom-width (-> c/filling :tiny px)
    :border-bottom-color (:grey-light c/colour)
    :border-bottom-style :solid}

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
     :z-index 2}

    [:&--upper
     {:cursor :crosshair
      :top 0
      :bottom (px
               (- (-> c/filling :x-large (/ 2))
                  (-> c/filling :tiny (/ 2))))}]
    [:&--lower
     {:cursor :pointer
      :top (px
            (+ (-> c/filling :xxx-large)
               (-> c/plot :height)
               (-> c/filling :huge)
               (-> c/filling :tiny (/ 2))
               (-> c/filling :x-large (/ -2))))
      :bottom (px
               (+ (-> c/filling :tiny (/ 2) -)
                  (-> c/filling :x-large (/ 2) -)))}]]

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
     :z-index 2}]

   [:&__note-marker
    {:position :absolute
     :opacity 0.2
     :background-color (:black-light  c/colour)
     :width (-> c/filling :x-small px)
     :height (-> c/filling :x-small px)
     :top (px
           (+ (-> c/filling :xxx-large)
              (-> c/plot :height)
              (-> c/filling :huge)
              (-> c/filling :tiny (/ 2))
              (-> c/filling :x-small (/ -2))))
     :border-radius (-> c/proportion :50 percent)}]

   [:&__note-adder
    {:position :absolute
     :top (px
           (+ (-> c/filling :xxx-large)
              (-> c/plot :height)
              (-> c/filling :huge)
              (-> c/filling :tiny (/ 2))
              (-> c/filling :x-large (/ -2))))
     :background-color (:black-light c/colour)
     :width (-> c/filling :x-large px)
     :height (-> c/filling :x-large px)
     :border-radius (-> c/proportion :50 percent)}

    [:&__cross
     {:position :absolute
      :background-color (:white-light c/colour)}

     [:&--vertical
      {:top (px (- (-> c/filling :x-large (/ 2))
                   (-> c/filling :small (/ 2))))
       :left (px (- (-> c/filling :x-large (/ 2))
                    (-> c/filling :xx-tiny (/ 2))))
       :background-color (:white-light c/colour)
       :width (-> c/filling :xx-tiny px)
       :height (-> c/filling :small px)}]

     [:&--horizontal
      {:top (px (- (-> c/filling :x-large (/ 2))
                   (-> c/filling :xx-tiny (/ 2))))
       :left (px (- (-> c/filling :x-large (/ 2))
                    (-> c/filling :small (/ 2))))
       :background-color (:white-light c/colour)
       :width (-> c/filling :small px)
       :height (-> c/filling :xx-tiny px)}]]]])
