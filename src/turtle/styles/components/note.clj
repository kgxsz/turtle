(ns styles.components.note
  (:require [styles.constants :as c]
            [styles.utils :as u]
            [garden.def :refer [defstyles]]
            [garden.units :refer [px percent ms vh vw]]))


(defstyles note
  [:.note
   [:&__body
    {:width (px (:width c/note))
     :border [[:solid (px (:xxx-tiny c/filling)) (:white-three c/colour)]]}
    [:&--focused
     {:border [[:solid (px (:xxx-tiny c/filling)) (:grey-three c/colour)]]}]]
   [:&__symbol
    {:cursor :default
     :border-radius (px (:medium c/filling))}]
   [:&__delete
    {:cursor :pointer}]])
