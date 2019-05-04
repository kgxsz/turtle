(ns styles.components.page
  (:require [styles.constants :as c]
            [styles.utils :as u]
            [garden.def :refer [defstyles]]
            [garden.units :refer [px percent ms vh vw]]))


(defstyles page
  [:.page
   {:display :none
    :overflow :auto
    :min-height (vh (:100 c/proportion))
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
     :width (vw (:100 c/proportion))
     :height (px (:xx-large c/filling))}

    [:&--hidden
     {:display :none}]]

   [:&__body
    {:padding-top (px (:xx-large c/filling))}]

   [:&__footer
    {:min-height (px (:xx-large c/filling))}]

   [:&__sections
    [:&__section
     [:&--fixed
      {:position :fixed
       :width (vw (:100 c/proportion))}]

     [:&--offset
      {:padding-top (px (+ (:height c/plot)
                           (:xxx-large c/filling)
                           (:huge c/filling)))}]]]])
