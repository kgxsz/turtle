(ns styles.components.tooltip
  (:require [styles.constants :as c]
            [styles.utils :as u]
            [garden.def :refer [defstyles]]
            [garden.units :refer [px percent ms vh vw]]))


(defstyles tooltip
  [:.tooltip
   {:position :absolute
    :width (-> c/tooltip :width px)
    :height (-> c/tooltip :height px)}

   [:&__locus
    {:position :absolute
     :opacity 0.2
     :background-color (:black-light  c/colour)
     :width (-> c/filling :x-small px)
     :height (-> c/filling :x-small px)
     :top (px (- (-> c/tooltip :height) (-> c/filling :x-small (/ 2))))
     :left (px (- (-> c/tooltip :width (/ 2)) (-> c/filling :x-small (/ 2))))
     :border-radius (-> c/proportion :50 percent)}]

   [:&__pointer
    {:position :absolute
     :opacity 0.4
     :top (px (- (-> c/tooltip :height) (-> c/filling :x-small) (-> c/filling :tiny)))
     :left (px (- (-> c/tooltip :width (/ 2)) (-> c/filling :x-small (/ 2))))
     :border-top [[(-> c/filling :x-small (/ 2) px) :solid (:black-light c/colour)]]
     :border-right [[(-> c/filling :x-small (/ 2) px) :solid :transparent]]
     :border-bottom [[(-> c/filling :x-small (/ 2) px) :solid :transparent]]
     :border-left [[(-> c/filling :x-small (/ 2) px) :solid :transparent]]}]

   [:&__backing
    {:position :absolute
     :opacity 0.4
     :background-color (:black-light c/colour)
     :width (-> c/filling :huge px)
     :height (-> c/filling :xx-large px)}]

   [:&__date
    {:position :relative
     :margin-top (-> c/spacing :xx-small px)}]

   [:&__close
    {:position :relative
     :display :flex
     :flex-direction :row
     :justify-content :center
     :align-items :baseline}]])
