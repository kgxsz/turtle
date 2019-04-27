(ns client.views.tooltip
  (:require [re-frame.core :as re-frame]
            [cljs-time.core :as t]
            [cljs-time.format :as t.format]
            [cljs-time.coerce :as t.coerce]
            [reagent.format :as format]
            [client.utils :as u]))


;; TODO - get this to utils
(def label-formatter (t.format/formatter "MMM do"))


(defn view [{:keys [instant close]}]
  [:div
   {:class (u/bem [:ticker__tooltip])}
   [:div
    {:class (u/bem [:ticker__tooltip__locus])}]
   [:div
    {:class (u/bem [:ticker__tooltip__pointer])}]
   [:div
    {:class (u/bem [:ticker__tooltip__backing])}]
   [:div
    {:class (u/bem [:ticker__tooltip__date])}
    [:div
     {:class (u/bem [:text :font-size-xx-small :font-weight-bold :colour-white-light :align-center])}
     ;; TODO - get this to utils
     (t.format/unparse label-formatter (t.coerce/from-long instant))]]
   [:div
    {:class (u/bem [:ticker__tooltip__close])}
    [:div
     {:class (u/bem [:text :font-size-xx-tiny :font-weight-bold :colour-white-light])}
     "USD"]
    [:div
     {:class (u/bem [:text :font-size-medium :font-weight-bold :colour-white-light :padding-left-xx-tiny])}
     ;; TODO - get this to utils
     (format/format "%.1f" close)]]])


(defn standard []
  (let [!tooltip (re-frame/subscribe [:tooltip])]
    (fn []
      [view @!tooltip])))
