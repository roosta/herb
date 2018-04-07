(defproject herb "0.5.0-SNAPSHOT"
  :description "Clojurescript styling library that tries to mix functional programming with CSS"
  :url "https://github.com/roosta/herb"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}

  :plugins [[lein-cljsbuild "1.1.7"]
            [lein-codox "0.10.3"]]

  :source-paths ["src"]

  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/clojurescript "1.9.473"]
                 [philoskim/debux-stubs "0.4.5"]
                 [garden "1.3.3"]]

  :profiles {:dev {:dependencies [[philoskim/debux "0.4.5"]
                                  [etaoin "0.2.8-SNAPSHOT"]]
                   :source-paths ["test"]
                   :plugins [[lein-doo "0.1.8"]]}})
