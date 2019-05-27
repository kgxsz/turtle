(ns client.views.note
  (:require [re-frame.core :as re-frame]
            [client.utils :as u]))


(defn view [{:keys [note-id instant close text symbol authorised? focused?]}
            {:keys []}
            {:keys [delete on-mouse-over on-mouse-out route-to-ticker]}]
  [:div
   {:class (u/bem [:note]
                  [:cell :column :padding-top-large :padding-bottom-small])
    :on-mouse-over on-mouse-over
    :on-mouse-out on-mouse-out}
   [:div
    {:class (u/bem [:note__body (when focused? :focused)]
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
     {:class (u/bem [:cell :width-cover :height-xxx-large :margin-top-medium :margin-bottom-medium])}
     [:div
      {:class (u/bem [:text :font-size-large :colour-grey-one])}
      text]]

    [:div
     {:class (u/bem [:cell :row :justify-space-between :align-center])}
     [:div
      {:class (u/bem [:note__symbol]
                     [:cell :row :align-center :height-medium :padding-medium :colour-black-two])
       :on-click route-to-ticker}
      [:div
       {:class (u/bem [:text :font-size-medium :font-weight-bold :colour-white-two])}
       (u/format-symbol symbol)]]
     (when authorised?
       [:div
        {:class (u/bem [:note__delete]
                       [:cell :row :align-center :padding-tiny])
         :on-click delete}
        [:div
         {:class (u/bem [:icon :trash :font-size-huge :colour-grey-one])}]])]]])


(defn note [note-id]
  (let [!note (re-frame/subscribe [:note note-id])
        !authorised? (re-frame/subscribe [:authorised?])
        !hovered-note (re-frame/subscribe [:hovered-note])]
    (fn []
      (let [{:keys [tick] :as note} @!note]
        [view
         (merge
          (select-keys note [:note-id :text])
          (select-keys tick [:instant :close :symbol])
          {:authorised? @!authorised?
           :focused? (= @!hovered-note note)})
         {}
         {:delete #(re-frame/dispatch [:delete-note note-id])
          :route-to-ticker #(re-frame/dispatch [:route-to-ticker (:symbol tick)])
          :on-mouse-over #(re-frame/dispatch [:update-hovered-note note-id])
          :on-mouse-out #(re-frame/dispatch [:update-hovered-note])}]))))
