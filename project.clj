(defproject herb "0.4.0-SNAPSHOT"
  :description "Clojurescript styling library that tries to mix functional programming with CSS"
  :url "https://github.com/roosta/herb"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}

  :plugins [[lein-cljsbuild "1.1.7"]
            [lein-codox "0.10.3"]]

  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/clojurescript "1.9.473"]
                 [garden "1.3.3"]]

  :profiles {:dev {:dependencies [[philoskim/debux "0.4.3"]
                                  [etaoin "0.2.8-SNAPSHOT"]]
                   :plugins [[lein-doo "0.1.8"]]}}

  :source-paths ["src"]

  :cljsbuild {:builds [{:id "test"
                        :source-paths ["src" "test"]
                        :compiler {:output-to "target/cljs/test/test.js"
                                   :output-dir "target/cljs/test"
                                   :optimizations :none
                                   :pretty-print true
                                   :source-map true
                                   :main herb.runner}}
                       {:id "prod"
                        :source-paths ["src"]
                        :compiler {:output-to "herb.js"
                                   :optimizations :advanced}}]})
