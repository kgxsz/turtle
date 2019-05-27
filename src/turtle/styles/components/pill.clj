(ns styles.components.pill
  (:require [styles.constants :as c]
            [styles.utils :as u]
            [garden.def :refer [defstyles]]
            [garden.units :refer [px percent ms vh vw]]))


(defstyles pill
  [:.pill
   {:cursor :default
    :border-radius (px (:medium c/filling))}
   [:&--clickable
    {:cursor :pointer}]])
