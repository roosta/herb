(ns site.snippets.selectors
  (:require [herb.core :refer-macros [<class]]))

(defn selectors []
  ^{:combinators {[:> :div :span] {:background :red}
                  [:- :.sibling] {:background :green}}}
  {:background :blue
   :color :white})

(defn component
  []
  [:div
   [:div {:class (<class selectors)}
    [:div
     [:span "Child"]]]
   [:div {:class "sibling"} "Sibling"]])
