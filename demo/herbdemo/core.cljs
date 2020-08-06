(ns herbdemo.core
  (:require [garden.units :refer [px em rem em]]
            [herb.core :refer [<class] :as herb]
            [herbdemo.examples :as examples]
            [reagent.dom :as dom]) )

(defn appframe []
  [examples/main])

(defn mount-root []
  (dom/render [appframe] (.getElementById js/document "demo")))

(defn init!
  []
  (herb/init!
   {:vendors ["webkit" :moz]
    :auto-prefix #{:transition :animation}})
  (mount-root))

(init!)
