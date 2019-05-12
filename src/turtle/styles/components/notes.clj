(ns styles.components.notes
  (:require [styles.constants :as c]
            [styles.utils :as u]
            [garden.def :refer [defstyles]]
            [garden.units :refer [px percent ms vh vw]]))


(defstyles notes
  [:.notes
   {:padding-top (px (+ (:height c/ticker-plot)
                        (:xx-large c/filling)
                        (:huge c/filling)))}])
