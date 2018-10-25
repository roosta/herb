(ns herb-demo.snippets.my-group
  (:require [herb.core :refer-macros [<class defgroup]]
            [garden.units :refer [px]]))

;; Here we pick out a component based on a passed key, and modify that styles
;; metadata to include :key and :group.
(defgroup my-group
  {:container {:display :flex}
   :component-1
   {:background-color :black
    :width (px 50)
    :height (px 50)}
   :component-2
   {:background-color :grey
    :width (px 50)
    :height (px 50)}})

(defn component
  []
  [:div {:class (<class my-group :container)}
   [:div {:class (<class my-group :component-1)}]
   [:div {:class (<class my-group :component-2)}]])
