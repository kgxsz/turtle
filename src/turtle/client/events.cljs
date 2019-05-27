(ns client.events
  (:require [ajax.core :as ajax]
            [re-frame.core :as re-frame]
            [client.interceptors :as interceptors]
            [domkm.silk :as silk]
            [pushy.core :as pushy]
            [medley.core :as medley]
            [clojure.string :as string]
            [cljs-time.core :as time]
            [cljs-time.coerce :as time.coerce]))


(re-frame/reg-event-fx
 :initialise-db
 [interceptors/schema]
 (fn [{:keys [db]} event]
   {:db {:initialising-routing? true
         :fetching-ticks? false
         :fetching-notes? false
         :route :unknown
         :authorised? false
         :note-ids '()
         :note-by-id {}
         :tick-ids '()
         :tick-by-id {}}}))


(re-frame/reg-event-fx
 :initialise-routing
 [interceptors/schema]
 (fn [{:keys [db]} event]
   {:initialise-routing []}))


(re-frame/reg-event-fx
 :route-success
 [interceptors/schema]
 (fn [{:keys [db]} [_ {:keys [route route-params query-params]}]]
   (let [db (-> db
                (assoc :initialising-routing? false)
                (assoc :authorised? (= (:auth query-params) "26031987"))
                (assoc :route route))]
     (case route
       :home {:db (-> db
                      (assoc :fetching-notes? true))
              :queries [[:notes]]}
       :ticker (let [symbol (:symbol route-params)]
                 {:db (-> db
                          (assoc :fetching-ticks? true)
                          (assoc :fetching-notes? true)
                          (assoc :symbol symbol))
                  :queries [[:notes symbol] [:ticks symbol]]})
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
   (case (-> query first keyword)
     :notes (let [note-by-id (->> response
                                  :note-by-id
                                  ;; TODO - remove this when transit is used
                                  (medley/map-keys (comp medley/uuid name))
                                  (medley/map-vals #(-> %
                                                        (update :note-id medley/uuid)
                                                        (update :tick-id medley/uuid))))
                  note-ids (->> response
                                :note-ids
                                (map medley/uuid))
                  tick-by-id (->> response
                                  :tick-by-id
                                  ;; TODO - remove this when transit is used
                                  (medley/map-keys (comp medley/uuid name))
                                  (medley/map-vals #(-> %
                                                        (update :tick-id medley/uuid)
                                                        (update :symbol (comp keyword string/lower-case)))))]
              {:db (-> db
                       (assoc :fetching-notes? false)
                       (update :note-by-id merge note-by-id)
                       (assoc :note-ids note-ids)
                       (update :tick-by-id merge tick-by-id))})
     :ticks (let [tick-by-id (->> response
                                  :tick-by-id
                                  ;; TODO - remove this when transit is used
                                  (medley/map-keys (comp medley/uuid name))
                                  (medley/map-vals #(-> %
                                                        (update :tick-id medley/uuid)
                                                        (update :symbol (comp keyword string/lower-case)))))
                  tick-ids (->> response
                                :tick-ids
                                (map medley/uuid))]
              {:db (-> db
                       (assoc :fetching-ticks? false)
                       (update :tick-by-id merge tick-by-id)
                       (assoc :tick-ids tick-ids))})
     (throw Exception.))))


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
 (fn [{:keys [db]} [_ tick-id input-value]]
   (let [note-id (medley/random-uuid)
         note {:note-id note-id
               :tick-id tick-id
               :added-at (time.coerce/to-long (time/now))
               :text (string/trim input-value)}]
     ;; TODO - pass uuid when transit is used
     {:commands [[:add-note (-> note
                                (update :note-id str)
                                (update :tick-id str))]]
      :db (-> db
              (assoc-in [:note-by-id note-id] note)
              (update :note-ids #(conj % note-id)))
      :dispatch [:deactivate-note-adder]})))


(re-frame/reg-event-fx
 :delete-note
 [interceptors/schema]
 (fn [{:keys [db]} [_ note-id]]
   ;; TODO - pass uuid when transit is used
   {:commands [[:delete-note (str note-id)]]
    :db (-> db
            (update :note-by-id dissoc note-id)
            (update :note-ids #(remove (partial = note-id) %))
            (dissoc :hovered-note-id))}))

