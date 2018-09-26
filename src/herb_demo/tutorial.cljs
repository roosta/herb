(ns herb-demo.tutorial
  (:require [garden.units :refer [em px rem]]
            [herb-demo.components.container :refer [container]]
            [herb-demo.components.paper :refer [paper]]
            [herb-demo.components.text :refer [text]]
            [herb-demo.examples.intro :as intro]
            [herb-demo.tutorials.css-garden :refer [css-garden]]
            [herb-demo.tutorials.intro :refer [intro]]
            [herb-demo.tutorials.why-fns :refer [why-fns]]
            [herb.core :as herb :refer-macros [<class <id]]
            [reagent.core :as r]
            [reagent.debug :as d])
  (:require-macros [herb-demo.macros :as macros]))

(defn header-style
  [component]
  (let [styles {:container {:flex-basis "100%"
                            :margin-bottom (px 10)}}]
    (with-meta (component styles) {:key component})))

(defn header
  []
  [:div {:class (<class header-style :container)}
   [text {:align :center
          :variant :display}
    "Herb"]
   [text {:align :center
          :variant :headline}
    "Clojurescript styling library site"]])

(defn tutorial []
  (let [state (r/atom "green")]
    (fn []
      [container
       [header]
       [intro]
       [why-fns]
       [css-garden]])))
