(ns styles.constants)

(def colour
  {:black-one "#000000"
   :black-two "#333333"
   :black-three "#454545"
   :black-four "#666666"
   :grey-one "#AAAAAA"
   :grey-two "#CCCCCC"
   :white-one "#FFFFFF"
   :white-two "#FDFDFD"
   :white-three "#F4F4F4"
   :green-one "#FBFDF8"
   :green-two "#8ACA55"
   :yellow-one "#FEFCF8"
   :yellow-two "#FADA6E"
   :red-one "#FFFAFB"
   :red-two "#EB5468"})

(def spacing
  {:xxx-tiny 1
   :xx-tiny 2
   :x-tiny 3
   :tiny 4
   :xxx-small 6
   :xx-small 8
   :x-small 10
   :small 12
   :medium 16
   :large 20
   :x-large 30
   :xx-large 40
   :xxx-large 50
   :huge 60
   :x-huge 100
   :xx-huge 140
   :xxx-huge 180})

(def filling
  {:xxx-tiny 1
   :xx-tiny 2
   :x-tiny 3
   :tiny 4
   :xxx-small 6
   :xx-small 10
   :x-small 14
   :small 18
   :medium 24
   :large 30
   :x-large 40
   :xx-large 50
   :xxx-large 70
   :huge 100
   :x-huge 150
   :xx-huge 210
   :xxx-huge 270})

(def breakpoint
  {:tiny {:end 319}
   :small {:start 320 :end 479}
   :medium {:start 480 :end 767}
   :large {:start 768 :end 1023}
   :huge {:start 1024}})

(def radius
  {:tiny 1
   :small 2
   :medium 3
   :large 4
   :huge 8})

(def proportion
  {:0 0
   :5 5
   :10 10
   :15 15
   :20 20
   :25 25
   :30 30
   :35 35
   :40 40
   :45 45
   :50 50
   :55 55
   :60 60
   :65 65
   :70 70
   :75 75
   :80 80
   :85 85
   :90 90
   :95 95
   :100 100})

(def font-size
  {:xx-tiny 8
   :x-tiny 9
   :tiny 10
   :xxx-small 11
   :xx-small 12
   :x-small 13
   :medium 14
   :large 15
   :x-large 16
   :xx-large 17
   :xxx-large 18
   :huge 22
   :x-huge 28
   :xx-huge 32
   :xxx-huge 40})

(def ticker-plot
  ;; TODO - just use numbers here
  {:width (- (-> breakpoint :huge :start)
             (:xxx-large spacing)
             (:x-large spacing)
             (:xxx-large filling))
   :height (:xx-huge filling)
   :padding (:xx-tiny spacing)
   :circle-radius (:small radius)})

(def editor
  {:width 700
   :height 220})

(def tooltip
  ;; TODO - just use numbers here
  {:width (:huge filling)
   :height (+ (:xx-large filling)
              (:x-small filling)
              (:tiny filling))})
