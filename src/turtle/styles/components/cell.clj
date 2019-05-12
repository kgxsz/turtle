(ns styles.components.cell
  (:require [styles.constants :as c]
            [styles.utils :as u]
            [garden.def :refer [defstyles]]
            [garden.units :refer [px percent ms vh vw]]))


(defstyles cell
  [:.cell
   [:&--fixed
    {:display :block
     :position :fixed}]
   [:&--absolute
    {:display :block
     :position :absolute}]
   [:&--relative
    {:display :block
     :position :relative}]

   [:&--overflow-auto
    {:overflow :auto}]

   [:&--row
    {:display :flex
     :flex-direction :row
     :justify-content :center
     :align-items :center}]

   [:&--column
    {:display :flex
     :flex-direction :column
     :justify-content :center
     :align-items :center}]

   [:&--align
    [:&-baseline
     {:align-items :baseline}]
    [:&-start
     {:align-items :flex-start}]
    [:&-end
     {:align-items :flex-end}]]

   [:&--justify
    [:&-start
     {:justify-content :flex-start}]
    [:&-space-between
     {:justify-content :space-between}]
    [:&-space-around
     {:justify-content :space-around}]]

   [:&--width
    (u/make-modifiers c/filling :width px)
    [:&-cover
     {:width (percent (:100 c/proportion))}]]

   [:&--height
    (u/make-modifiers c/filling :height px)
    [:&-cover
     {:height (percent (:100 c/proportion))}]]

   [:&--padding
    (u/make-modifiers c/spacing :padding px)]

   [:&--padding-top
    (u/make-modifiers c/spacing :padding-top px)]

   [:&--padding-bottom
    (u/make-modifiers c/spacing :padding-bottom px)]

   [:&--padding-left
    (u/make-modifiers c/spacing :padding-left px)]

   [:&--padding-right
    (u/make-modifiers c/spacing :padding-right px)]

   [:&--margin
    (u/make-modifiers c/spacing :margin px)]

   [:&--margin-top
    (u/make-modifiers c/spacing :margin-top px)]

   [:&--margin-bottom
    (u/make-modifiers c/spacing :margin-bottom px)]

   [:&--margin-left
    (u/make-modifiers c/spacing :margin-left px)]

   [:&--margin-right
    (u/make-modifiers c/spacing :margin-right px)]

   [:&--colour
    (u/make-modifiers c/colour :background-color)]

   [:&--opacity
    (u/make-modifiers c/fraction :opacity)]

   [:&--hidden
    {:display :none}]

   ;; row
   ;; columns
   ;; alignment
   ;; overlays
   ;; underlays
   ;; z index
   ;; fixed
   ;; hidden
   ;; overflow
   ;; position
   ;; opacity

   ])
