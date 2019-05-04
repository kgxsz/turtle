(ns client.views.notes
  (:require [re-frame.core :as re-frame]
            [client.views.note :refer [note]]
            [client.utils :as u]))


(defn view [{:keys [notes]}]
  [:ul
   (doall
    (for [{:keys [note-id]} notes]
      [:li
       {:key note-id}
       [note note-id]]))])


(defn notes []
  (let [!notes (re-frame/subscribe [:notes])]
    (fn []
      [view
       {:notes @!notes}])))
