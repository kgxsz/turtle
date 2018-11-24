(ns turtle.events
  (:require [ajax.core :as ajax]
            [re-frame.core :as re-frame]
            [turtle.interceptors :as interceptors]))


(re-frame/reg-event-fx
 :initialise
 [interceptors/schema]
 (fn [{:keys [db]} event]
   {:query [:calendars]
    :db {:initialising? true
         :calendar-by-id {1 {:id 1 :checked-dates []}
                          2 {:id 2 :checked-dates []}
                          3 {:id 3 :checked-dates []}
                          4 {:id 4 :checked-dates []}
                          5 {:id 5 :checked-dates []}}}}))


(re-frame/reg-event-fx
 :query-succeeded
 [interceptors/schema]
 (fn [{:keys [db]} [_ query {:keys [calendars] :as response}]]
   (case (-> query first keyword)
     :calendars {:db (-> db
                         (assoc :initialising? false)
                         (assoc :calendar-by-id (zipmap (map :id calendars) calendars)))}
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
 :add-checked-date
 [interceptors/schema]
 (fn [{:keys [db]} [_ id date]]
   {:command [:add-checked-date id date]
    :db (update-in db [:calendar-by-id id :checked-dates] #(-> % set (conj date) vec))}))


(re-frame/reg-event-fx
 :remove-checked-date
 [interceptors/schema]
 (fn [{:keys [db]} [_ id date]]
   {:command [:remove-checked-date id date]
    :db (update-in db [:calendar-by-id id :checked-dates] #(-> % set (disj date) vec))}))
