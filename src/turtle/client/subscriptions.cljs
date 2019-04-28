(ns client.subscriptions
  (:require-macros [reagent.ratom :refer [reaction]])
  (:require [re-frame.core :as re-frame]))


(re-frame/reg-sub
 :initialising-routing?
 (fn [db [_]]
   (:initialising-routing? db)))


(re-frame/reg-sub
 :initialising-ticks?
 (fn [db [_]]
   (:initialising-ticks? db)))


(re-frame/reg-sub
 :initialising-notes?
 (fn [db [_]]
   (:initialising-notes? db)))


(re-frame/reg-sub
 :input-value
 (fn [db [_]]
   (:input-value db)))


(re-frame/reg-sub
 :focused-tick
 (fn [db [_]]
   (get-in db [:tick-by-id (:focused-tick-id db)])))


(re-frame/reg-sub
 :focused-tick-id
 (fn [db [_]]
   (:focused-tick-id db)))


(re-frame/reg-sub
 :ticks
 (fn [db [_]]
   (map #(get-in db [:tick-by-id %]) (:tick-ids db))))


(re-frame/reg-sub
 :tick
 (fn [db [_ id]]
   (get-in db [:tick-by-id id])))


(re-frame/reg-sub
 :notes
 (fn [db [_]]
   (let [get-note #(get-in db [:note-by-id %])
         get-tick #(get-in db [:tick-by-id %])
         join-tick #(-> % (dissoc :tick-id) (assoc :tick (get-tick (:tick-id %))))]
     (->> (:note-ids db)
          (map get-note)
          (map join-tick)))))


(re-frame/reg-sub
 :note-ids
 (fn [db [_]]
   (get-in db [:note-ids])))


(re-frame/reg-sub
 :note
 (fn [db [_ id]]
   (let [get-note #(get-in db [:note-by-id %])
         get-tick #(get-in db [:tick-by-id %])
         join-tick #(-> % (dissoc :tick-id) (assoc :tick (get-tick (:tick-id %))))]
     (->> id (get-note) (join-tick)))))
