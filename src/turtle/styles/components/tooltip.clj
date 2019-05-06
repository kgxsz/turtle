(ns styles.components.tooltip
  (:require [styles.constants :as c]
            [styles.utils :as u]
            [garden.def :refer [defstyles]]
            [garden.units :refer [px percent ms vh vw]]))


(defstyles tooltip
  [:.tooltip
   {:top (px (- (:xx-large c/filling) (:height c/tooltip)))
    :left (px (u/halve (- (:width c/tooltip))))
    :width (px (:width c/tooltip))
    :height (px (:height c/tooltip))}

   [:&__locus
    {:top (px (- (:height c/tooltip) (u/halve (:x-small c/filling))))
     :left (px (u/halve (- (:width c/tooltip) (:x-small c/filling))))
     :border-radius (percent (:50 c/proportion))}]

   [:&__pointer
    {:top (px (- (:height c/tooltip) (:x-small c/filling) (:tiny c/filling)))
     :left (px (u/halve (- (:width c/tooltip) (:x-small c/filling))))
     :border-top [[(px (u/halve (:x-small c/filling))) :solid (:black-two c/colour)]]
     :border-right [[(px (u/halve (:x-small c/filling))) :solid :transparent]]
     :border-bottom [[(px (u/halve (:x-small c/filling))) :solid :transparent]]
     :border-left [[(px (u/halve (:x-small c/filling))) :solid :transparent]]}]])
