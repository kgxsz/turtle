(ns client.views.pills
  (:require [re-frame.core :as re-frame]
            [client.views.pill :as pill]
            [client.utils :as u]))


(defn view [{:keys [pill]}]
  [:div
   {:class (u/bem [:pills]
                  [:cell :column :width-cover :padding-top-x-huge])}
   [:div
    {:class (u/bem [:pills__body]
                   [:cell :row :wrap :align-center])}
    (doall
     (for [symbol [:aapl :goog :lloy.lon :gs :amzn :av.lon :t :cmcsa :kkpny :vod :bt]]
       [:div
        {:key symbol
         :class (u/bem [:cell :padding-xx-tiny])}
        [pill {:symbol symbol}]]))]])


(defn pills []
  [view
   {:pill pill/pill}])
