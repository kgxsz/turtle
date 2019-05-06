(ns styles.components.note
  (:require [styles.constants :as c]
            [styles.utils :as u]
            [garden.def :refer [defstyles]]
            [garden.units :refer [px percent ms vh vw]]))


(defstyles note
  [:.note
   [:&__body
    {:height (px (:height c/note))
     :width (px (:width c/note))
     :border [[:solid (px (:xxx-tiny c/filling)) (:white-three c/colour)]]}]])
