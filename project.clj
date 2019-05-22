(defproject herb "0.8.2-SNAPSHOT"
  :description "ClojureScript styling using functions"
  :url "https://github.com/roosta/herb"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}

  :plugins [[lein-cljsbuild "1.1.7"]
            [lein-codox "0.10.6"]]

  :source-paths ["src"]

  :codox {:language :clojurescript
          :metadata {:doc/format :markdown}
          :output-path "docs"
          :source-paths ["src"]}

  :min-lein-version "2.5.0"

  :clean-targets ^{:protect false} ["resources/public/js" "target"]

  ;; Exclude the demo,site and compiled files from the output of either 'lein jar' or 'lein install'
  :jar-exclusions [#"(?:^|\/)demo\/" #"(?:^|\/)public\/"]

  :dependencies [[org.clojure/clojure "1.10.0" :scope "provided"]
                 [org.clojure/clojurescript "1.10.520" :scope "provided"]
                 [garden "1.3.9"]]

  :profiles {:dev {:dependencies [[binaryage/devtools "0.9.10"]
                                  [org.clojure/test.check "0.10.0-alpha3"]
                                  [figwheel "0.5.18"]
                                  [philoskim/debux "0.5.5"]
                                  [reagent "0.8.1"]
                                  [com.taoensso/tufte "2.0.1"]
                                  [figwheel-sidecar "0.5.18"]
                                  [etaoin "0.3.2"]]
                   :plugins [[lein-figwheel "0.5.18"]]
                   :source-paths ["test" "demo"]}
             :dev-cider {:dependencies [[cider/piggieback "0.4.0"]]
                         :figwheel {:nrepl-middleware [cider.piggieback/wrap-cljs-repl]}}}

  :cljsbuild {:builds [{:id "demo"
                        :source-paths ["demo"]
                        :figwheel true
                        :watch-paths ["src" "demo" "test"]
                        :compiler {:main "herbdemo.dev"
                                   :output-to "resources/public/js/demo.js"
                                   :preloads [devtools.preload]
                                   :output-dir "resources/public/js/out"
                                   :asset-path   "js/out"
                                   :source-map true
                                   :optimizations :none
                                   :pretty-print  true}}

                       {:id "demo-release"
                        :source-paths ["demo"]
                        :compiler {:main "herbdemo.prod"
                                   :output-to "resources/public/js/demo.js"
                                   :output-dir "resources/public/js/release"
                                   :closure-defines {"goog.DEBUG" false}
                                   ;; :psudo-names true
                                   :optimizations :advanced
                                   :pretty-print false}}]})
