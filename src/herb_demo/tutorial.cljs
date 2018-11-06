(ns herb-demo.tutorial
  (:require [garden.units :refer [em px rem]]
            [herb-demo.components.container :refer [container]]
            [herb-demo.components.paper :refer [paper]]
            [herb-demo.components.text :refer [text]]
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
               :margin-bottom (px 10)}})

(defn header
  []
  [:div {:class (<class header-style :container)}
   [text {:align :center
          :variant :display}
    "Herb"]
   [text {:align :center
          :variant :headline}
    "Clojurescript styling using functions"]])

(defgroup main-style
  (let [nav-width 256]
    {:root {}
     :content {:padding-left (px nav-width)}
     :nav {:position "fixed"
           :overflow-y "auto"
           :width (px nav-width)
           :height "100%"}}))

(defn main []
  [:div {:class (<class main-style :root)}
   [:div {:class (<class main-style :nav)}
    [nav/sidebar]]
   [:div {:class (<class main-style :content)}
    [container
     [header]
     [intro]
     [why-fns]
     [extending]
     [key-meta]
     [group-meta]]]])
