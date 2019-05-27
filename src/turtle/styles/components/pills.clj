(ns styles.components.pills
  (:require [styles.constants :as c]
            [styles.utils :as u]
            [garden.def :refer [defstyles]]
            [garden.units :refer [px percent ms vh vw]]))


(defstyles pills
  [:.pills
   {:backgound-color :green
    :border-bottom [[:solid (px (:tiny c/filling)) (:white-three c/colour)]]}])
