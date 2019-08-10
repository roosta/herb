(defproject herb "0.10.1-SNAPSHOT"
  :description "ClojureScript styling using functions"
  :url "https://github.com/roosta/herb"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}

  ;; :plugins [[lein-codox "0.10.7"]]

  :source-paths ["src"]

  ;; :codox {:language :clojurescript
  ;;         :metadata {:doc/format :markdown}
  ;;         :output-path "docs"
  ;;         :source-paths ["src"]}

  :min-lein-version "2.5.0"

  :clean-targets ^{:protect false} ["target"]

  :resource-paths ["target" "resources"]

  :jar-exclusions [#"(?:^|\/)public\/"]

  :dependencies [[org.clojure/clojure "1.10.1" :scope "provided"]
                 [org.clojure/clojurescript "1.10.520" :scope "provided"]
                 [garden "1.3.9"]]
  :profiles {:dev {:dependencies [[com.bhauman/figwheel-main "0.2.1"]
                                  [reagent "0.8.1"]
                                  [philoskim/debux "0.5.6"]]
                   :aliases {"fig" ["trampoline" "run" "-m" "figwheel.main"]
                             "fig:test" ["run" "-m" "figwheel.main" "-co" "test.cljs.edn" "-m" herb.test-runner]
                             "fig:build" ["trampoline" "run" "-m" "figwheel.main" "-b" "dev" "-r"]
                             "fig:min"   ["run" "-m" "figwheel.main" "-O" "advanced" "-bo" "dev"]
                             "fig:benchmark" ["run" "-m" "figwheel.main" "-co" "benchmark.cljs.edn" "-m" herb.benchmark-runner]}
                   :source-paths ["test" "demo" "benchmark"]}})
