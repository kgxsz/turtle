(ns turtle.interceptors
  (:require [cljs.spec.alpha :as s]
            [expound.alpha :as expound]
            [re-frame.core :as re-frame]
            [turtle.schema :as schema]))


(def schema
  (re-frame/after
   (fn [db]
     (when-not (s/valid? ::schema/db db)
       (js/console.error (expound/expound-str ::schema/db db))
       (throw (ex-info "the db spec has been violated"
                       {:spec ::schema/db
                        :db db}))))))


(def debug
  (re-frame/after
   (fn [db]
     (js/console.warn db))))

