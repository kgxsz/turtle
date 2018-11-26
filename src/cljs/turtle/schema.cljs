(ns turtle.schema
  (:require [cljs.spec.alpha :as spec]
            [clojure.string :as str]))


;; TODO - include length here
(spec/def ::text (spec/and string? (complement str/blank?)))

(spec/def ::initialising? boolean?)

(spec/def ::input-value string?)

(spec/def ::db (spec/keys :req-un [::input-value
                                   ::initialising?]))
