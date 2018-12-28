(ns site.snippets.intro
  (:require
   [herb.core :refer-macros [<class <id]]))

;; Define a function that returns a map containing styles.
(defn example-style
  []
  {:background-color "#FF9999"
   :height "50px"
   :width "100%"})

(defn example-component
  []
  ;; using either <class or <id, the macros both take a function, and returns a
  ;; unique classname or id that we attach to our div here.
  [:div {:class (<class example-style)}])
