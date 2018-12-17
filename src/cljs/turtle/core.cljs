(ns turtle.core
  (:require [reagent.core :as reagent]
            [re-frame.core :as re-frame]
            [turtle.effects :as effects]
            [turtle.events :as events]
            [turtle.subscriptions :as subscriptions]
            [turtle.views :as views]))


(defn mount-root []
  (re-frame/clear-subscription-cache!)
  (reagent/render [views/app]
                  (.getElementById js/document "root")))


(defn ^:export initialise []
  (enable-console-print!)
  (re-frame/dispatch-sync [:initialise-db])
  (re-frame/dispatch-sync [:initialise-ticker])
  (re-frame/dispatch-sync [:initialise-notes])
  (mount-root))
