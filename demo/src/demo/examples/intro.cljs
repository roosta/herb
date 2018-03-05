(ns demo.examples.intro
  (:require
   [herb.core :refer [<class]]))

(defn example-style
  []
  {:background-color "#FF9999"
   :height "50px"
   :width "100%"})

(defn example
  []
  [:div {:class (<class example-style)}])
