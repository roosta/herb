(defproject site "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}

  :dependencies [[org.clojure/clojure "1.9.0"]
                 [org.clojure/clojurescript "1.10.312"]
                 [com.taoensso/tufte "2.0.1"]
                 [garden "1.3.5"]
                 [reagent "0.8.1"]]

  :plugins [[lein-cljsbuild "1.1.7"]
            [lein-figwheel "0.5.16"]]

  :min-lein-version "2.5.0"

  :clean-targets ^{:protect false}
  [:target-path
   [:cljsbuild :builds :app :compiler :output-dir]
   [:cljsbuild :builds :app :compiler :output-to]
   [:cljsbuild :builds :release :compiler :output-dir]
   [:cljsbuild :builds :release :compiler :output-to] ]

  :resource-paths ["public"]

  :source-paths ["src" "../src"]

  :figwheel {:http-server-root "."
             :nrepl-port 7002
             :nrepl-middleware ["cemerick.piggieback/wrap-cljs-repl"]}

  :cljsbuild {:builds {:app
                       {:source-paths ["src" "../src" "env/dev"]
                        :compiler
                        {:main "site.dev"
                         :output-to "public/js/app.js"
                         :output-dir "public/js/out"
                         :asset-path   "js/out"
                         :source-map true
                         :optimizations :none
                         :pretty-print  true}
                        :figwheel
                        {:on-jsload "site.core/mount-root"}}
                       :release
                       {:source-paths ["src" "../src" "env/prod"]
                        :compiler
                        {:output-to "public/js/app.js"
                         :output-dir "public/js/release"
                         :closure-defines {"goog.DEBUG" false}
                         :optimizations :advanced
                         :pretty-print false}}}}

  :aliases {"package" ["do" "clean" ["cljsbuild" "once" "release"]]}

  :profiles {:dev {:dependencies [[binaryage/devtools "0.9.10"]
                                  [figwheel-sidecar "0.5.16"]
                                  [philoskim/debux "0.4.8"]
                                  [org.clojure/tools.nrepl "0.2.13"]
                                  [com.cemerick/piggieback "0.2.2"]]}})
