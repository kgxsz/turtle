(ns client.views.note
  (:require [re-frame.core :as re-frame]
            [client.utils :as u]))


(defn view [{:keys [note]}]
  [:div
   {:class (u/bem [:note]
                  [:cell :column :padding-top-xxx-large :padding-bottom-large])}
   [:div
    {:class (u/bem [:note__body]
                   [:cell :padding-medium :colour-white-two])}
    (str note)
    [:div
     {:class (u/bem [:text :font-size-large :colour-black-three])}
     (:text note)]]])


(defn note [note-id]
  (let [!note (re-frame/subscribe [:note note-id])]
    (fn []
      [view
       {:note @!note}])))
