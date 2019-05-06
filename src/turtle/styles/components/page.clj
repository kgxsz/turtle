(ns styles.components.page
  (:require [styles.constants :as c]
            [styles.utils :as u]
            [garden.def :refer [defstyles]]
            [garden.units :refer [px percent ms vh vw]]))


(defstyles page
  [:.page
   {:display :none
    :min-height (vh (:100 c/proportion))}

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
    {:width (vw (:100 c/proportion))}

    [:&--hidden
     {:display :none}]]

   [:&__footer
    {:width (vw (:100 c/proportion))}]

   [:&__section
    {:width (vw (:100 c/proportion))}
    [:&--offset
     {:padding-top (px (+ (:height c/ticker-plot)
                          (:xxx-large c/filling)
                          (:huge c/filling)))}]]])
