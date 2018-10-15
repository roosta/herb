(defproject herb "0.6.0"
  :description "Clojurescript styling library that tries to mix functional programming with CSS"
  :url "https://github.com/roosta/herb"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}

  :plugins [[lein-cljsbuild "1.1.7"]
            [lein-codox "0.10.5"]]

  :source-paths ["src"]

  :min-lein-version "2.5.0"

  :clean-targets ^{:protect false} ["resources/public/js" "target"]

  ;; Exclude the demo,site and compiled files from the output of either 'lein jar' or 'lein install'
  :jar-exclusions [#"(?:^|\/)herb_demo\/" #"(?:^|\/)public\/" #"server.clj"]

  :dependencies [[org.clojure/clojure "1.9.0" :scope "provided"]
                 [org.clojure/clojurescript "1.10.339" :scope "provided"]
                 [philoskim/debux-stubs "0.5.1"]
                 [org.clojure/tools.analyzer.jvm "0.7.2"]
                 [org.clojure/tools.analyzer "0.6.9"]
                 [garden "1.3.6"]]

  :figwheel {:css-dirs ["resources/public/css"]
             :ring-handler server/app}

  :aliases {"demo-release" ["with-profile" "+demo" "cljsbuild" "once" "demo-release"]}

  :profiles {:dev {:dependencies [[philoskim/debux "0.5.1"]
                                  [binaryage/devtools "0.9.10"]
                                  [compojure "1.6.1"]
                                  [ring "1.7.0"]
                                  [ring/ring-defaults "0.3.2"]
                                  [figwheel "0.5.16"]
                                  [figwheel-sidecar "0.5.16"]
                                  [etaoin "0.2.9"]]
                   :plugins [[lein-figwheel "0.5.16"]]
                   :source-paths ["test" "dev"]}
             :dev-cider {:dependencies [[cider/piggieback "0.3.9"]]
                         :figwheel {:nrepl-middleware [cider.piggieback/wrap-cljs-repl]}}
             :demo {:dependencies [[com.taoensso/tufte "1.2.0"]
                                   [secretary "1.2.3"]
                                   [cljsjs/highlight "9.12.0-2"]
                                   [venantius/accountant "0.2.4"]
                                   [reagent "0.8.1"]]}}

  :cljsbuild {:builds [{:id "demo"
                        :source-paths ["src" "env/dev"]
                        :figwheel {:on-jsload "herb-demo.core/mount-root"}
                        :compiler {:main "herb-demo.dev"
                                   :output-to "resources/public/js/demo.js"
                                   :external-config {:devtools/config {:features-to-install [:formatters :hints]}}
                                   :output-dir "resources/public/js/out"
                                   :asset-path   "js/out"
                                   :source-map true
                                   :optimizations :none
                                   :pretty-print  true}}

                       {:id "demo-release"
                        :source-paths ["src" "env/release"]
                        :compiler {:output-to "resources/public/js/demo.js"
                                   :output-dir "resources/public/js/release"
                                   :closure-defines {"goog.DEBUG" false}
                                   :optimizations :advanced
                                   :pretty-print false}}]})
