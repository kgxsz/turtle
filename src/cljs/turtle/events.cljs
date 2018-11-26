(ns turtle.events
  (:require [ajax.core :as ajax]
            [re-frame.core :as re-frame]
            [turtle.interceptors :as interceptors]))


(re-frame/reg-event-fx
 :initialise
 [interceptors/schema]
 (fn [{:keys [db]} event]
   {:query [:notes]
    :db {:initialising? true
         :input-value ""}}))


(re-frame/reg-event-fx
 :query-succeeded
 [interceptors/schema]
 (fn [{:keys [db]} [_ query {:keys [calendars] :as response}]]
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
