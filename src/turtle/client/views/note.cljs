(ns client.views.note
  (:require [re-frame.core :as re-frame]
            [client.utils :as u]))


(defn view [{:keys [text]}]
  [:div
   {:class (u/bem [:note])}
   [:div
    {:class (u/bem [:note__body])}
    [:div
     {:class (u/bem [:text :font-size-large :colour-grey-dark])}
     text]]])


(defn note [id]
  (let [!note (re-frame/subscribe [:note id])]
    (fn []
      [view @!note])))
