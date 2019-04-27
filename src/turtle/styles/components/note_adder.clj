(ns styles.components.note-adder
  (:require [styles.constants :as c]
            [styles.utils :as u]
            [garden.def :refer [defstyles]]
            [garden.units :refer [px percent ms vh vw]]))


(defstyles note-adder
  [:.note-adder
   {:display :flex
    :flex-direction :column
    :align-items :center
    :height (px 100) #_(-> c/filling :tiny px)
    :background-color :pink #_(:grey-light c/colour)}

   [:&__body
    {:position :relative
     :top (-> (:tiny c/filling) (+ (:x-large c/filling)) - (/ 2) px)
     :width (-> c/plot :width px)
     :height (-> c/filling :x-large px)
     :margin-left (-> c/spacing :xxx-large px)
     :margin-right (-> c/spacing :x-huge px)}]

   [:&__overlay
    {:position :absolute
     :top 0
     :bottom 0
     :cursor :pointer
     :z-index 1}]

   [:&__plus-button
    {:position :absolute
     :pointer-events :none
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
