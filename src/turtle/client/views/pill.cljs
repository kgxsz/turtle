(ns client.views.pill
  (:require [re-frame.core :as re-frame]
            [client.utils :as u]))


(defn view [{:keys [symbol clickable?]}
            {:keys [on-click]}]
  [:div
   {:class (u/bem [:pill (when clickable? :clickable)]
                  [:cell :row :align-center :height-medium :padding-medium :colour-black-two])
    :on-click (when clickable? on-click)}
   [:div
    {:class (u/bem [:text :font-size-medium :font-weight-bold :colour-white-two])}
    (u/format-symbol symbol)]])


(defn pill [{:keys [symbol] :as properties}]
  (let [!symbol (re-frame/subscribe [:symbol])]
    (fn []
      [view
       {:symbol symbol
        :clickable? (not= symbol @!symbol)}
       {:on-click #(re-frame/dispatch [:route-to-ticker symbol])}])))
