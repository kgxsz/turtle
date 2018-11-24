(ns turtle.styles.utils
  (:require [turtle.styles.constants :as c]
            [garden.stylesheet :refer [at-media]]
            [garden.units :refer [px]]))


(defn tiny-width
  [styles]
  (at-media {:max-width (-> c/breakpoint :tiny :end px)}
            [:& styles]))


(defn small-width
  [styles]
  (at-media {:min-width (-> c/breakpoint :small :start px)
             :max-width (-> c/breakpoint :small :end px)}
            [:& styles]))


(defn medium-width
  [styles]
  (at-media {:min-width (-> c/breakpoint :medium :start px)
             :max-width (-> c/breakpoint :medium :end px)}
            [:& styles]))


(defn large-width
  [styles]
  (at-media {:min-width (-> c/breakpoint :large :start px)
             :max-width (-> c/breakpoint :large :end px)}
            [:& styles]))


(defn huge-width
  [styles]
  (at-media {:min-width (-> c/breakpoint :huge :start px)}
            [:& styles]))


(defn make-modifiers
  ([constants property]
   (make-modifiers constants property identity))
  ([constants property unit]
   (map (fn [[k v]] [(keyword (str "&-" (name k))) {property (unit v)}]) constants)))
