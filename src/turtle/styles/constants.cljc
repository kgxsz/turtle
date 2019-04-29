(ns styles.constants)

(def colour
  {:black-light "#333333"
   :grey-dark "#666666"
   :grey-medium "#AAAAAA"
   :grey-pale "#CCCCCC"
   :grey-light "#F4F4F4"
   :white-medium "#FDFDFD"
   :white-light "#FFFFFF"
   :green-light"#FBFDF8"
   :green-dark "#8ACA55"
   :yellow-light "#FEFCF8"
   :yellow-dark "#FADA6E"
   :purple-dark "#D4A3E3"
   :blue-light "#F9FBFF"
   :blue-medium "#F2F6FF"
   :blue-dark "#58A1F5"
   :red-light "#FFFAFB"
   :red-dark "#EB5468"})

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
   :huge 20
   :x-huge 28
   :xx-huge 32
   :xxx-huge 40})

(def plot
  {:width (- (-> breakpoint :huge :start)
             (:xxx-large spacing)
             (:x-large spacing)
             (:xxx-large filling))
   :height (:xx-huge filling)
   :padding (:xx-tiny spacing)
   :circle-radius (:small radius)})

(def tooltip
  {:width (:huge filling)
   :height (+ (:xx-large filling)
              (:x-small filling)
              (:tiny filling))})
