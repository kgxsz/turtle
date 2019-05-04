(ns styles.components.button
  (:require [styles.constants :as c]
            [styles.utils :as u]
            [garden.def :refer [defstyles]]
            [garden.units :refer [px percent ms vh vw]]))


(defstyles button
  [:.button
   {:display :flex
    :flex-direction :row
    :justify-content :center
    :align-items :center
    :cursor :pointer
    :width (px (:huge c/filling))
    :height (px (:x-large c/filling))
    :background-color (:black-two c/colour)}

   [:&--disabled
    {:cursor :not-allowed
     :opacity 0.3}]

   [:&--primary
    {:background-color (:black-two c/colour)}]

   [:&--secondary
    {:background-color (:white-one c/colour)
     :border [[:solid (px (:xx-tiny c/filling)) (:black-two c/colour)]]}]

   [:&__label
    {:font-size (px (:medium c/font-size))}

    [:&--primary
     {:color (:white-one c/colour)}]

    [:&--secondary
     {:color (:black-one c/colour)}]]])
