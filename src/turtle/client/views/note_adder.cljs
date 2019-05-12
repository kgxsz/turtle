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
     {:class (u/bem [:note-adder]
                    [:cell :column])}
     [:div
      {:class (u/bem [:note-adder__add-button-container (when active? :invisible)]
                     [:cell :relative :margin-left-xxx-large :margin-right-x-huge])}
      [:div
       {:class (u/bem [:note-adder__add-button]
                      [:cell :absolute :height-x-large])}
       [:div
        {:class (u/bem [:note-adder__add-button__body]
                       [:cell :absolute :width-x-large :height-x-large :colour-black-two])
         :style {:left left}}
        [:div
         {:class (u/bem [:note-adder__add-button__body__cross-vertical]
                        [:cell :absolute :colour-white-one])}]
        [:div
         {:class (u/bem [:note-adder__add-button__body__cross-horizontal]
                        [:cell :absolute :colour-white-one])}]]

       (doall
        (for [{:keys [tick-id left width]} overlays]
          [:div
           {:key tick-id
            :class (u/bem [:note-adder__add-button__overlay]
                          [:cell :absolute])
            :on-click (partial on-click tick-id)
            :on-mouse-enter (partial on-mouse-enter tick-id)
            :on-mouse-leave on-mouse-leave
            :style {:left left
                    :width width}}]))]]

     [:div
      {:key tick-id
       :class (u/bem [:note-adder__note-editor-container (when-not active? :invisible)]
                     [:cell :column :width-cover :colour-white-two])}
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
