(ns styles.components.notification
  (:require [styles.constants :as c]
            [garden.def :refer [defstyles]]
            [garden.units :refer [px percent ms vh vw]]))

(defstyles notification
  [:.notification
   {:left 0
    :right 0
    :top 0
    :z-index -1
    :border-bottom :solid
    :border-width (px (:xx-tiny c/filling))}

   [:&--success
    {:background-color (:green-one c/colour)
     :border-color (:green-two c/colour)
     :color (:green-two c/colour)}]

   [:&--warning
    {:background-color (:yellow-one c/colour)
     :border-color (:yellow-two c/colour)
     :color (:yellow-two c/colour)}]

   [:&--error
    {:background-color (:red-one c/colour)
     :border-color (:red-two c/colour)
     :color (:red-two c/colour)}]])
