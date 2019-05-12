(ns client.views.logo
  (:require [re-frame.core :as re-frame]
            [client.utils :as u]))


(defn view []
  [:div
   {:class (u/bem [:logo]
                  [:cell :width-xxx-large :height-xxx-large])}
   [:svg
    {:xmlns "http://www.w3.org/2000/svg"
     :viewBox "0 0 70 70"}
    [:g
     {:transform "translate(-50 -171)"}
     [:line
      {:class (u/bem [:logo__line])
       :y1 "27"
       :transform "translate(85 191)"}]
     [:line
      {:class (u/bem [:logo__line])
       :x2 "30"
       :transform "translate(70 187.5)"}]
     [:circle
      {:class (u/bem [:logo__circle])
       :cx "4.5"
       :cy "4.5"
       :r "4.5"
       :transform "translate(80.5 183)"}]
     [:circle
      {:class (u/bem [:logo__circle])
       :cx "4.5"
       :cy "4.5"
       :r "4.5"
       :transform "translate(62 183)"}]
     [:circle
      {:class (u/bem [:logo__circle])
       :cx "4.5"
       :cy "4.5"
       :r "4.5"
       :transform "translate(80.5 216)"}]
     [:circle
      {:class (u/bem [:logo__circle])
       :cx "4.5"
       :cy "4.5"
       :r "4.5"
       :transform "translate(99 183)"}]]]])


(defn logo []
  [view])
