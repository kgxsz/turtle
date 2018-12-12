(ns turtle.schema
  (:require [cljs.spec.alpha :as s]
            [clojure.string :as string]
            [medley.core :as medley]))

(s/def ::initialising? boolean?)

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


(s/def ::db (s/keys :req-un [::initialising?
                             ::input-value
                             ::note-list
                             ::note-by-id]))
