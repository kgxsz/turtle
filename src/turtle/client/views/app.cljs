(ns client.views.app
  (:require [re-frame.core :as re-frame]
            [client.utils :as u]
            [client.views.notification :as notification]
            [client.views.logo :as logo]
            [client.views.ticker :as ticker]
            [client.views.note-adder :as note-adder]
            [client.views.note-timeline :as note-timeline]
            [client.views.pills :as pills]
            [client.views.notes :as notes]
            [styles.constants :as c]))


(defn view [{:keys [initialising? route]}
            {:keys [error-notification logo ticker note-timeline note-adder pills notes]}]
  [:div
   {:class (u/bem [:app])}
   [error-notification]

   [:div
    {:class (u/bem [:page]
                   [:cell :overflow-auto :colour-white-one])}

    [:div
     {:class (u/bem [:cell :fixed :width-cover :height-xx-large :colour-white-two (when initialising? :hidden)])}]

    (if initialising?
      [:div
       {:class (u/bem [:cell :column :padding-huge])}
       [logo]]
      (case route
        :home [:div
               {:class (u/bem [:cell :width-cover :padding-top-xxx-large])}
               [:div
                {:class (u/bem [:cell :fixed :width-cover])}
                [pills]]
               [:div
                {:class (u/bem [:cell :width-cover :padding-top-huge])}
                [notes]]]
        :ticker [:div
                 {:class (u/bem [:cell :padding-top-xxx-large])}
                 [:div
                  {:class (u/bem [:cell :fixed :width-cover])}
                  [ticker]
                  [note-timeline]
                  [note-adder]]
                 [:div
                  {:class (u/bem [:cell :width-cover :padding-top-xxx-huge])}
                  [notes]]]
        ;; TODO - make this a better message
        :unknown [:div "unknown"]))

    [:div
     {:class (u/bem [:cell :width-cover :height-xx-large])}]]])


(defn app []
  (let [!initialising-routing? (re-frame/subscribe [:initialising-routing?])
        !fetching-ticks? (re-frame/subscribe [:fetching-ticks?])
        !fetching-notes? (re-frame/subscribe [:fetching-notes?])
        !route (re-frame/subscribe [:route])]
    (fn []
      (let [route @!route]
        [view
         {:initialising? (or (nil? route) @!fetching-ticks? @!fetching-notes?)
          :route route}
         {:error-notification notification/error-notification
          :logo logo/logo
          :ticker ticker/ticker
          :note-timeline note-timeline/note-timeline
          :note-adder note-adder/note-adder
          :pills pills/pills
          :notes notes/notes}]))))
