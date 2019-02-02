(ns turtle.events
  (:require [ajax.core :as ajax]
            [re-frame.core :as re-frame]
            [turtle.interceptors :as interceptors]
            [medley.core :as medley]
            [cljs-time.core :as time]
            [cljs-time.coerce :as time.coerce]))


(re-frame/reg-event-fx
 :initialise-db
 [interceptors/schema]
 (fn [{:keys [db]} event]
   {:db {:initialising-ticker? true
         :initialising-notes? true
         :input-value ""
         :note-list []
         :note-by-id {}
         :ticker []}}))


(re-frame/reg-event-fx
 :initialise-ticker
 [interceptors/schema]
 (fn [{:keys [db]} event]
   {:query [:ticker]}))


(re-frame/reg-event-fx
 :initialise-notes
 [interceptors/schema]
 (fn [{:keys [db]} event]
   {:query [:notes]}))


(re-frame/reg-event-fx
 :query-succeeded
 [interceptors/schema interceptors/log]
 (fn [{:keys [db]} [_ query {:keys [notes ticker] :as response}]]
   (case (-> query first keyword)
     :notes (let [notes (mapv #(update % :id medley/uuid) notes)
                  note-list (mapv :id notes)]
              {:db (-> db
                       (assoc :initialising-notes? false)
                       (assoc :note-list note-list)
                       (assoc :note-by-id (zipmap note-list notes)))})
     :ticker (let [ticker (sort-by :instant ticker)]
               {:db (-> db
                        (assoc :initialising-ticker? false)
                        (assoc :ticker ticker))})
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
