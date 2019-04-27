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
    :height (-> c/filling :tiny px)
    :background-color (:grey-light c/colour)}

   [:&__markers
    {:display :flex
     :flex-direction :row
     :position :relative
     :width (-> c/plot :width px)
     :margin-left (-> c/spacing :xxx-large px)
     :margin-right (-> c/spacing :x-huge px)}]

   [:&__marker
    {:position :absolute
     :opacity 0.2
     :background-color (:black-light  c/colour)
     :width (-> c/filling :x-small px)
     :height (-> c/filling :x-small px)
     :top (-> (:tiny c/filling) (- (:x-small c/filling)) (/ 2) px)
     :border-radius (-> c/proportion :50 percent)}]])
