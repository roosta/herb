(defproject herb "0.3.3"
  :description "Clojurescript styling library that tries to mix functional programming with CSS"
  :url "https://github.com/roosta/herb"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}

  :plugins [[lein-cljsbuild "1.1.7"]
            [lein-doo "0.1.8"]
            [lein-codox "0.10.3"]]

  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/clojurescript "1.9.473"]
                 [garden "1.3.3"]]

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
