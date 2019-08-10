(ns site.tutorial
  (:require [garden.units :refer [em px rem]]
            [goog.dom :as dom]
            [site.components.container :refer [container]]
            [site.components.navigation :as nav]
            [goog.events.EventType :as event-type]
            [site.components.text :as text]
            [site.observer :refer [create-observer]]
            [goog.events :as events]
            [reagent.core :as r]
            [site.async :as async]
            [site.easing :as easing]
            [site.tutorials.examples :as examples]
            [site.tutorials.advanced-compile :as adv]
            [site.tutorials.extending :as extending]
            [site.tutorials.feature-queries :as feature-queries]
            [site.tutorials.fn-vars :as fn-vars]
            [site.tutorials.global :as global]
            [site.tutorials.defgroup :as defgroup]
            [site.tutorials.intro :as intro]
            [site.tutorials.keyframes :as keyframes]
            [site.tutorials.media-queries :as media-queries]
            [site.tutorials.metadata :as metadata]
            [site.tutorials.pseudo :as pseudo]
            [site.tutorials.vendor-prefixes :as vendor-prefixes]
            [site.tutorials.why-fns :as why-fns]
            [site.tutorials.selectors :as selectors]
            [herb.core :as herb :refer [<class defgroup]]))


(defn image-style []
  {:flex-basis "100%"
   :text-align :center
   :padding-bottom "1rem"}
  )

(def sticky? (r/atom 0))

(defgroup title-style
  {:container {:position "sticky"
               :flex-basis "100%"
               :top (px 3)}
   :text {:transition (str "font-size 200ms " (:ease-out-quad easing/easing))
          :color "#0b486b"
          :font-size (if @sticky? (text/px->rem 34) (text/px->rem 52) )
          }})


(defn break-style []
  {:height "1px"
   :visibility "hidden"})

(defn break [id state]
  (let [observer (create-observer state)]
    (r/create-class
     {:component-did-mount #(.observe observer (.querySelector js/document (str "#" id)))
      :reagent-render
      (fn []
        [:div {:id id
               :class (<class break-style)}])})))

(defn title []
  [:div {:class (<class title-style :container)}
   [text/text {:variant :title
               :class (<class title-style :text)
               :align :center}
    "Herb"]])

(defn logo
  []
  [:div {:class (<class image-style)}
   [:img {:height "400" :src "assets/herb.svg"}]])

(defn subheading-style
  []
  {:flex-basis "100%"})

(defn subheading []
  [text/text {:align :center
         :class (<class subheading-style)
         :variant :subtitle}
   "Clojurescript styling using functions"]
  )

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
    #_[:div#top {:class (<class main-style :spacer)}]
    [nav/appbar]
    [break "appbar-break" nav/sticky?]
    [container
     [logo]
     [title]
     [break "title-break" sticky?]
     [subheading]
     [intro/main]
     [why-fns/main]
     [metadata/main]
     [pseudo/main]
     [media-queries/main]
     [extending/main]
     [defgroup/main]
     [feature-queries/main]
     [global/main]
     [selectors/main]
     [vendor-prefixes/main]
     [keyframes/main]
     [fn-vars/main]
     [adv/main]
     [examples/main]]]])
