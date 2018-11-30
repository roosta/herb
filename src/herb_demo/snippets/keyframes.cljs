(ns herb-demo.snippets.keyframes
  (:require [herb.core :refer-macros [<class defkeyframes]]))

(defkeyframes pulse-animation
  [:from {:opacity 1}]
  [:to {:opacity 0}])

(defkeyframes color-change
  ["0%" {:background "yellow"}]
  ["50%" {:background "orange"}]
  ["100%" {:background "orange"}])
