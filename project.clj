(defproject turtle "0.1.0-SNAPSHOT"
  :dependencies [[org.clojure/clojure "1.9.0"]
                 [org.clojure/clojurescript "1.10.339"]
                 [reagent "0.8.1"]
                 [reagent-utils "0.3.2"]
                 [re-frame "0.10.6"]
                 [cljs-ajax "0.7.4"]
                 [garden "1.3.6"]
                 [com.powernoodle/normalize "7.0.0"]
                 [com.amazonaws/aws-lambda-java-core "1.0.0"]
                 [com.taoensso/faraday "1.9.0"]
                 [cheshire "5.8.0"]
                 [com.domkm/silk "0.1.2"]
                 [kibu/pushy "0.3.8"]
                 [ring/ring-core "1.7.1"]
                 [medley "1.0.0"]
                 [expound "0.7.1"]
                 [clj-time "0.15.0"]
                 [clj-http "3.9.1"]
                 [danlentz/clj-uuid "0.1.7"]
                 [camel-snake-kebab "0.4.0"]
                 [com.andrewmcveigh/cljs-time "0.5.2"]]

  :source-paths ["src/turtle"]

  :plugins [[lein-cljsbuild "1.1.5"]
            [lein-garden "0.3.0"]]

  :clean-targets ^{:protect false} ["resources/public/js/compiled"
                                    "resources/public/css"
                                    "target"
                                    ".nrepl-port"
                                    ".lein-repl-history"]

  :profiles {:dev {:source-paths ["dev/turtle"]
                   :dependencies [[binaryage/devtools "0.9.10"]
                                  [figwheel-sidecar "0.5.16"]
                                  [cider/piggieback "0.3.9"]]
                   :plugins [[lein-figwheel "0.5.16"]]
                   :figwheel {:css-dirs ["resources/public/css"]
                              :ring-handler server/handler
                              :server-logfile "target/figwheel_temp/logs/figwheel_server.log"}}

             :uberjar {:aot :all
                       :uberjar-exclusions [#"resources/public/.*"
                                            #"target/.*"
                                            #"dev"
                                            #"src/turtle/client/.*"
                                            #"src/turtle/styles/.*"
                                            #"node_modules/.*"
                                            #"package.json"
                                            #"package-lock.json"
                                            #"firebase.json"
                                            #".git"
                                            #".firebase"]
                       :uberjar-name "turtle.jar"
                       :auto-clean false}}

  :garden {:builds [{:id "dev"
                     :source-paths ["src/turtle"]
                     :stylesheet styles.core/app
                     :compiler {:output-to "resources/public/css/compiled/app.css"
                                :pretty-print? true}}
                    {:id "min"
                     :source-paths ["src/turtle"]
                     :stylesheet styles.core/app
                     :compiler {:output-to "resources/public/css/compiled/app.css"
                                :pretty-print? false}}]}

  :cljsbuild {:builds [{:id "dev"
                        :source-paths ["src/turtle"]
                        :figwheel {:on-jsload "client.core/mount-root"}
                        :compiler {:main client.core
                                   :output-to "resources/public/js/compiled/app.js"
                                   :output-dir "resources/public/js/compiled/out"
                                   :asset-path "/js/compiled/out"
                                   :source-map-timestamp true
                                   :preloads [devtools.preload]
                                   :external-config {:devtools/config {:features-to-install :all}}}}
                       {:id "min"
                        :source-paths ["src/turtle"]
                        :compiler {:main client.core
                                   :output-to "resources/public/js/compiled/app.js"
                                   :optimizations :advanced
                                   :closure-defines {goog.DEBUG false}
                                   :pretty-print false}}]})
