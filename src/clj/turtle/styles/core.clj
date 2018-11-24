(ns turtle.styles.core
  (:require [turtle.styles.components :as components]
            [turtle.styles.constants :as c]
            [turtle.styles.fonts :as fonts]
            [garden.def :refer [defstyles]]
            [garden.stylesheet :refer [at-keyframes]]
            [garden.units :refer [px percent ms]]
            [normalize.core :refer [normalize]]))


(defstyles app

  ;; third party css
  normalize

  ;; foundations
  [:*
   {:box-sizing :border-box
    :margin 0
    :padding 0}]

  ;; fonts
  fonts/icomoon

  ;; components
  components/icon
  components/text
  components/underlay
  components/notification
  components/page)
