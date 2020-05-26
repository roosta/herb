(ns user
  (:require [figwheel.main.api]))

(defn start-fw []
  (figwheel.main.api/start {:mode :serve} "dev"))

(defn cljs []
  (figwheel.main.api/cljs-repl "dev"))
