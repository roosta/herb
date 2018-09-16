(ns herb-demo.components.container
  (:require [herb.core :refer [<class]]
            [garden.units :refer [rem em px]]
            [reagent.core :as r]))

(defn container-style
  []
  ^{:media {{:screen :only :min-width (em 32)} {:width (rem 33)}
            {:screen :only :min-width (em 52)} {:width (rem 53)}}}
  {:margin-right "auto"
   :display "flex"
   :flex-wrap "wrap"
   :margin-left "auto"})

(defn container
  []
  (into [:div {:class (<class container-style)}]
        (r/children (r/current-component))))
