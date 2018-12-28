(ns site.snippets.my-group
  (:require [herb.core :refer-macros [<class defgroup]]
            [garden.units :refer [px]]))

;; Same as the previous example, defgroup takes a name and a map of styles, and
;; when used as a function for <class its first argument is the component
;; classname you want out of the group.
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
