(ns styles.components.button
  (:require [styles.constants :as c]
            [styles.utils :as u]
            [garden.def :refer [defstyles]]
            [garden.units :refer [px percent ms vh vw]]))


(defstyles button
  [:.button
   {:cursor :pointer
    :border [[:solid (px (:xx-tiny c/filling)) (:black-two c/colour)]]}

   [:&--disabled
    {:cursor :not-allowed
     :opacity (:10 c/fraction)}]

   [:&--primary
    {:color (:white-one c/colour)
     :background-color (:black-two c/colour)}]

   [:&--secondary
    {:color (:black-two c/colour)
     :background-color (:white-one c/colour)}]])
