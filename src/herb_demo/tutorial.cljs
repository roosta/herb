(ns herb-demo.tutorial
  (:require
   [herb-demo.components.container :refer [container]]
   [herb-demo.components.navigation :as nav]
   [herb-demo.components.text :refer [text]]
   [herb-demo.easing :as easing]
   [herb-demo.tutorials.extending :as extending]
   [herb-demo.tutorials.feature-queries :as feature-queries]
   [herb-demo.tutorials.fn-vars :as fn-vars]
   [herb-demo.tutorials.group-meta :as group-meta]
   [herb-demo.tutorials.intro :as intro]
   [herb-demo.tutorials.key-meta :as key-meta]
   [herb-demo.tutorials.keyframes :as keyframes]
   [herb-demo.tutorials.media-queries :as media-queries]
   [herb-demo.tutorials.metadata :as metadata]
   [herb-demo.tutorials.pseudo :as pseudo]
   [herb-demo.tutorials.why-fns :as why-fns]
   [herb.core :as herb :refer-macros [<class defgroup]]))

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
     [intro/main]
     [why-fns/main]
     [metadata/main]
     [pseudo/main]
     [media-queries/main]
     [extending/main]
     [feature-queries/main]
     [keyframes/main]
     [key-meta/main]
     [group-meta/main]
     [fn-vars/main]]]])
