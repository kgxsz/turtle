(ns styles.components.notification
  (:require [styles.constants :as c]
            [styles.utils :as u]
            [garden.def :refer [defstyles]]
            [garden.units :refer [px percent ms vh vw]]))

(defstyles notification
  [:.notification
   {:display :flex
    :flex-direction :column
    :align-items :center
    :justify-content :center
    :position :fixed
    :left 0
    :right 0
    :top 0
    :z-index -1
    :height (-> c/filling :huge px)
    :border-bottom :solid
    :border-width (-> c/filling :xx-tiny px)
    :padding-left (-> c/spacing :small px)
    :padding-right (-> c/spacing :small px)}

   [:&--success
    {:background-color (:green-light c/colour)
     :border-color (:green-dark c/colour)
     :color (:green-dark c/colour)}]

   [:&--warning
    {:background-color (:yellow-light c/colour)
     :border-color (:yellow-dark c/colour)
     :color (:yellow-dark c/colour)}]

   [:&--error
    {:background-color (:red-light c/colour)
     :border-color (:red-dark c/colour)
     :color (:red-dark c/colour)}]

   [:&__title
    {:display :flex
     :flex-direction :row
     :align-items :baseline}]

   [:&__paragraph
    {:margin-top (-> c/spacing :tiny px)}]])
