(ns turtle.subscriptions
  (:require-macros [reagent.ratom :refer [reaction]])
  (:require [re-frame.core :as re-frame]))


(re-frame/reg-sub
 :initialising?
 (fn [db [_]]
   (:initialising? db)))


(re-frame/reg-sub
 :input-value
 (fn [db [_]]
   (:input-value db)))


(re-frame/reg-sub
 :notes
 (fn [db [_]]
   (:notes db)))


(re-frame/reg-sub
 :note
 (fn [db [_ id]]
   (get-in db [:notes-by-id id])))
