(ns site.snippets.keyframes
  (:require [herb.core :refer [<class defkeyframes]]))

(defkeyframes pulse-animation
  [:from {:opacity 1}]
  [:to {:opacity 0}])

(defkeyframes color-change
  ["0%" {:background "yellow"}]
  ["50%" {:background "orange"}]
  ["100%" {:background "orange"}])
