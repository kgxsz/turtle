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
 :note-list
 (fn [db [_]]
   (:note-list db)))


(re-frame/reg-sub
 :note
 (fn [db [_ id]]
   (get-in db [:note-by-id id])))
