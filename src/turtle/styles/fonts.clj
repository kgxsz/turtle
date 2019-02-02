(ns styles.fonts
  (:require [garden.stylesheet :refer [at-font-face]]))

(def icomoon
  (at-font-face {:font-family "'icomoon'"
                 :font-weight :normal
                 :font-style :normal
                 :src "url('/fonts/icomoon.eot?r0cvwu#iefix') format('embedded-opentype'),
                       url('/fonts/icomoon.woff2?r0cvwu') format('woff2'),
                       url('/fonts/icomoon.ttf?r0cvwu') format('truetype'),
                       url('/fonts/icomoon.woff?r0cvwu') format('woff'),
                       url('/fonts/icomoon.svg?r0cvwu#icomoon') format('svg')"}))
