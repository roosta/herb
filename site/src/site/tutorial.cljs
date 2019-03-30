(ns site.tutorial
  (:require [garden.units :refer [em px rem]]
            [site.components.container :refer [container]]
            [site.components.navigation :as nav]
            [site.components.text :refer [text]]
            [site.easing :as easing]
            [site.tutorials.examples :as examples]
            [site.tutorials.advanced-compile :as adv]
            [site.tutorials.extending :as extending]
            [site.tutorials.feature-queries :as feature-queries]
            [site.tutorials.fn-vars :as fn-vars]
            [site.tutorials.global :as global]
            [site.tutorials.group-meta :as group-meta]
            [site.tutorials.intro :as intro]
            [site.tutorials.key-meta :as key-meta]
            [site.tutorials.keyframes :as keyframes]
            [site.tutorials.media-queries :as media-queries]
            [site.tutorials.metadata :as metadata]
            [site.tutorials.pseudo :as pseudo]
            [site.tutorials.vendor-prefixes :as vendor-prefixes]
            [site.tutorials.why-fns :as why-fns]
            [site.tutorials.selectors :as selectors]
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
   :content
   ^{:media {{:screen :only :max-width (em 53)} {:width "100%"}}}
   {:transition (str "padding 400ms " (:ease-in-out-quad easing/easing))
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
     [key-meta/main]
     [group-meta/main]
     [feature-queries/main]
     [global/main]
     [selectors/main]
     [vendor-prefixes/main]
     [keyframes/main]
     [fn-vars/main]
     [adv/main]
     [examples/main]]]])
