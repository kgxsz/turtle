(ns styles.components.tooltip
  (:require [styles.constants :as c]
            [styles.utils :as u]
            [garden.def :refer [defstyles]]
            [garden.units :refer [px percent ms vh vw]]))


(defstyles tooltip
  [:.tooltip
   {:position :absolute
    :width (px (:width c/tooltip))
    :height (px (:height c/tooltip))}

   [:&__locus
    {:position :absolute
     :opacity 0.2
     :background-color (:black-light  c/colour)
     :width (px (:x-small c/filling))
     :height (px (:x-small c/filling))
     :top (px (- (:height c/tooltip) (u/halve (:x-small c/filling))))
     :left (px (u/halve (- (:width c/tooltip) (:x-small c/filling))))
     :border-radius (percent (:50 c/proportion))}]

   [:&__pointer
    {:position :absolute
     :opacity 0.4
     :top (px (- (:height c/tooltip) (:x-small c/filling) (:tiny c/filling)))
     :left (px (u/halve (- (:width c/tooltip) (:x-small c/filling))))
     :border-top [[(px (u/halve (:x-small c/filling))) :solid (:black-light c/colour)]]
     :border-right [[(px (u/halve (:x-small c/filling))) :solid :transparent]]
     :border-bottom [[(px (u/halve (:x-small c/filling))) :solid :transparent]]
     :border-left [[(px (u/halve (:x-small c/filling))) :solid :transparent]]}]

   [:&__backing
    {:position :absolute
     :opacity 0.4
     :background-color (:black-light c/colour)
     :width (px (:huge c/filling))
     :height (px (:xx-large c/filling))}]

   [:&__instant-label
    {:position :relative
     :margin-top (px (:xx-small c/spacing))}]

   [:&__close-label
    {:position :relative
     :display :flex
     :flex-direction :row
     :justify-content :center
     :align-items :baseline}]])
