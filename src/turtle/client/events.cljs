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
         :initialising-ticks? true
         :initialising-notes? true
         :page :unknown
         :authorised? false
         :note-ids '()
         :note-by-id {}
         :tick-ids '()
         :tick-by-id {}}}))


(re-frame/reg-event-fx
 :initialise-routing
 [interceptors/schema]
 (fn [{:keys [db]} event]
   (let [routes (silk/routes [[:root [[]]]
                              [:ticker [[:symbol]]]])
         dispatch (fn [route] (re-frame/dispatch [:route route]))
         parse-url (fn [url] (or (silk/arrive routes url) {}))]
     (pushy/start! (pushy/pushy dispatch parse-url))
     {})))


(re-frame/reg-event-fx
 :initialise-ticks
 [interceptors/schema]
 (fn [{:keys [db]} event]
   (if (= :ticker (:page db))
     {:query [:ticks (:symbol db)]}
     {})))


(re-frame/reg-event-fx
 :initialise-notes
 [interceptors/schema]
 (fn [{:keys [db]} event]
   (if (= :ticker (:page db))
     {:query [:notes (:symbol db)]}
     {})))


(re-frame/reg-event-fx
 :route
 [interceptors/schema]
 (fn [{:keys [db]} [_ route]]
   (let [query-params (-> route ::silk/url :query)]
     ;; TODO - move this when routing isn't done on refresh
     (re-frame/dispatch [:initialise-ticks])
     (re-frame/dispatch [:initialise-notes])
     {:db (-> db
              (assoc :initialising-routing? false)
              ;; TODO - move this when better auth is done
              (assoc :authorised? (= (get query-params "auth") "26031987"))
              (assoc :symbol (-> route :symbol string/upper-case))
              (assoc :page (or (::silk/name route) :unknown)))})))


(re-frame/reg-event-fx
 :query-success
 [interceptors/schema]
 (fn [{:keys [db]} [_ query response]]
   (js.console.warn response)
   (case (-> query first keyword)
     :notes (let [note-by-id (->> response
                                  :note-by-id
                                  ;; TODO - remove this when transit is used
                                  (medley/map-keys (comp medley/uuid name))
                                  (medley/map-vals #(update % :note-id medley/uuid)))
                  note-ids (->> response
                                :note-ids
                                (map medley/uuid))]
              {:db (-> db
                       (assoc :initialising-notes? false)
                       (assoc :note-by-id note-by-id)
                       (assoc :note-ids note-ids))})
     :ticks (let [tick-by-id (->> response
                                  :tick-by-id
                                  ;; TODO - remove this when transit is used
                                  (medley/map-keys (comp medley/uuid name))
                                  (medley/map-vals #(update % :tick-id medley/uuid)))
                  tick-ids (->> response
                                :tick-ids
                                (map medley/uuid))]
              {:db (-> db
                       (assoc :initialising-ticks? false)
                       (assoc :tick-by-id tick-by-id)
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
     {:command [:add-note (update note :note-id str)]
      :db (-> db
              (assoc-in [:note-by-id note-id] note)
              (update :note-ids #(conj % note-id)))
      :dispatch [:deactivate-note-adder]})))


(re-frame/reg-event-fx
 :delete-note
 [interceptors/schema]
 (fn [{:keys [db]} [_ note-id]]
   ;; TODO - pass uuid when transit is used
   {:command [:delete-note (str note-id)]
    :db (-> db
            (update :note-by-id dissoc note-id)
            (update :note-ids #(remove (partial = note-id) %))
            (dissoc :hovered-note-id))}))

