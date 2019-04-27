(ns client.views.notification
  (:require [client.utils :as u]))


(defn view
  [{:keys [type paragraph]}]
  (let [icon (case type
               :success :checkmark-circle
               :warning :warning
               :error :warning)
        title (clojure.string/upper-case (name type))]
    [:div
     {:class (u/bem [:notification type])}
     [:div
      {:class (u/bem [:notification__title])}
      [:div
       {:class (u/bem [:icon icon :font-size-xxx-large])}]
      [:div
       {:class (u/bem [:text :font-size-x-large :font-weight-bold :padding-left-xxx-small])}
       title]]
     [:div
      {:class (u/bem [:notification__paragraph])}
      [:div
       {:class (u/bem [:text :align-center])}
       paragraph]]]))


(defn browser-window-error []
  [view
   {:type :error
    :paragraph "This application requires a larger browser window."}])
