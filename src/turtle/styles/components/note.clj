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
   [:&__body
    {:height (px 300)
     :width (px 700)
     :padding (px 15)
     :background-color (:white-medium c/colour)
     :border :solid
     :border-width (-> c/filling :xxx-tiny px)
     :border-color (:grey-light c/colour)

     }
    ]]
  )
