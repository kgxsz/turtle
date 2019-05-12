(ns client.views.app
  (:require [re-frame.core :as re-frame]
            [client.utils :as u]
            [client.views.notification :as notification]
            [client.views.logo :as logo]
            [client.views.ticker :as ticker]
            [client.views.note-adder :as note-adder]
            [client.views.note-timeline :as note-timeline]
            [client.views.notes :as notes]
            [styles.constants :as c]))


(defn view [{:keys [initialising?]}
            {:keys [error-notification logo ticker note-timeline note-adder notes]}]
  [:div
   {:class (u/bem [:app])}
   [error-notification]
   [:div
    {:class (u/bem [:page]
                   [:cell :overflow-auto :colour-white-one])}

    [:div
     {:class (u/bem [:cell :fixed :width-cover :height-xx-large :colour-white-two (when initialising? :hidden)])}]

    [:div
     {:class (u/bem [:cell :padding-top-xx-large])}
     (if initialising?
       [:div
        [:div
         {:class (u/bem [:cell :column :padding-top-xx-large])}
         [logo]]]
       [:div
        [:div
         {:class (u/bem [:cell :fixed :width-cover])}
         [ticker]
         [note-timeline]
         [note-adder]]
        [:div
         {:class (u/bem [:cell :width-cover])}
         [notes]]])]

    [:div
     {:class (u/bem [:cell :width-cover :height-xx-large])}]]])


(defn app []
  (let [!initialising-routing? (re-frame/subscribe [:initialising-routing?])
        !initialising-ticks? (re-frame/subscribe [:initialising-ticks?])
        !initialising-notes? (re-frame/subscribe [:initialising-notes?])]
    (fn []
      [view
       {:initialising? (or @!initialising-routing?
                           @!initialising-ticks?
                           @!initialising-notes?)}
       {:error-notification notification/error-notification
        :logo logo/logo
        :ticker ticker/ticker
        :note-timeline note-timeline/note-timeline
        :note-adder note-adder/note-adder
        :notes notes/notes}])))
