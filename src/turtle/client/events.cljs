(ns client.events
  (:require [ajax.core :as ajax]
            [re-frame.core :as re-frame]
            [client.interceptors :as interceptors]
            [medley.core :as medley]
            [cljs-time.core :as time]
            [cljs-time.coerce :as time.coerce]))


(re-frame/reg-event-fx
 :initialise-db
 [interceptors/schema]
 (fn [{:keys [db]} event]
   {:db {:initialising-ticks? true
         :initialising-notes? true
         :input-value ""
         :note-ids '()
         :note-by-id {}
         :tick-ids '()
         :tick-by-id {}
         :focused-tick-id nil}}))


(re-frame/reg-event-fx
 :initialise-ticks
 [interceptors/schema]
 (fn [{:keys [db]} event]
   {:query [:ticks]}))


(re-frame/reg-event-fx
 :initialise-notes
 [interceptors/schema]
 (fn [{:keys [db]} event]
   {:query [:notes]}))


(re-frame/reg-event-fx
 :query-succeeded
 [interceptors/schema interceptors/log]
 (fn [{:keys [db]} [_ query {:keys [notes ticks] :as response}]]
   (case (-> query first keyword)
     :notes (let [notes (map #(update % :id medley/uuid) notes)
                  note-ids (map :id notes)]
              {:db (-> db
                       (assoc :initialising-notes? false)
                       (assoc :note-ids note-ids)
                       (assoc :note-by-id (zipmap note-ids notes)))})
     :ticks (let [ticks (map #(update % :id medley/uuid) ticks)
                  tick-ids (map :id ticks)]
              {:db (-> db
                       (assoc :initialising-ticks? false)
                       (assoc :tick-ids tick-ids)
                       (assoc :tick-by-id (zipmap tick-ids ticks)))})
     (throw Exception.))))


(re-frame/reg-event-fx
 :query-failed
 [interceptors/schema]
 (fn [{:keys [db]} [_ query response]]
   {:db db}))


(re-frame/reg-event-fx
 :command-succeeded
 [interceptors/schema]
 (fn [{:keys [db]} [_ command response]]
   {:db db}))


(re-frame/reg-event-fx
 :command-failed
 [interceptors/schema]
 (fn [{:keys [db]} [_ command response]]
   {:db db}))


(re-frame/reg-event-fx
 :update-input-value
 [interceptors/schema]
 (fn [{:keys [db]} [_ input-value]]
   {:db (assoc db :input-value input-value)}))


(re-frame/reg-event-fx
 :update-focused-tick-id
 [interceptors/schema]
 (fn [{:keys [db]} [_ id]]
   {:db (assoc db :focused-tick-id id)}))


(re-frame/reg-event-fx
 :add-note
 [interceptors/schema]
 (fn [{:keys [db]} [_]]
   (let [note {:id (medley/random-uuid)
               :added-at (time.coerce/to-long (time/now))
               :text (:input-value db)}]
     {:command [:add-note (update note :id str)]
      :db (-> db
              (assoc :input-value "")
              (assoc-in [:note-by-id (:id note)] note)
              (update :note-list #(conj % (:id note))))})))
