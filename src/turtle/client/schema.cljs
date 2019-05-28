(ns client.schema
  (:require [cljs.spec.alpha :as s]
            [clojure.string :as string]
            [medley.core :as medley]))


(s/def ::id medley/uuid?)


(s/def ::initialising-routing? boolean?)


(s/def ::fetching-ticks? boolean?)


(s/def ::fetching-notes? boolean?)


(s/def ::route #{:home :authorise :ticker :unknown})

(s/def ::route-params map?)

(s/def ::query-params map?)

(s/def ::authorised? boolean?)


(s/def ::note-id ::id)

(s/def ::added-at int?)

(s/def ::text (s/and string?
                     (complement string/blank?)
                     (fn [s] (<= (count s) 128))))

(s/def ::note (s/keys :req-un [::note-id
                               ::tick-id
                               ::added-at
                               ::text]))

(s/def ::note-ids (s/coll-of ::note-id))

(s/def ::note-by-id (s/and map? (s/map-of ::note-id ::note)))


(s/def ::tick-id ::id)

(s/def ::symbol keyword?)

(s/def ::instant int?)

(s/def ::open float?)

(s/def ::high float?)

(s/def ::low float?)

(s/def ::close float?)

(s/def ::adjusted-close float?)

(s/def ::volume float?)

(s/def ::tick (s/keys :req-un [::tick-id
                               ::added-at
                               ::symbol
                               ::instant
                               ::open
                               ::high
                               ::low
                               ::close
                               ::adjusted-close
                               ::volume]))


(s/def ::tick-ids (s/coll-of ::tick-id))


(s/def ::tick-by-id (s/and map? (s/map-of ::tick-id ::tick)))


(s/def ::hovered-tick-id ::tick-id)

(s/def ::hovered-note-id ::note-id)

(s/def ::clicked-tick-id ::tick-id)

(s/def ::input-value string?)

(s/def ::db (s/keys :req-un [::authorised?
                             ::fetching-ticks?
                             ::fetching-notes?
                             ::note-ids
                             ::note-by-id
                             ::tick-ids
                             ::tick-by-id]
                    :opt-un [::route
                             ::route-params
                             ::query-params
                             ::hovered-tick-id
                             ::hovered-note-id
                             ::clicked-tick-id
                             ::input-value]))
