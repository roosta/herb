(ns herb-demo.snippets.media-queries
  (:require [herb.core :refer-macros [<class]]
            [garden.units :refer [em rem]]))

(defn style
  []
  ^{:media {{:screen :only :min-width (em 32)} {:width (rem 33)}
            {:screen :only :min-width (em 52)} {:width (rem 53)}}}
  {:margin-right "auto"
   :display "flex"
   :flex-wrap "wrap"
   :margin-left "auto"})


(defn component []
  [:div {:class (<class style)}
   [:span "content"]])
