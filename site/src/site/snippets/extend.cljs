(ns site.snippets.extend
  (:require [herb.core :refer [<class]]
            [garden.units :refer [px]]))

(defn button
  []
  {:display "inline-block"
   :color "white"
   :text-transform "uppercase"
   :cursor "pointer"
   :padding (px 12)})

(defn red-button
  []
  ^{:extend button}
  {:background-color "#F02311"})

(defn blue-button
  []
  ^{:extend button}
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
