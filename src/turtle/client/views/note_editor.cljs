(ns client.views.note-editor
  (:require [re-frame.core :as re-frame]
            [client.views.button :as button]
            [client.schema :as schema]
            [cljs.spec.alpha :as spec]
            [styles.constants :as c]
            [client.utils :as u]))


(defn view [{:keys [instant close input-value character-count]}
            {:keys [primary-button secondary-button]}
            {:keys [on-change on-primary-click on-secondary-click]}]
  [:div
   {:class (u/bem [:note-editor]
                  [:cell :padding-top-x-large :padding-left-medium :padding-right-medium])}
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

   [:textarea
    {:class (u/bem [:note-editor__input]
                   [:cell :width-cover :height-xxx-large :margin-top-medium :margin-bottom-medium])
     :type :text
     :value input-value
     :placeholder "Write something here"
     :on-change on-change}]

   [:div
    {:class (u/bem [:cell :row :justify-space-between :align-start])}
    [:div
     {:class (u/bem [:text :font-size-medium :colour-black-four])}
     (str character-count " characters left")]
    [:div
     {:class (u/bem [:cell :row])}
     [:div
      {:class (u/bem [:cell])}
      [secondary-button
       {:label "Cancel"}
       {:on-click on-secondary-click}]]
     [:div
      {:class (u/bem [:cell :padding-left-medium])}
      [primary-button
       {:label "Done"
        :disabled? (not (spec/valid? ::schema/text input-value))}
       {:on-click on-primary-click}]]]]])


(defn note-editor [tick-id]
  (let [!tick (re-frame/subscribe [:tick tick-id])
        !input-value (re-frame/subscribe [:input-value])]
    (fn []
      (let [input-value @!input-value]
        [view
         (-> @!tick
             (select-keys [:tick-id :instant :close])
             (assoc :input-value input-value
                    :character-count (- 128 (count input-value))))
         {:primary-button button/primary-button
          :secondary-button button/secondary-button}
         ;; TODO, this doesn't feel right, they should chain up
         {:on-change #(re-frame/dispatch [:update-input-value (.. % -target -value)])
          :on-primary-click #(do (re-frame/dispatch [:add-note tick-id input-value])
                                 (re-frame/dispatch [:deactivate-note-adder]))
          :on-secondary-click #(re-frame/dispatch [:deactivate-note-adder])}]))))
