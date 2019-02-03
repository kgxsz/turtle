(ns client.core
  (:require [reagent.core :as reagent]
            [re-frame.core :as re-frame]
            [client.effects :as effects]
            [client.events :as events]
            [client.subscriptions :as subscriptions]
            [client.views :as views]))


(defn mount-root []
  (re-frame/clear-subscription-cache!)
  (reagent/render [views/app]
                  (.getElementById js/document "root")))


(defn ^:export initialise []
  (enable-console-print!)
  (re-frame/dispatch-sync [:initialise-db])
  (re-frame/dispatch-sync [:initialise-ticks])
;  (re-frame/dispatch-sync [:initialise-notes])
  (mount-root))
