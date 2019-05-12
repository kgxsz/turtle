(ns styles.components.note-adder
  (:require [styles.constants :as c]
            [styles.utils :as u]
            [garden.def :refer [defstyles]]
            [garden.units :refer [px percent ms vh vw]]))


(defstyles note-adder
  [:.note-adder
   [:&__add-button-container
    {:width (px (:width c/ticker-plot))}
    [:&--invisible
     {:display :none}]]

   [:&__add-button
    {:top (px (u/halve (- (+ (:tiny c/filling) (:x-large c/filling)))))
     :left 0
     :width (px (:width c/ticker-plot))}

    [:&__body
     {:left (px (- (:width c/ticker-plot) (:circle-radius c/ticker-plot)))
      :margin-left (px (u/halve (- (:x-large c/filling))))
      :pointer-events :none
      :border-radius (percent (:50 c/proportion))}

     [:&__cross-vertical
      {:top (px (u/halve (- (:x-large c/filling) (:small c/filling))))
       :left (px (u/halve (- (:x-large c/filling) (:xx-tiny c/filling))))
       :width (px (:xx-tiny c/filling))
       :height (px (:small c/filling))}]

     [:&__cross-horizontal
      {:top (px (u/halve (- (:x-large c/filling) (:xx-tiny c/filling))))
       :left (px (u/halve (- (:x-large c/filling) (:small c/filling))))
       :width (px (:small c/filling))
       :height (px (:xx-tiny c/filling))}]]

    [:&__overlay
     {:top 0
      :bottom 0
      :cursor :pointer
      :z-index 2}]]

   [:&__note-editor-container
    {:border-bottom [[:solid (px (:tiny c/filling)) (:white-three c/colour)]]}

    [:&--invisible
     {:display :none}]]])
