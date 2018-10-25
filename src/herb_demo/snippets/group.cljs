(ns herb-demo.snippets.group
  (:require [herb.core :refer-macros [<class]]
            [garden.units :refer [px]]))

;; Adding {:group true} to a keyed style, groups results in the same <style>
;; element
(defn style
  [color]
  ^{:key color
    :group true}
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
