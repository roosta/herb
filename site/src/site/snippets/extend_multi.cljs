(ns site.snippets.extend_multi
  (:require [herb.core :refer [<class]]
            [garden.units :refer [px]]))

(defn button
  []
  {:display "inline-block"
   :text-transform "uppercase"
   :cursor "pointer"
   :padding (px 12)})

(defn text-color
  [color]
  {:color color})

(defn red-button
  []
  ^{:extend [[button] [text-color "black"]]}
  {:background-color "#ff796d"})

(defn blue-button
  []
  ^{:extend [[button] [text-color "white"]]}
  {:background-color "#1693a5"})

(defn container
  []
  {:display :flex})

(defn component
  []
  [:div {:class (<class container)}
   [:button {:class (<class red-button)}
    "Red"]
   [:button {:class (<class blue-button)}
    "Blue"]])
