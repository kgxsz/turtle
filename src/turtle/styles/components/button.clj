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
    :background-color (:black-light c/colour)}

   [:&--disabled
    {:cursor :not-allowed
     :opacity 0.3}]

   [:&--primary
    {:background-color (:black-light c/colour)}]

   [:&--secondary
    {:background-color (:white-light c/colour)
     :border [[:solid (px (:xx-tiny c/filling)) (:black-light c/colour)]]}]

   [:&__label
    {:font-size (px (:medium c/font-size))}

    [:&--primary
     {:color (:white-light c/colour)}]

    [:&--secondary
     {:color (:black-light c/colour)}]]])
