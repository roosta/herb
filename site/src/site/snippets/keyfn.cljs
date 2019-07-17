(ns site.snippets.keyfn
  (:require [herb.core :refer [<class]]
            [garden.units :refer [px]]))

;; Here we create a style function that takes an argument "color" Note the use
;; of the meta data {:key color}
(defn style
  [color]
  ^{:key color}
  {:background-color color
   :width (px 50)
   :height (px 50)})

;; inline divs
(defn flex-container
  []
  {:display :flex})

(defn component
  []
  (let [colors ["red" "green" "blue"]]
    [:div {:class (<class flex-container)}
     (for [c colors]
       [:div {:key c
              :class (<class style c)}])]))
