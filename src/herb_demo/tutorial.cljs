(ns herb-demo.tutorial
  (:require
   [herb.core :refer-macros [<class <id] :as herb]
   [herb-demo.components.text :refer [text]]
   [herb-demo.examples.intro :as intro]
   [herb-demo.components.container :refer [container]]
   [herb-demo.intro :refer [intro]]
   [herb-demo.css-garden :refer [css-garden]]
   [herb-demo.why-fns :refer [why-fns]]
   [herb-demo.components.paper :refer [paper]]
   [garden.units :refer [rem em px]]
   [reagent.debug :as d]
   [reagent.core :as r])
  (:require-macros
   [herb-demo.macros :as macros]))

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

(defn site []
  (let [state (r/atom "green")]
    (fn []
      [container
       [header]
       [intro]
       [why-fns]
       [css-garden]])))
