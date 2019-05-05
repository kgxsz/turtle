(ns styles.components.note-editor
  (:require [styles.constants :as c]
            [styles.utils :as u]
            [garden.def :refer [defstyles]]
            [garden.units :refer [px percent ms vh vw]]))


(defstyles note-editor
  [:.note-editor
   {:display :flex
    :flex-direction :column
    :justify-content :center
    :height (px (:height c/editor))
    :width (px (:width c/editor))
    :margin :auto
    :padding-left (px (:medium c/spacing))
    :padding-right (px (:medium c/spacing))}

   [:&__section
    {:display :flex
     :flex-direction :row
     :justify-content :space-between}

    [:&--align-bottom
     {:align-items :baseline}]

    [:&--align-top
     {:align-items :flex-start}]]

   [:&__label
    {:display :flex
     :flex-direction :row
     :align-items :baseline}]

   [:&__input
    {:width (percent (:100 c/proportion))
     :height (px (:xxx-large c/filling))
     :margin-top (px (:medium c/spacing))
     :margin-bottom (px (:medium c/spacing))}]

   [:&__buttons
    {:display :flex
     :flex-direction :row}

    [:&__button
     {:padding-left (px (:medium c/spacing))}]]])
