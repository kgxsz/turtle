(ns turtle.schema
  (:require [cljs.spec.alpha :as spec]
            [clojure.string :as str]))

(spec/def ::initialising? boolean?)

(spec/def ::input-value string?)

(spec/def ::id int?)

;; TODO - include length here
(spec/def ::text (spec/and string? (complement str/blank?)))


(spec/def ::notes (spec/coll-of ::id))

(spec/def ::note (spec/keys :req-un [::id
                                     ::added-at
                                     ::text]))

(spec/def ::notes-by-id (spec/and map? (spec/map-of ::id ::note)))


(spec/def ::db (spec/keys :req-un [::initialising?
                                   ::input-value
                                   ::notes
                                   ::notes-by-id]))
