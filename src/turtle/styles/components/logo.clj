(ns styles.components.logo
  (:require [styles.constants :as c]
            [styles.utils :as u]
            [garden.def :refer [defstyles]]
            [garden.units :refer [px percent ms vh vw]]))


(defstyles logo
  [:.logo
   [:&__line
    {:fill :none
     :stroke (:grey-two c/colour)
     :stroke-width (:xxx-small c/filling)}]
   [:&__circle
    {:fill (:black-three c/colour)}]])
