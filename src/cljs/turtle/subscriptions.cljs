(ns turtle.subscriptions
  (:require-macros [reagent.ratom :refer [reaction]])
  (:require [re-frame.core :as re-frame]))


(re-frame/reg-sub
 :initialising?
 (fn [db [_ ]]
   (:initialising? db)))


(re-frame/reg-sub
 :input-value
 (fn [db [_ ]]
   (:input-value db)))


#_(re-frame/reg-sub
 :checked-dates
 (fn [db [_ id]]
   (get-in db [:calendar-by-id id :checked-dates])))
