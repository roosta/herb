(ns herb-demo.snippets.my-group-args
  (:require [herb.core :refer-macros [<class defgroup]]
            [garden.units :refer [px]]))

(defgroup my-group
  {:container {:display :flex}
   :component-1
   {:background-color (first args)
    :width (px 50)
    :height (px 50)}})

(defn component
  []
  [:div {:class (<class my-group :container)}
   [:div {:class (<class my-group :component-1 :black)}]])
