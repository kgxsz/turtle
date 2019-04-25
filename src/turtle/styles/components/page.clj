(ns styles.components.page
  (:require [styles.constants :as c]
            [styles.utils :as u]
            [garden.def :refer [defstyles]]
            [garden.units :refer [px percent ms vh vw]]))


(defstyles page
  [:.page
   {:display :none
    :overflow :auto
    :min-height (-> c/proportion :100 vh)
    :background-color (:white-light c/colour)}

   (u/tiny-width
    {:display :none})

   (u/small-width
    {:display :none})

   (u/medium-width
    {:display :none})

   (u/large-width
    {:display :none})

   (u/huge-width
    {:display :block})

   [:&__header
    {:position :fixed
     :background-color (:white-medium c/colour)
     :width (vw 100)
     :min-height (-> c/filling :xx-large px)}]

   [:&__body
    {:padding-top (-> c/filling :xx-large px)}]

   [:&__footer
    {:min-height (-> c/filling :xx-large px)}]

   [:&__sections
    [:&__section
     [:&--fixed
      {:position :fixed
       :width (vw 100)}]]]])
