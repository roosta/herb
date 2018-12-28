(ns site.snippets.keyframes-use
  (:require
   [garden.units :refer [px]]
   [herb.core :refer-macros [<class defkeyframes]]))

(defkeyframes pulse-animation
  [:from {:opacity 1}]
  [:to {:opacity 0}])

;; In garden, if you use a single vector [arg1 arg1] you get a comma separated
;; string, if you add a second vector is gets space separated
(defn style
  []
  {:animation [[pulse-animation "2s" :infinite :alternate]]
   :background-color "black"
   :transition "all 1s ease-out"
   :width (px 20)
   :height (px 20)})

(defn component
  []
  [:div {:class (<class style)}])
