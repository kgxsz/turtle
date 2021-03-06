(ns client.events
  (:require [ajax.core :as ajax]
            [re-frame.core :as re-frame]
            [client.interceptors :as interceptors]
            [domkm.silk :as silk]
            [pushy.core :as pushy]
            [medley.core :as medley]
            [clojure.string :as string]
            [reagent.cookies :as cookies]
            [cljs-time.core :as time]
            [cljs-time.coerce :as time.coerce]))


(re-frame/reg-event-fx
 :initialise
 [interceptors/schema]
 (fn [{:keys [db]} event]
   {:db {:fetching-ticks? false
         :fetching-notes? false
         :authorised? (cookies/get :authorised? false)
         :note-ids '()
         :note-by-id {}
         :tick-ids '()
         :tick-by-id {}}
    :listen {}}))


(re-frame/reg-event-fx
 :route
 [interceptors/schema]
 (fn [{:keys [db]} [_ {:keys [route route-params query-params]}]]
   (let [db (-> db
                (assoc :route route)
                (assoc :route-params route-params)
                (assoc :query-params query-params))]
     (case route
       :home {:db db}
       :authorise {:db (assoc db :authorised? true)
                   :cookie [:authorised? true {:max-age 2419200}]
                   :route [:home]}
       :ticker (let [symbol (:symbol route-params)]
                 {:db (-> db
                          (assoc :fetching-ticks? true)
                          (assoc :fetching-notes? true))
                  :query {:notes {:symbol symbol} :ticks {:symbol symbol}}})
       {:db db}))))


(re-frame/reg-event-fx
 :route-to-ticker
 [interceptors/schema]
 (fn [{:keys [db]} [_ symbol]]
   {:route [:ticker {:symbol (name symbol)}]}))


(re-frame/reg-event-fx
 :query-success
 [interceptors/schema]
 (fn [{:keys [db]} [_ query response]]
   (let [{:keys [notes ticks]} query
         {:keys [note-ids tick-ids note-by-id tick-by-id]} response]
     {:db (cond-> db
            (some? notes) (assoc :fetching-notes? false)
            (some? ticks) (assoc :fetching-ticks? false)
            (some? note-ids) (assoc :note-ids note-ids)
            (some? tick-ids) (assoc :tick-ids tick-ids)
            (some? note-by-id) (update :note-by-id merge note-by-id)
            (some? tick-by-id) (update :tick-by-id merge tick-by-id))})))


(re-frame/reg-event-fx
 :query-failure
 [interceptors/schema]
 (fn [{:keys [db]} [_ query response]]
   (js/console.warn "QUERY FAILED!")
   {:db db}))


(re-frame/reg-event-fx
 :command-success
 [interceptors/schema]
 (fn [{:keys [db]} [_ command response]]
   {:db db}))


(re-frame/reg-event-fx
 :command-failure
 [interceptors/schema]
 (fn [{:keys [db]} [_ command response]]
   (js/console.warn "COMMAND FAILED!")
   {:db db}))


(re-frame/reg-event-fx
 :update-hovered-tick
 [interceptors/schema]
 (fn [{:keys [db]} [_ tick-id]]
   {:db (cond-> db
          (some? tick-id) (assoc :hovered-tick-id tick-id)
          (nil? tick-id) (dissoc :hovered-tick-id))}))


(re-frame/reg-event-fx
 :update-hovered-note
 [interceptors/schema]
 (fn [{:keys [db]} [_ note-id]]
   {:db (cond-> db
          (some? note-id) (assoc :hovered-note-id note-id)
          (nil? note-id) (dissoc :hovered-note-id))}))


(re-frame/reg-event-fx
 :activate-note-adder
 [interceptors/schema]
 (fn [{:keys [db]} [_ tick-id]]
   {:db (-> db
            (assoc :input-value "")
            (assoc :clicked-tick-id tick-id))}))


(re-frame/reg-event-fx
 :deactivate-note-adder
 [interceptors/schema]
 (fn [{:keys [db]} [_]]
   {:db (-> db
            (dissoc :input-value)
            (dissoc :clicked-tick-id))}))


(re-frame/reg-event-fx
 :update-input-value
 [interceptors/schema]
 (fn [{:keys [db]} [_ value]]
   (let [sanitised-value (-> value
                             (string/triml)
                             (string/replace #"\n" ""))]
     {:db (assoc db :input-value sanitised-value)})))


(re-frame/reg-event-fx
 :add-note
 [interceptors/schema]
 (fn [{:keys [db]} [_ tick-id]]
   (let [note-id (medley/random-uuid)
         note {:note-id note-id
               :tick-id tick-id
               :added-at (time.coerce/to-long (time/now))
               :text (string/trim (:input-value db))}
         tick (get-in db [:tick-by-id tick-id])]
     {:command {:add-note {:note note :tick tick}}
      :db (-> db
              (assoc-in [:note-by-id note-id] note)
              (update :note-ids #(conj % note-id)))
      :dispatch [:deactivate-note-adder]})))


(re-frame/reg-event-fx
 :delete-note
 [interceptors/schema]
 (fn [{:keys [db]} [_ note-id]]
   (let [{:keys [tick-id] :as note} (get-in db [:note-by-id note-id])
         tick (get-in db [:tick-by-id tick-id])]
     {:command {:delete-note {:note note :tick tick}}
      :db (-> db
              (update :note-by-id dissoc note-id)
              (update :note-ids #(remove (partial = note-id) %))
              (dissoc :hovered-note-id))})))

