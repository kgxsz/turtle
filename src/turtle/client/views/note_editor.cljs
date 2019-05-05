(ns client.views.note-editor
  (:require [re-frame.core :as re-frame]
            [client.views.button :refer [primary-button secondary-button]]
            [client.schema :as schema]
            [cljs.spec.alpha :as spec]
            [styles.constants :as c]
            [client.utils :as u]))


(defn view [{:keys [tick-id instant close input-value character-count]}]
  [:div
   {:class (u/bem [:note-editor])}
   [:div
    {:class (u/bem [:note-editor__section :align-bottom])}
    [:div
     {:class (u/bem [:note-editor__label])}
     [:div
      {:class (u/bem [:text :font-size-large :font-weight-bold :colour-black-two])}
      (u/format-regular-time instant)]]
    [:div
     {:class (u/bem [:note-editor__label])}
     [:div
      {:class (u/bem [:text :font-size-large :font-weight-bold :colour-black-two])}
      "USD"]
     [:div
      {:class (u/bem [:text :font-size-huge :font-weight-bold :colour-black-two :padding-left-xx-tiny])}
      (u/format-price close)]]]

   [:textarea
    {:class (u/bem [:note-editor__input])
     :type :text
     :value input-value
     :placeholder "Write something here"
     :on-change #(re-frame/dispatch [:update-input-value (.. % -target -value)])}]

   [:div
    {:class (u/bem [:note-editor__section :align-top])}
    [:div
     {:class (u/bem [:text :font-size-medium :colour-black-four])}
     (str character-count " characters left")]
    [:div
     {:class (u/bem [:note-editor__buttons])}
     [:div
      {:class (u/bem [:note-editor__buttons__button])}
      [secondary-button
       {:label "Cancel"
        :on-click #(re-frame/dispatch [:deactivate-note-adder])}]]
     [:div
      {:class (u/bem [:note-editor__buttons__button])}
      [primary-button
       {:label "Done"
        :disabled? (not (spec/valid? ::schema/text input-value))
        ;; TODO, this doesn't feel right, they should chain up
        :on-click #(do (re-frame/dispatch [:add-note tick-id input-value])
                       (re-frame/dispatch [:deactivate-note-adder]))}]]]]])


(defn note-editor [tick-id]
  (let [!tick (re-frame/subscribe [:tick tick-id])
        !input-value (re-frame/subscribe [:input-value])]
    (fn []
      (let [input-value @!input-value]
        [view
         (merge
          (select-keys @!tick [:tick-id :instant :close])
          {:input-value input-value
           :character-count (- 128 (count input-value))})]))))
