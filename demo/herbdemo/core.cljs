(ns herbdemo.core
  (:require [garden.units :refer [px em rem em]]
            [herb.core :refer-macros [<class]]
            [herbdemo.examples :as examples]
            [reagent.core :as r]) )

(defn appframe []
  [examples/main])

(defn mount-root []
  (r/render [appframe] (.getElementById js/document "demo")))

(defn init!
  []
  (mount-root))
