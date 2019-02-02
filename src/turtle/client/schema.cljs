(ns client.schema
  (:require [cljs.spec.alpha :as s]
            [clojure.string :as string]
            [medley.core :as medley]))

(s/def ::initialising-ticker? boolean?)

(s/def ::initialising-notes? boolean?)

(s/def ::input-value string?)

(s/def ::id medley/uuid?)

(s/def ::added-at int?)


(s/def ::text (s/and string?
                     (complement string/blank?)
                     (fn [s] (<= (count s) 128))))

(s/def ::note-list (s/coll-of ::id :type vector?))

(s/def ::note (s/keys :req-un [::id
                               ::added-at
                               ::text]))

(s/def ::note-by-id (s/and map? (s/map-of ::id ::note)))

(s/def ::instant int?)

(s/def ::open float?)

(s/def ::close float?)

(s/def ::tick (s/keys :req-un [::instant
                               ::open
                               ::close]))

(s/def ::ticker (s/coll-of ::tick :type vector?))

(s/def ::db (s/keys :req-un [::initialising-ticker?
                             ::initialising-notes?
                             ::input-value
                             ::note-list
                             ::note-by-id
                             ::ticker]))
