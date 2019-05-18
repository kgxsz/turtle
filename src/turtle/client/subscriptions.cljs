(ns client.subscriptions
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
 :authorised?
 (fn [db [_]]
   (:authorised? db)))


(re-frame/reg-sub
 :hovered-tick
 (fn [db [_]]
   (get-in db [:tick-by-id (:hovered-tick-id db)])))


(re-frame/reg-sub
 :clicked-tick
 (fn [db [_]]
   (get-in db [:tick-by-id (:clicked-tick-id db)])))


(re-frame/reg-sub
 :input-value
 (fn [db [_]]
   (:input-value db)))


(re-frame/reg-sub
 :ticks
 (fn [db [_]]
   (map #(get-in db [:tick-by-id %]) (:tick-ids db))))


(re-frame/reg-sub
 :tick
 (fn [db [_ tick-id]]
   (get-in db [:tick-by-id tick-id])))


(re-frame/reg-sub
 :notes
 (fn [db [_]]
   (let [get-note #(get-in db [:note-by-id %])
         get-tick #(get-in db [:tick-by-id %])
         join-tick #(some-> % (dissoc :tick-id) (assoc :tick (get-tick (:tick-id %))))]
     (->> (:note-ids db)
          (map get-note)
          (map join-tick)))))


(re-frame/reg-sub
 :note
 (fn [db [_ note-id]]
   (let [get-note #(get-in db [:note-by-id %])
         get-tick #(get-in db [:tick-by-id %])
         join-tick #(some-> % (dissoc :tick-id) (assoc :tick (get-tick (:tick-id %))))]
     (->> note-id
          (get-note)
          (join-tick)))))


(re-frame/reg-sub
 :hovered-note
 (fn [db [_]]
   (let [get-note #(get-in db [:note-by-id %])
         get-tick #(get-in db [:tick-by-id %])
         join-tick #(some-> % (dissoc :tick-id) (assoc :tick (get-tick (:tick-id %))))]
     (->> (:hovered-note-id db)
          (get-note)
          (join-tick)))))
