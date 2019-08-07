(defproject site "0.1.0-SNAPSHOT"
  :description "Herb website"
  :url "http://herb.roosta.sh"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}

  :dependencies [[org.clojure/clojure "1.10.0"]
                 [ring-server "0.5.0"]
                 [reagent "0.8.1"]
                 [reagent-utils "0.3.2"]
                 [ring "1.7.1"]
                 [herb "0.10.0-SNAPSHOT"]
                 [ring/ring-defaults "0.3.2"]
                 [hiccup "1.0.5"]
                 [cljsjs/highlight "9.12.0-2"]
                 [yogthos/config "1.1.1"]
                 [org.clojure/clojurescript "1.10.520"]
                 [metosin/reitit "0.2.13"]
                 [pez/clerk "1.0.0"]
                 [venantius/accountant "0.2.4"
                  :exclusions [org.clojure/tools.reader]]]

  :plugins [[lein-environ "1.1.0"]
            [lein-cljsbuild "1.1.7"]
            [lein-asset-minifier "0.4.5"
             :exclusions [org.clojure/clojure]]]

  :ring {:handler site.handler/app
         :uberwar-name "site.war"}

  :min-lein-version "2.5.0"
  :uberjar-name "site.jar"
  :main site.server
  :clean-targets ^{:protect false}
  [:target-path
   [:cljsbuild :builds :app :compiler :output-dir]
   [:cljsbuild :builds :app :compiler :output-to]]

  :source-paths ["src"]
  :resource-paths ["resources" "target/cljsbuild"]

  :minify-assets [[:css {:source "resources/public/css/atelier-forest-light.css"
                         :target "resources/public/css/atelier-forest-light.min.css"}]]

  :cljsbuild {:builds {:min {:source-paths ["src" "env/prod"]
                             :compiler {:output-to "target/cljsbuild/public/js/app.js"
                                        :output-dir "target/cljsbuild/public/js"
                                        :source-map "target/cljsbuild/public/js/app.js.map"
                                        :closure-defines {"goog.DEBUG" false}
                                        ;; :pseudo-names true
                                        :optimizations :advanced
                                        :pretty-print  false}}
                       :app {:source-paths ["src" "env/dev"]
                             :figwheel {:on-jsload "site.core/mount-root"}
                             :compiler {:main "site.dev"
                                        :asset-path "/js/out"
                                        :output-to "target/cljsbuild/public/js/app.js"
                                        :output-dir "target/cljsbuild/public/js/out"
                                        :source-map true
                                        :optimizations :none
                                        :pretty-print  true}}}}

  :figwheel {:http-server-root "public"
             :server-port 3450
             :nrepl-port 7002
             :nrepl-middleware [cider.piggieback/wrap-cljs-repl]
             :css-dirs ["resources/public/css"]
             :ring-handler site.handler/app}

  :profiles {:dev {:repl-options {:init-ns site.repl}
                   :dependencies [[cider/piggieback "0.4.0"]
                                  [binaryage/devtools "0.9.10"]
                                  [prone "1.6.1"]
                                  [ring/ring-mock "0.3.2"]
                                  [ring/ring-devel "1.7.1"]
                                  [pjstadig/humane-test-output "0.9.0"]
                                  [figwheel-sidecar "0.5.18"]
                                  [nrepl "0.6.0"]]
                   :source-paths ["env/dev"]
                   :injections [(require 'pjstadig.humane-test-output)
                                (pjstadig.humane-test-output/activate!)]
                   :plugins [[lein-figwheel "0.5.18"]]
                   :env {:dev true}}

             :uberjar {:hooks [minify-assets.plugin/hooks]
                       :source-paths ["env/prod"]
                       :prep-tasks ["compile" ["cljsbuild" "once" "min"]]
                       :env {:production true}
                       :aot :all
                       :omit-source true}})
