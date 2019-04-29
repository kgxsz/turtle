(ns client.views.app
  (:require [re-frame.core :as re-frame]
            [client.utils :as u]
            [client.views.notification :refer [error-notification]]
            [client.views.logo :refer [logo]]
            [client.views.ticker :refer [ticker]]
            [client.views.note-adder :refer [note-adder]]
            [client.views.note-timeline :refer [note-timeline]]
            [client.views.notes :refer [notes]]
            [styles.constants :as c]))


(defn view [{:keys [initialising?]}]
  [:div
   {:class (u/bem [:app])}
   [error-notification]
   [:div
    {:class (u/bem [:page])}
    [:div
     {:class (u/bem [:page__header (when initialising? :hidden)])}]
    [:div
     {:class (u/bem [:page__body])}
     (if initialising?
       [:div
        {:class (u/bem [:page__sections])}
        [:div
         {:class (u/bem [:page__sections__section])}
         [logo]]]
       [:div
        {:class (u/bem [:page__sections])}
        [:div
         {:class (u/bem [:page__sections__section :fixed])}
         [ticker]
         [note-timeline]
         [note-adder]]
        [:div
         {:class (u/bem [:page__sections__section])}
         [notes]]])]
    [:div
     {:class (u/bem [:page__footer])}]]])


(defn app []
  (let [!initialising-routing? (re-frame/subscribe [:initialising-routing?])
        !initialising-ticks? (re-frame/subscribe [:initialising-ticks?])
        !initialising-notes? (re-frame/subscribe [:initialising-notes?])]
    (fn []
      (let [initialising-routing? @!initialising-routing?
            initialising-ticks? @!initialising-ticks?
            initialising-notes? @!initialising-notes?]
        [view
         {:initialising? (or initialising-routing?
                             initialising-ticks?
                             initialising-notes?)}]))))
