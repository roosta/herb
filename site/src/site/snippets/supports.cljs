(ns site.snippets.supports
  (:require [herb.core :refer [<class]]))

(defn style
  []
  ^{:supports {{:display :grid} {:display :grid}}}
  {:display "flex"})

(defn component
  []
  [:div {:class (<class style)}])
