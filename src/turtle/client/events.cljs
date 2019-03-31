(ns client.events
  (:require [ajax.core :as ajax]
            [re-frame.core :as re-frame]
            [client.interceptors :as interceptors]
            [domkm.silk :as silk]
            [pushy.core :as pushy]
            [medley.core :as medley]
            [cljs-time.core :as time]
            [cljs-time.coerce :as time.coerce]))


(re-frame/reg-event-fx
 :initialise-db
 [interceptors/schema]
 (fn [{:keys [db]} event]
   {:db {:initialising-routing? true
         :initialising-ticks? true
         :initialising-notes? true
         :route :unknown
         :input-value ""
         :note-ids '()
         :note-by-id {}
         :tick-ids '()
         :tick-by-id {}
         :focused-tick-id nil}}))


(re-frame/reg-event-fx
 :initialise-routing
 [interceptors/schema]
 (fn [{:keys [db]} event]
   (let [routes (silk/routes [[:home [[]]]
                              [:user [["26031987"]]]])
         dispatch (fn [route] (re-frame/dispatch [:route route]))
         parse-url (fn [url]
                     (js/console.warn url)
                     (or (silk/arrive routes url) {}))]
     (pushy/start! (pushy/pushy dispatch parse-url))
     {})))

(def routes (silk/routes [[:home [[]]]
                          [:user [["26031987"]]]]))

(silk/arrive routes "26031987/s")

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
 :route
 [interceptors/schema]
 (fn [{:keys [db]} [_ route]]
   (js/console.warn route)
   {:db (-> db
            (assoc :initialising-routing? false)
            (assoc :route :hello))}))


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
               :instant (get-in db [:tick-by-id (:focused-tick-id db) :instant])
               :text "Some test note" #_(:input-value db)}]
     {;:command [:add-note (update note :id str)]
      :db (-> db
              #_(assoc :input-value "")
              (assoc-in [:note-by-id (:id note)] note)
              (update :note-ids #(conj % (:id note))))})))
