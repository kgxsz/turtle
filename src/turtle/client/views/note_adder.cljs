(ns client.views.note-adder
  (:require [re-frame.core :as re-frame]
            [styles.constants :as c]
            [client.utils :as u]))


(defn view [{:keys [tick-positions focused-tick-position]}]
  [:div
   {:class (u/bem [:note-adder])}
   [:div
    {:class (u/bem [:note-adder__body])}
    (doall
     (for [{:keys [id left width]} tick-positions]
       [:div
        {:key id
         :class (u/bem [:note-adder__overlay])
         :on-click (fn [e]
                     (re-frame/dispatch [:add-note])
                     (.preventDefault e))
         :on-mouse-enter (fn [e]
                           (re-frame/dispatch [:update-focused-tick-id id])
                           (.preventDefault e))
         :on-mouse-leave (fn [e]
                           (re-frame/dispatch [:update-focused-tick-id nil])
                           (.preventDefault e))
         :style {:left left
                 :width width}}]))
    [:div
     {:class (u/bem [:note-adder__plus-button])
      :style {:left (:x focused-tick-position)}}
     [:div
      {:class (u/bem [:note-adder__plus-button__cross :vertical])}]
     [:div
      {:class (u/bem [:note-adder__plus-button__cross :horizontal])}]]]])


(defn note-adder []
  (let [!ticks (re-frame/subscribe [:ticks])
        !focused-tick-id (re-frame/subscribe [:focused-tick-id])]
    (fn []
      (let [ticks @!ticks
            focused-tick-id @!focused-tick-id
            tick-positions (u/tick-positions ticks)]
        [view
         {:tick-positions tick-positions
          :focused-tick-position (u/get-by-id focused-tick-id tick-positions)}]))))
