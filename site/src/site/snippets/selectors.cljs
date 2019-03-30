(ns site.snippets.selectors
  (:require [herb.core :refer-macros [<class]]))

(defn selectors []
  ^{:combinators {[:> :div :span] {:background :red}
                  [:- :p] {:margin 0
                           :background :green}}}
  {:background :blue
   :color :white})

(defn component
  []
  [:div
   [:div {:class (<class selectors)}
    [:div
     [:span "Child"]]]
   [:p "Sibling"]]
  )
