(ns turtle.interceptors
  (:require [cljs.spec.alpha :as spec]
            [re-frame.core :as re-frame]
            [turtle.schema :as schema]))


(def schema
  (re-frame/after
   (fn [db]
     (when-not (spec/valid? ::schema/db db)
       (throw (ex-info (str "spec check failed: " (spec/explain-str ::schema/db db)) {}))))))

