(ns styles.components.note-timeline
  (:require [styles.constants :as c]
            [styles.utils :as u]
            [garden.def :refer [defstyles]]
            [garden.units :refer [px percent ms vh vw]]))


(defstyles note-timeline
  [:.note-timeline
   [:&__markers
    {:width (px (:width c/ticker-plot))}
    [:&__marker
     {:margin-left (px (u/halve (- (:x-small c/filling))))
      :top (px (u/halve (- (:tiny c/filling) (:x-small c/filling))))
      :border-radius (percent (:50 c/proportion))
      :opacity (c/fraction :20)}
     [:&--focused
      {:opacity (c/fraction :50)}]]]])
