(ns client.schema
  (:require [cljs.spec.alpha :as s]
            [clojure.string :as string]
            [medley.core :as medley]))

(s/def ::initialising-routing? boolean?)

(s/def ::initialising-ticks? boolean?)

(s/def ::initialising-notes? boolean?)

(s/def ::route keyword?)

(s/def ::input-value string?)

(s/def ::id medley/uuid?)


(s/def ::added-at int?)

(s/def ::text (s/and string?
                     (complement string/blank?)
                     (fn [s] (<= (count s) 128))))

(s/def ::note (s/keys :req-un [::id
                               ::added-at
                               ::instant
                               ::text]))

(s/def ::note-ids (s/coll-of ::id))

(s/def ::note-by-id (s/and map? (s/map-of ::id ::note)))


(s/def ::instant int?)

(s/def ::symbol #{"AAPL"})

(s/def ::open float?)

(s/def ::close float?)

(s/def ::tick (s/keys :req-un [::id
                               ::symbol
                               ::instant
                               ::open
                               ::close]))

(s/def ::tick-ids (s/coll-of ::id))

(s/def ::tick-by-id (s/and map? (s/map-of ::id ::tick)))

(s/def ::focused-tick-id (s/nilable medley/uuid?))

(s/def ::db (s/keys :req-un [::initialising-routing?
                             ::initialising-ticks?
                             ::initialising-notes?
                             ::route
                             ::input-value
                             ::note-ids
                             ::note-by-id
                             ::tick-ids
                             ::tick-by-id
                             ::focused-tick-id]))
