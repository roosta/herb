(defproject site "0.1.0-SNAPSHOT"
  :description "Herb website"
  :url "http://herb.roosta.sh"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}

  :dependencies [[org.clojure/clojure "1.10.3"]
                 [ring-server "0.5.0"]
                 [reagent "1.1.0"]
                 [reagent-utils "0.3.4"]
                 [ring "1.9.4"]
                 [herb "0.10.0"]
                 [cljsjs/react "17.0.2-0"]
                 [cljsjs/react-dom "17.0.2-0"]
                 [ring/ring-defaults "0.3.3"]
                 [hiccup "1.0.5"]
                 [cljsjs/highlight "10.3.1-0"]
                 [yogthos/config "1.1.8"]
                 [org.clojure/clojurescript "1.10.879"]
                 [metosin/reitit "0.5.15"]
                 [pez/clerk "1.0.0"]
                 [venantius/accountant "0.2.5"
                  :exclusions [org.clojure/tools.reader]]]

  :plugins [[lein-environ "1.2.0"]
            [lein-cljsbuild "1.1.8"]
            [lein-asset-minifier "0.4.6"
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
                   :dependencies [[cider/piggieback "0.5.2"]
                                  [binaryage/devtools "1.0.3"]
                                  [prone "2021-04-23"]
                                  [ring/ring-mock "0.4.0"]
                                  [ring/ring-devel "1.9.4"]
                                  [pjstadig/humane-test-output "0.11.0"]
                                  [figwheel-sidecar "0.5.20"]
                                  [nrepl "0.8.3"]]
                   :source-paths ["env/dev"]
                   :injections [(require 'pjstadig.humane-test-output)
                                (pjstadig.humane-test-output/activate!)]
                   :plugins [[lein-figwheel "0.5.20"]]
                   :env {:dev true}}

             :uberjar {:hooks [minify-assets.plugin/hooks]
                       :source-paths ["env/prod"]
                       :prep-tasks ["compile" ["cljsbuild" "once" "min"]]
                       :env {:production true}
                       :aot :all
                       :omit-source true}})
