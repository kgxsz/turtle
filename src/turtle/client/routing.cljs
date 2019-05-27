(ns client.routing
  (:require [re-frame.core :as re-frame]
            [domkm.silk :as silk]))


(defonce !history (atom nil))

(defonce routes (silk/routes [[:home [[]]]
                              [:authorise [["authorise"]]]
                              [:ticker [[:symbol]]]]))
