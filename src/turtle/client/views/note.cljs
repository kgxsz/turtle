(ns client.views.note
  (:require [re-frame.core :as re-frame]
            [client.utils :as u]))


(defn view [{:keys [note-id instant close text symbol]}]
  [:div
   {:class (u/bem [:note]
                  [:cell :column :padding-top-large :padding-bottom-small])}
   [:div
    {:class (u/bem [:note__body]
                   [:cell :padding-medium :colour-white-two])}
    [:div
     {:class (u/bem [:cell :row :justify-space-between :align-baseline])}
     [:div
      {:class (u/bem [:cell :row :align-baseline])}
      [:div
       {:class (u/bem [:text :font-size-large :font-weight-bold :colour-black-two])}
       (u/format-regular-time instant)]]
     [:div
      {:class (u/bem [:cell :row :align-baseline])}
      [:div
       {:class (u/bem [:text :font-size-large :font-weight-bold :colour-black-two])}
       "USD"]
      [:div
       {:class (u/bem [:text :font-size-huge :font-weight-bold :colour-black-two :padding-left-xx-tiny])}
       (u/format-price close)]]]

    [:div
     {:class (u/bem [:cell :width-cover :margin-top-medium :margin-bottom-medium])}
     [:div
      {:class (u/bem [:text :font-size-large :colour-grey-one])}
      text]]

    [:div
     {:class (u/bem [:cell :row :justify-space-between :align-baseline])}
     [:div
      {:class (u/bem [:cell :row :align-baseline])}
      [:div
       {:class (u/bem [:text :font-size-large :font-weight-bold :colour-black-two])}
       symbol]]
     [:div
      {:class (u/bem [:cell :row :align-baseline])}
      [:div
       {:class (u/bem [:text :font-size-large :font-weight-bold :colour-black-two])}
       "rubbish"]]]]])


(defn note [note-id]
  (let [!note (re-frame/subscribe [:note note-id])]
    (fn []
      (let [{:keys [tick] :as note} @!note]
        (js/console.warn note)
        [view
         (merge
          (select-keys note [:note-id :text])
          (select-keys tick [:instant :close :symbol]))]))))
