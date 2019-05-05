(ns client.views.note-adder
  (:require [re-frame.core :as re-frame]
            [client.views.note-editor :as note-editor]
            [client.schema :as schema]
            [cljs.spec.alpha :as spec]
            [styles.constants :as c]
            [client.utils :as u]))


(defn view [{:keys [authorised? active? left overlays tick-id]}
            {:keys [note-editor]}
            {:keys [on-click on-mouse-enter on-mouse-leave]}]
  (when authorised?
    [:div
     {:class (u/bem [:note-adder])}
     [:div
      {:class (u/bem [:note-adder__add-button-container (when active? :invisible)])}
      [:div
       {:class (u/bem [:note-adder__add-button])}
       [:div
        {:class (u/bem [:note-adder__add-button__body])
         :style {:left left}}
        [:div
         {:class (u/bem [:note-adder__add-button__body__cross :vertical])}]
        [:div
         {:class (u/bem [:note-adder__add-button__body__cross :horizontal])}]]

       (doall
        (for [{:keys [tick-id left width]} overlays]
          [:div
           {:key tick-id
            :class (u/bem [:note-adder__add-button__overlay])
            :on-click (partial on-click tick-id)
            :on-mouse-enter (partial on-mouse-enter tick-id)
            :on-mouse-leave on-mouse-leave
            :style {:left left
                    :width width}}]))]]

     [:div
      {:key tick-id
       :class (u/bem [:note-adder__note-editor-container (when-not active? :invisible)])}
      [note-editor tick-id]]]))


(defn note-adder []
  (let [!authorised? (re-frame/subscribe [:authorised?])
        !ticks (re-frame/subscribe [:ticks])
        !hovered-tick (re-frame/subscribe [:hovered-tick])
        !clicked-tick (re-frame/subscribe [:clicked-tick])]
    (fn []
      (let [authorised? @!authorised?
            ticks @!ticks
            {:keys [tick-id]} @!clicked-tick
            {:keys [x]} (u/tick-position (:tick-id @!hovered-tick) ticks)]
        [view
         {:authorised? authorised?
          :active? (and authorised? (some? tick-id))
          :left x
          :overlays (for [tick-position (u/tick-positions ticks)]
                      (select-keys tick-position [:tick-id :left :width]))
          :tick-id tick-id}
         {:note-editor note-editor/note-editor}
         {:on-click #(re-frame/dispatch [:activate-note-adder %])
          :on-mouse-enter #(re-frame/dispatch [:update-hovered-tick %])
          :on-mouse-leave #(re-frame/dispatch [:update-hovered-tick])}]))))
