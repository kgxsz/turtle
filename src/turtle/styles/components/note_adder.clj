(ns styles.components.note-adder
  (:require [styles.constants :as c]
            [styles.utils :as u]
            [garden.def :refer [defstyles]]
            [garden.units :refer [px percent ms vh vw]]))


(defstyles note-adder
  [:.note-adder
   {:display :flex
    :flex-direction :column
    :align-items :center}

   [:&__add-button-container
    {:position :relative
     :width (px (:width c/plot))
     :margin-left (px (:xxx-large c/spacing))
     :margin-right (px (:x-huge c/spacing))}

    [:&--invisible
     {:display :none}]]

   [:&__add-button
    {:position :absolute
     :top (px (u/halve (- (+ (:tiny c/filling) (:x-large c/filling)))))
     :left 0
     :width (px (:width c/plot))
     :height (px (:x-large c/filling))}

    [:&__body
     {:position :absolute
      :left (px (- (:width c/plot) (:circle-radius c/plot)))
      :margin-left (px (u/halve (- (:x-large c/filling))))
      :pointer-events :none
      :background-color (:black-two c/colour)
      :width (px (:x-large c/filling))
      :height (px (:x-large c/filling))
      :border-radius (percent (:50 c/proportion))}

     [:&__cross
      {:position :absolute
       :background-color (:white-one c/colour)}

      [:&--vertical
       {:top (px (u/halve (- (:x-large c/filling) (:small c/filling))))
        :left (px (u/halve (- (:x-large c/filling) (:xx-tiny c/filling))))
        :background-color (:white-one c/colour)
        :width (px (:xx-tiny c/filling))
        :height (px (:small c/filling))}]

      [:&--horizontal
       {:top (px (u/halve (- (:x-large c/filling) (:xx-tiny c/filling))))
        :left (px (u/halve (- (:x-large c/filling) (:small c/filling))))
        :background-color (:white-one c/colour)
        :width (px (:small c/filling))
        :height (px (:xx-tiny c/filling))}]]]

    [:&__overlay
     {:position :absolute
      :top 0
      :bottom 0
      :cursor :pointer
      :z-index 2}]]

   [:&__note-editor-container
    {:width (percent (:100 c/proportion))
     :background-color (:white-two c/colour)
     :border-bottom [[:solid (px (:tiny c/filling)) (:white-three c/colour)]]}

    [:&--invisible
     {:display :none}]]])
