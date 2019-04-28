(ns client.views.tooltip
  (:require [re-frame.core :as re-frame]
            [cljs-time.core :as t]
            [cljs-time.format :as t.format]
            [cljs-time.coerce :as t.coerce]
            [reagent.format :as format]
            [client.utils :as u]))


;; TODO - get this to utils
(def label-formatter (t.format/formatter "MMM do"))


(defn view [{:keys [tooltip]}]
  [:div
   {:class (u/bem [:tooltip])}
   [:div
    {:class (u/bem [:tooltip__locus])}]
   [:div
    {:class (u/bem [:tooltip__pointer])}]
   [:div
    {:class (u/bem [:tooltip__backing])}]
   [:div
    {:class (u/bem [:tooltip__instant-label])}
    [:div
     {:class (u/bem [:text :font-size-xx-small :font-weight-bold :colour-white-light :align-center])}
     (:instant-label tooltip)]]
   [:div
    {:class (u/bem [:tooltip__close-label])}
    [:div
     {:class (u/bem [:text :font-size-xx-tiny :font-weight-bold :colour-white-light])}
     "USD"]
    [:div
     {:class (u/bem [:text :font-size-medium :font-weight-bold :colour-white-light :padding-left-xx-tiny])}
     (:close-label tooltip)]]])


(defn tooltip []
  (let [!focused-tick (re-frame/subscribe [:focused-tick])]
    (fn []
      (let [focused-tick @!focused-tick]
        [view
         ;; TODO - get this to utils
         {:tooltip {:close-label (format/format "%.1f" (:close focused-tick))
                    :instant-label (t.format/unparse label-formatter (t.coerce/from-long (:instant focused-tick)))}}]))))
