(ns turtle.events
  (:require [ajax.core :as ajax]
            [re-frame.core :as re-frame]
            [turtle.interceptors :as interceptors]
            [medley.core :as medley]
            [cljs-time.core :as time]
            [cljs-time.coerce :as time.coerce]))


(re-frame/reg-event-fx
 :initialise
 [interceptors/schema]
 (fn [{:keys [db]} event]
   {:query [:notes]
    :db {:initialising? true
         :input-value ""
         :notes []
         :notes-by-id {}}}))


(re-frame/reg-event-fx
 :query-succeeded
 [interceptors/schema]
 (fn [{:keys [db]} [_ query {:keys [notes] :as response}]]
   (case (-> query first keyword)
     :notes {:db (-> db
                     (assoc :initialising? false)
                     #_(assoc :notes-by-id (zipmap (map :id calendars) calendars)))}
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


(re-frame/reg-event-db
 :update-input-value
 [interceptors/schema]
 (fn  [db [_ input-value]]
   (-> db
       (assoc :input-value input-value))))


(re-frame/reg-event-db
 :add-note
 [interceptors/schema interceptors/debug]
 (fn [db [_]]
   (let [id (medley/random-uuid)
         added-at (time.coerce/to-long (time/now))  ;; TODO - use cljs-time's long
         note {:id id
               :added-at added-at
               :text (:input-value db)}]
     (-> db
         (assoc :input-value "")
         (assoc-in [:notes-by-id id] note)
         (update-in [:notes] (partial cons id))))))


#_(re-frame/reg-event-fx
 :add-checked-date
 [interceptors/schema]
 (fn [{:keys [db]} [_ id date]]
   {:command [:add-checked-date id date]
    :db (update-in db [:calendar-by-id id :checked-dates] #(-> % set (conj date) vec))}))


#_(re-frame/reg-event-fx
 :remove-checked-date
 [interceptors/schema]
 (fn [{:keys [db]} [_ id date]]
   {:command [:remove-checked-date id date]
    :db (update-in db [:calendar-by-id id :checked-dates] #(-> % set (disj date) vec))}))
