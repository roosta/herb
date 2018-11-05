(ns herb-demo.tutorial
  (:require [garden.units :refer [em px rem]]
            [herb-demo.components.container :refer [container]]
            [herb-demo.components.paper :refer [paper]]
            [herb-demo.components.text :refer [text]]
            [herb-demo.nav :refer [nav]]
            [herb-demo.tutorials.intro :refer [intro]]
            [herb-demo.tutorials.key-meta :refer [key-meta]]
            [herb-demo.tutorials.extending :refer [extending]]
            [herb-demo.tutorials.group-meta :refer [group-meta]]
            [herb-demo.tutorials.why-fns :refer [why-fns]]
            [herb.core :as herb :refer-macros [<class <id defgroup]]
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
    "Clojurescript styling using functions"]])

(defgroup main-style
  {:root {:display :flex}
   :window {:height "100vh"
            :width "100%"
            :overflow-y "auto"}
   :nav {:flex-basis "15%"
         :overflow-y "auto"
         :height "100vh"}})

(defn main []
  (let [state (r/atom "green")]
    (fn []
      [:div {:class (<class main-style :root)}
       [:div {:class (<class main-style :nav)}
        [nav]]
       [:div {:class (<class main-style :window)}
        [container
         [header]
         [intro]
         [why-fns]
         [extending]
         [key-meta]
         [group-meta]]]])))
