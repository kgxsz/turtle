(ns styles.components.pills
  (:require [styles.constants :as c]
            [styles.utils :as u]
            [garden.def :refer [defstyles]]
            [garden.units :refer [px percent ms vh vw]]))


(defstyles pills
  [:.pills
   {:border-bottom [[:solid (px (:xxx-tiny c/filling)) (:white-three c/colour)]]}
   [:&__body
    {:width (px (:width c/pills))}]])
