(ns turtle.schema
  (:require [cljs.spec.alpha :as spec]
            [clojure.string :as str]))

(spec/def ::id int?)

(spec/def ::date (spec/and string? (complement str/blank?)))

(spec/def ::checked-dates (spec/coll-of ::date))

(spec/def ::calendar (spec/keys :req-un [::id
                                         ::checked-dates]))

(spec/def ::calendar-by-id (spec/and map? (spec/map-of ::id ::calendar)))

(spec/def ::initialising? boolean?)

(spec/def ::db (spec/keys :req-un [::calendar-by-id
                                   ::initialising?]))
