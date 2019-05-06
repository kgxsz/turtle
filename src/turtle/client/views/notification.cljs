(ns client.views.notification
  (:require [re-frame.core :as re-frame]
            [client.utils :as u]))


(defn view [{:keys [type icon title paragraph]}]
  [:div
   {:class (u/bem [:notification type]
                  [:cell :fixed :column :height-huge :padding-left-small :padding-right-small])}
   [:div
    {:class (u/bem [:cell :row :align-baseline])}
    [:div
     {:class (u/bem [:icon icon :font-size-xxx-large])}]
    [:div
     {:class (u/bem [:text :font-size-x-large :font-weight-bold :padding-left-xxx-small])}
     title]]
   [:div
    {:class (u/bem [:cell :margin-top-tiny])}
    [:div
     {:class (u/bem [:text :align-center])}
     paragraph]]])


(defn notification [{:keys [type] :as properties}]
  [view
   (assoc properties
          :icon (case type
                  :success :checkmark-circle
                  :warning :warning
                  :error :warning)
          :title (clojure.string/upper-case (name type)))])


(defn error-notification []
  [notification
   {:type :error
    :paragraph "This application requires a larger browser window."}])
