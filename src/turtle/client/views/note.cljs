(ns client.views.note
  (:require [re-frame.core :as re-frame]
            [client.utils :as u]))


(defn view [{:keys [note]}]
  [:div
   {:class (u/bem [:note])}
   [:div
    {:class (u/bem [:note__body])}
    (str note)
    [:div
     {:class (u/bem [:text :font-size-large :colour-grey-dark])}
     (:text note)]]])


(defn note [note-id]
  (let [!note (re-frame/subscribe [:note note-id])]
    (fn []
      [view
       {:note @!note}])))
