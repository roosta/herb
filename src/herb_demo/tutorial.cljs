(ns herb-demo.tutorial
  (:require [garden.units :refer [em px rem]]
            [herb-demo.components.container :refer [container]]
            [herb-demo.components.navigation :as nav]
            [herb-demo.components.paper :refer [paper]]
            [herb-demo.components.text :refer [text]]
            [herb-demo.easing :as easing]
            [herb-demo.tutorials.extending :refer [extending]]
            [herb-demo.tutorials.fn-vars :refer [fn-vars]]
            [herb-demo.tutorials.group-meta :refer [group-meta]]
            [herb-demo.tutorials.intro :refer [intro]]
            [herb-demo.tutorials.metadata :refer [metadata]]
            [herb-demo.tutorials.key-meta :refer [key-meta]]
            [herb-demo.tutorials.pseudo :refer [pseudo]]
            [herb-demo.tutorials.why-fns :refer [why-fns]]
            [herb.core :as herb :refer-macros [<class <id defgroup]]
            [reagent.core :as r]
            [reagent.debug :as d])
  (:require-macros [herb-demo.macros :as macros]))

(defgroup header-style
  {:container {:flex-basis "100%"}})

(defn header
  []
  [:header {:class (<class header-style :container)}
   [text {:align :center
          :variant :subtitle}
    "Clojurescript styling using functions"]])

(defgroup main-style
  {:root {}
   :spacer {:height nav/appbar-height}
   :content {:transition (str "padding 400ms " (:ease-in-out-quad easing/easing))
             :padding-left (if @nav/sidebar-open?
                             nav/sidebar-width
                             0)}})

(defn main []
  [:main {:class (<class main-style :root)}
   [nav/sidebar]
   [:section {:class (<class main-style :content)}
    [:div#top {:class (<class main-style :spacer)}]
    [nav/appbar]
    [container
     [header]
     [intro]
     [why-fns]
     [metadata]
     [pseudo]
     [extending]
     [key-meta]
     [group-meta]
     [fn-vars]]]])
