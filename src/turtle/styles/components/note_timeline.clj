(ns styles.components.note-timeline
  (:require [styles.constants :as c]
            [styles.utils :as u]
            [garden.def :refer [defstyles]]
            [garden.units :refer [px percent ms vh vw]]))


(defstyles note-timeline
  [:.note-timeline
   {:display :flex
    :flex-direction :column
    :align-items :center
    :height (px (:tiny c/filling))
    :background-color (:grey-light c/colour)}

   [:&__notes
    {:display :flex
     :flex-direction :row
     :position :relative
     :width (px (:width c/plot))
     :height (px 10)
     :margin-left (px (:xxx-large c/spacing))
     :margin-right (px (:x-huge c/spacing))}]

   [:&__note
    {:position :absolute
     :opacity 0.2
     :background-color (:black-light  c/colour)
     :width (px (:x-small c/filling))
     :height (px (:x-small c/filling))
     :margin-left (px (u/halve (- (:x-small c/filling))))
     :top (px (u/halve (- (:tiny c/filling) (:x-small c/filling))))
     :border-radius (percent (:50 c/proportion))}]])
