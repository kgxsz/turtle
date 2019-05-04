(ns styles.components.note
  (:require [styles.constants :as c]
            [styles.utils :as u]
            [garden.def :refer [defstyles]]
            [garden.units :refer [px percent ms vh vw]]))


(defstyles note
  ;; TODO extract values
  [:.note
   {:padding-top (px 50)
    :padding-bottom (px 20)
    :display :flex
    :flex-direction :column
    :align-items :center}
   ;; TODO extract values
   [:&__body
    {:height (px 300)
     :width (px 700)
     :padding (px 15)
     :background-color (:white-two c/colour)
     :border :solid
     :border-width (px (:xxx-tiny c/filling))
     :border-color (:white-three c/colour)}]])
