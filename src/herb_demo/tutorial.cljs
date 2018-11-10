(ns herb-demo.tutorial
  (:require [garden.units :refer [em px rem]]
            [herb-demo.components.container :refer [container]]
            [herb-demo.components.paper :refer [paper]]
            [herb-demo.components.text :refer [text]]
            [herb-demo.easing :as easing]
            [herb-demo.components.navigation :as nav]
            [herb-demo.tutorials.intro :refer [intro]]
            [herb-demo.tutorials.key-meta :refer [key-meta]]
            [herb-demo.tutorials.extending :refer [extending]]
            [herb-demo.tutorials.group-meta :refer [group-meta]]
            [herb-demo.tutorials.why-fns :refer [why-fns]]
            [herb.core :as herb :refer-macros [<class <id defgroup]]
            [reagent.core :as r]
            [reagent.debug :as d])
  (:require-macros [herb-demo.macros :as macros]))

(defgroup header-style
  {:container {:flex-basis "100%"
               :margin-bottom (rem 0.625)}})

(defn header
  []
  [:header {:class (<class header-style :container)}
   [text {:align :center
          :variant :subtitle}
    "Clojurescript styling using functions"]])

(defgroup main-style
  {:root {}
   :content {:padding [[(rem 2) 0 (rem 3) 0]]
             :padding-left nav/sidebar-width}})

(defn main []
  [:main {:class (<class main-style :root)}
   [nav/sidebar]
   [:section {:class (<class main-style :content)}
    [nav/appbar]
    [container
     [header]
     [intro]
     [why-fns]
     [extending]
     [key-meta]
     [group-meta]]]])
