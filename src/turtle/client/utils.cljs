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


(defn find-tick-position [tick tick-positions]
  (first (filter #(= (:tick-id tick) (:tick-id %)) tick-positions)))


(def compact-time-formatter (t.format/formatter "MMM do"))
(def regular-time-formatter (t.format/formatter "MMMM do, YYYY"))


(defn format-time [formatter instant]
  (when instant
    (t.format/unparse formatter (t.coerce/from-long instant))))


(defn format-compact-time [instant]
  (format-time compact-time-formatter instant))


(defn format-regular-time [instant]
  (format-time regular-time-formatter instant))


(defn format-price [price]
  (when price
    (format/format "%.1f" price)))


(defn axis [n length values]
  (let [maximum-value (apply max values)
        minimum-value (apply min values)
        spacing (/ length n)]
    (->> (iterate (partial + spacing) (halve spacing))
         (take n)
         (map (partial * (/ (- maximum-value minimum-value) length)))
         (map (partial + minimum-value)))))


(def instant-axis
  (memoize
   (fn [ticks]
     (axis 7 (:width c/ticker-plot) (map :instant ticks)))))


(def close-axis
  (memoize
   (fn [ticks]
     (reverse (axis 5 (:height c/ticker-plot) (map :close ticks))))))


(def tick-positions
  (memoize
   (fn [ticks]
     (let [width (- (:width c/ticker-plot) (twice (:circle-radius c/ticker-plot)))
           height (- (:height c/ticker-plot) (twice (:circle-radius c/ticker-plot)))
           closes (map :close ticks)
           instants (map :instant ticks)
           maximum-close (apply max closes)
           minimum-close (apply min closes)
           maximum-instant (apply max instants)
           minimum-instant (apply min instants)
           normalise-instant (fn [instant]
                               (+ (:circle-radius c/ticker-plot)
                                  (* width
                                     (/ (- instant minimum-instant)
                                        (- maximum-instant minimum-instant)))))
           normalise-close (fn [close]
                             (- (:height c/ticker-plot)
                                (:circle-radius c/ticker-plot)
                                (* height
                                   (/ (- close minimum-close)
                                      (- maximum-close minimum-close)))))
           partitioned-ticks (partition 3 1 ticks)
           inner-tick-positions (for [[left-tick center-tick right-tick] partitioned-ticks]
                                  (let [left (halve
                                              (+ (normalise-instant (:instant left-tick))
                                                 (normalise-instant (:instant center-tick))))
                                        right (halve
                                               (+ (normalise-instant (:instant center-tick))
                                                  (normalise-instant (:instant right-tick))))]
                                    {:tick-id (:tick-id center-tick)
                                     :x (normalise-instant (:instant center-tick))
                                     :y (normalise-close (:close center-tick))
                                     :left left
                                     :right right
                                     :width (- right left)}))
           initial-tick-position {:tick-id (-> ticks first :tick-id)
                                  :x (-> ticks first :instant normalise-instant)
                                  :y (-> ticks first :close normalise-close)
                                  :left (- (:circle-radius c/ticker-plot)
                                           (halve (:x-large c/filling)))
                                  :right (+ (:circle-radius c/ticker-plot)
                                            (halve (- (:x-large c/filling)))
                                            (:left (first inner-tick-positions)))
                                  :width (+ (halve (:x-large c/filling))
                                            (- (:circle-radius c/ticker-plot))
                                            (:left (first inner-tick-positions)))}
           final-tick-position {:tick-id (-> ticks last :tick-id)
                                :x (-> ticks last :instant normalise-instant)
                                :y (-> ticks last :close normalise-close)
                                :left (:right (last inner-tick-positions))
                                :right (+ (halve (:x-large c/filling))
                                          (- (:circle-radius c/ticker-plot))
                                          (:width c/ticker-plot))
                                :width (- (+ (halve (:x-large c/filling))
                                             (:width c/ticker-plot))
                                          (:circle-radius c/ticker-plot)
                                          (:right (last inner-tick-positions)))}]

       (concat [initial-tick-position]
               inner-tick-positions
               [final-tick-position])))))


(def tick-position
  (memoize
   (fn [tick-id ticks]
     (->> (tick-positions ticks)
          (filter #(= tick-id (:tick-id %)))
          (first)))))
