(ns styles.components.note-editor
  (:require [styles.constants :as c]
            [styles.utils :as u]
            [garden.def :refer [defstyles]]
            [garden.units :refer [px percent ms vh vw]]))


(defstyles note-editor
  [:.note-editor
   {:height (px (:height c/editor))
    :width (px (:width c/editor))}])
