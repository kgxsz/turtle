(ns client.views.notes
  (:require [re-frame.core :as re-frame]
            [client.views.note :as note]
            [client.utils :as u]))


(defn view [{:keys [notes]}
            {:keys [note]}]
  (if (empty? notes)
    [:div
     {:class (u/bem [:notes])}
     [:div
      {:class (u/bem [:cell :column :align-center :padding-top-x-huge])}
      [:div
       {:class (u/bem [:text :font-weight-bold :font-size-xxx-large :colour-grey-one])}
       "There's nothing here yet!"]]]
    [:ul
     {:class (u/bem [:notes])}
     (doall
      (for [{:keys [note-id]} notes]
        [:li
         {:key note-id}
         [note note-id]]))]))


(defn notes []
  (let [!notes (re-frame/subscribe [:notes])]
    (fn []
      [view
       {:notes @!notes}
       {:note note/note}])))
