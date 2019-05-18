(ns client.views.notes
  (:require [re-frame.core :as re-frame]
            [client.views.note :refer [note]]
            [client.utils :as u]))


(defn view [{:keys [authorised? notes]}]
  (if (empty? notes)
    [:div
     {:class (u/bem [:notes])}
     [:div
      {:class (u/bem [:cell :column :align-center :padding-top-x-huge])}
      [:div
       {:class (u/bem [:text :font-weight-bold :font-size-xxx-large :colour-grey-one])}
       "There's nothing here yet!"]
      (when authorised?
        [:div
         {:class (u/bem [:text :padding-top-large :font-size-medium :colour-grey-one])}
         "You can add a note by clicking on the big plus button"])]]
    [:ul
     {:class (u/bem [:notes])}
     (doall
      (for [{:keys [note-id]} notes]
        [:li
         {:key note-id}
         [note note-id]]))]))


(defn notes []
  (let [!authorised? (re-frame/subscribe [:authorised?])
        !notes (re-frame/subscribe [:notes])]
    (fn []
      [view
       {:authorised? @!authorised?
        :notes @!notes}])))
