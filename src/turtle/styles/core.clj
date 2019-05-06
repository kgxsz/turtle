(ns styles.core
  (:require [styles.components.text :refer [text]]
            [styles.components.icon :refer [icon]]
            [styles.components.cell :refer [cell]]
            [styles.components.button :refer [button]]
            [styles.components.page :refer [page]]
            [styles.components.notification :refer [notification]]
            [styles.components.logo :refer [logo]]
            [styles.components.ticker :refer [ticker]]
            [styles.components.tooltip :refer [tooltip]]
            [styles.components.note-adder :refer [note-adder]]
            [styles.components.note-editor :refer [note-editor]]
            [styles.components.note-timeline :refer [note-timeline]]
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

  [:textarea
   {:outline :none
    :overflow :auto
    :-webkit-box-shadow :none
    :-moz-box-shadow :none
    :box-shadow :none
    :resize :none
    :padding (px (:xx-small c/spacing))
    :border [[:solid (px (:xxx-tiny c/filling)) (:white-three c/colour)]]
    :font-family "\"Open Sans\", sans-serif"
    :font-size (px (:medium c/font-size))
    :font-weight 400
    :line-height 1.3
    :font-variant :normal
    :text-transform :none
    :-webkit-font-smoothing :antialiased
    :-moz-osx-font-smoothing :grayscale
    :text-decoration :none}]

  ["textarea::placeholder"
   {:color (:grey-one c/colour)}]

  ;; fonts
  fonts/icomoon

  ;; components
  text
  icon
  cell
  button
  page
  notification
  logo
  ticker
  tooltip
  note-adder
  note-editor
  note-timeline
  note)
