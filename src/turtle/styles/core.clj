(ns styles.core
  (:require [styles.components.text :refer [text]]
            [styles.components.icon :refer [icon]]
            [styles.components.page :refer [page]]
            [styles.components.notification :refer [notification]]
            [styles.components.ticker :refer [ticker]]
            [styles.components.note :refer [note]]
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
    :list-style-type :none
    :margin 0
    :padding 0}]

  ;; fonts
  fonts/icomoon

  ;; components
  icon
  text
  notification
  page
  ticker
  note)
