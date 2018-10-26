(ns herb-demo.snippets.extend-args
  (:require [herb.core :refer-macros [<class]]
            [garden.units :refer [px]]))

(defn button
  [text-color]
  {:display "inline-block"
   :color text-color
   :text-transform "uppercase"
   :cursor "pointer"
   :padding (px 12)})

(defn red-button
  []
  ^{:extend [button "black"]}
  {:background-color "#ff796d"})

(defn blue-button
  []
  ^{:extend [button "white"]}
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
