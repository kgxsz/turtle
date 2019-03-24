(ns styles.core
  (:require [styles.components :as components]
            [styles.constants :as c]
            [styles.fonts :as fonts]
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
  components/notification
  components/page
  components/ticker
  components/notes)
