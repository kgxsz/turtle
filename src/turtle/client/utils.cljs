(ns client.utils
  (:require [styles.constants :as c]
            [cljs-time.core :as t]
            [cljs-time.format :as t.format]
            [cljs-time.coerce :as t.coerce]
            [reagent.format :as format]))


(defn bem
  "Creates a class string from bem structured arguments. Take multiple arguments in vectors.
  Each vector is composed of the block-elements keyword, then the optional modifiers.
  (bem [:block__element__element :modifier :modifier]
       [:block__element__element :modifier (if pred? :modifier-a :modifier-b) (when pred? :modifier-a)])"
  [& xs]
  (->> (for [x xs]
         (let [block-elements (first x)
               modifiers (->> x rest (remove nil?))]
           (cons
            (name block-elements)
            (for [modifier modifiers]
              (str (name block-elements) "--" (name modifier))))))
       (flatten)
       (interpose " ")
       (apply str)))


(defn view-box
  "Creates a view-box attribute for SVGs."
  [width height]
  (apply str (interpose " " [0 0 width height])))


(defn halve [x]
  (/ x 2))


(defn twice [x]
  (* 2 x))


(defn get-by-id [id xs]
  (first (filter #(= id (:id %)) xs)))


(def compact-time-formatter (t.format/formatter "MMM do"))


(defn format-compact-time [instant]
  (t.format/unparse compact-time-formatter (t.coerce/from-long instant)))


(defn format-price [price]
  (format/format "%.1f" price))


(defn tick-positions [ticks]
  (let [width (- (:width c/plot) (twice (:circle-radius c/plot)))
        height (- (:height c/plot) (twice (:circle-radius c/plot)))
        closes (map :close ticks)
        instants (map :instant ticks)
        maximum-close (apply max closes)
        minimum-close (apply min closes)
        maximum-instant (apply max instants)
        minimum-instant (apply min instants)
        normalise-instant (fn [instant]
                            (+ (:circle-radius c/plot)
                               (* width
                                  (/ (- instant minimum-instant)
                                     (- maximum-instant minimum-instant)))))
        normalise-close (fn [close]
                          (- (:height c/plot)
                             (:circle-radius c/plot)
                             (* height
                                (/ (- close minimum-close)
                                   (- maximum-close minimum-close)))))
        partitioned-ticks (partition 3 1 ticks)
        normalised-ticks (for [[left-tick center-tick right-tick] partitioned-ticks]
                           (let [left (halve
                                       (+ (normalise-instant (:instant left-tick))
                                          (normalise-instant (:instant center-tick))))
                                 right (halve
                                        (+ (normalise-instant (:instant center-tick))
                                           (normalise-instant (:instant right-tick))))]
                             {:id (:id center-tick)
                              :x (normalise-instant (:instant center-tick))
                              :y (normalise-close (:close center-tick))
                              :left left
                              :right right
                              :width (- right left)}))]
    (concat [{:id (-> ticks first :id)
              :x (-> ticks first :instant normalise-instant)
              :y (-> ticks first :close normalise-close)
              :left (- (:circle-radius c/plot)
                       (halve (:x-large c/filling)))
              :right (+ (:circle-radius c/plot)
                        (halve (- (:x-large c/filling)))
                        (:left (first normalised-ticks)))
              :width (+ (halve (:x-large c/filling))
                        (- (:circle-radius c/plot))
                        (:left (first normalised-ticks)))}]
            normalised-ticks
            [{:id (-> ticks last :id)
              :x (-> ticks last :instant normalise-instant)
              :y (-> ticks last :close normalise-close)
              :left (:right (last normalised-ticks))
              :right (+ (halve (:x-large c/filling))
                        (- (:circle-radius c/plot))
                        (:width c/plot))
              :width (- (+ (halve (:x-large c/filling))
                           (:width c/plot))
                        (:circle-radius c/plot)
                        (:right (last normalised-ticks)))}])))
