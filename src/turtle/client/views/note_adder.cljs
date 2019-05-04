(ns client.views.note-adder
  (:require [re-frame.core :as re-frame]
            [client.views.button :refer [primary-button secondary-button]]
            [client.schema :as schema]
            [cljs.spec.alpha :as spec]
            [styles.constants :as c]
            [client.utils :as u]))


(defn view [{:keys [input-value tick-positions clicked-tick focused-tick]}]
  [:div
   {:class (u/bem [:note-adder (when (some? clicked-tick) :visible)])}
   [:div
    {:class (u/bem [:note-adder__add-button-container (when (some? clicked-tick) :invisible)])}
    [:div
     {:class (u/bem [:note-adder__add-button])}
     [:div
      {:class (u/bem [:note-adder__add-button__body])
       :style {:left (:x (u/find-tick-position focused-tick tick-positions))}}
      [:div
       {:class (u/bem [:note-adder__add-button__body__cross :vertical])}]
      [:div
       {:class (u/bem [:note-adder__add-button__body__cross :horizontal])}]]
     (doall
      (for [{:keys [tick-id left width]} tick-positions]
        [:div
         {:key tick-id
          :class (u/bem [:note-adder__add-button__overlay])
          :on-click #(re-frame/dispatch [:activate-note-adder tick-id])
          :on-mouse-enter #(re-frame/dispatch [:update-focused-tick tick-id])
          :on-mouse-leave #(re-frame/dispatch [:update-focused-tick])
          :style {:left left
                  :width width}}]))]]

   [:div
    {:class (u/bem [:note-adder__editor (when (some? clicked-tick) :visible)])}
    [:div
     (str clicked-tick)]
    [:textarea
     {:class (u/bem [:note-adder__input])
      :type :text
      :value input-value
      :placeholder "Write something here"
      :on-change #(re-frame/dispatch [:update-input-value (.. % -target -value)])}]
    [:div
     (- 128 (count input-value))]
    [secondary-button
     {:label "Cancel"
      :on-click #(re-frame/dispatch [:deactivate-note-adder])}]
    [primary-button
     {:label "Done"
      :disabled? (not (spec/valid? ::schema/text input-value))
      :on-click #(do (re-frame/dispatch [:add-note (:tick-id clicked-tick) input-value])
                     (re-frame/dispatch [:deactivate-note-adder]))}]]])



(defn note-adder []
  (let [!authorised? (re-frame/subscribe [:authorised?])
        !ticks (re-frame/subscribe [:ticks])
        !focused-tick (re-frame/subscribe [:focused-tick])
        !clicked-tick (re-frame/subscribe [:clicked-tick])
        !input-value (re-frame/subscribe [:input-value])]
    (fn []
      (when @!authorised?
        [view
         {:input-value @!input-value
          :tick-positions (u/tick-positions @!ticks)
          :focused-tick @!focused-tick
          :clicked-tick @!clicked-tick}]))))
