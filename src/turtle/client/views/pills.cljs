(ns client.views.pills
  (:require [re-frame.core :as re-frame]
            [client.views.pill :as pill]
            [client.utils :as u]))


(defn view [{:keys [pill]}]
  [:div
   {:class (u/bem [:pills])}
   [:div
    {:class (u/bem [:cell :row :align-center :padding-bottom-small :colour-white-two])}
    (doall
     (for [symbol [:aapl :goog :blah :blob :foo :guu]]
       [:div
        {:key symbol
         :class (u/bem [:cell :padding-tiny])}
        [pill {:symbol symbol}]]))]])


(defn pills []
  [view
   {:pill pill/pill}])
