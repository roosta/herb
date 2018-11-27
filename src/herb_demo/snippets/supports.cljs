(ns herb-demo.snippets.supports
  (:require [herb.core :refer-macros [<class]]))

(defn style
  []
  ^{:supports {{:display :grid} {:display :grid}}}
  {:display "flex"})

(defn component
  []
  [:div {:class (<class style)}])
