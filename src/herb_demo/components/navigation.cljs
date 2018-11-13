(ns herb-demo.components.navigation
  (:require [garden.units :refer [px rem]]
            [goog.events :as events]
            [debux.cs.core :as d :refer-macros [clog clogn dbg dbgn break]]
            [clojure.string :as str]
            [herb-demo.easing :as easing]
            [garden.color :refer [rgba]]
            [goog.dom :as dom]
            [goog.events.EventType :as event-type]
            [herb-demo.components.text :refer [text]]
            [cljsjs.waypoints]
            [herb.core :refer-macros [<class defgroup]]
            [reagent.core :as r])
  (:require-macros [garden.def :refer [defcssfn]]))

(def appbar-height (rem 3))
(def sidebar-width (rem 16))

(def state (r/atom :intro))

(def items {:intro {:label "Introduction"}
            :why-fns {:label "Why functions?"}
            :extending {:label "Extending style functions"}
            :key-meta {:label "The key matadata"}
            :group-meta {:label "The group metadata"
                         :sub {:defgroup {:label "defgroup macro"}}}})

(defn create-waypoints
  []
  (into {}
        (map (fn [[k v]]
               (let [el (dom/getElement (name k))]
                 {k (js/Waypoint. #js {:element el
                                       :handler #(reset! state k)})}))
             items)))

(defgroup sidebar-style
  {:root {:background "#333"
          :position "fixed"
          :overflow-y "auto"
          :width sidebar-width
          :height "100%"
          :color "white"}
   :container {:padding (rem 1)}
   :row {:padding-bottom (rem 1)}})

(defn nav-item-style [padding?]
  ^{:pseudo {:hover {:color "#3BABFF"}}
    :key padding?}
  {:color "white"
   :padding-left (if padding? (rem 1) 0)})

(defn nav-item
  [[k v] index]
  (let [sub? (> index (count items))
        strindex (str index)]
    [:div  {:class (<class sidebar-style :row)}
     [:a {:href (str "#" (name k))}
      [text {:class (<class nav-item-style sub?)
             :color :white
             :variant :a}
       [:strong (if sub?
                  (str (first strindex) "." (apply str (rest strindex)) ". ")
                  (str index ". "))]
       (:label v)]]]))

(defn create-nav
  [items index result]
  (if (seq items)
    (if-let [sub (-> items first val :sub)]
      (recur (rest items)
             (inc index)
             (into result (create-nav sub (+ (* 10 (dec index)) 1) (list))))
      (recur (rest items)
             (inc index)
             (conj result ^{:key index} [nav-item (first items) index])))
    (reverse result)))

(defn sidebar []
  (r/create-class
   {:component-did-mount create-waypoints
    :reagent-render
    (fn []
      [:section {:class (<class sidebar-style :root)}
       [:nav {:class (<class sidebar-style :container)}
        (create-nav items 1 (list))]])}))

(defcssfn calc
  [& args]
  [args])

(defgroup appbar-style
  {:root {:position #{"-webkit-sticky" "sticky" }
          :background "#eee"
          :align-items "center"
          :display "flex"
          :top 0
          :height appbar-height}
   :column {:flex-basis "33%"}})

(defn divider-style
  [scroll?]
  {:transition (str "opacity 400ms " (:ease-in-out-quad easing/easing))
   :width "100%"
   :position "absolute"
   :bottom 0
   :height (px 1)
   :background (rgba 0 0 0 0.15)
   :opacity (if scroll? 1 0)})

(defn on-scroll
  [state e]
  (if (> (.-y (dom/getDocumentScroll)) 0)
    (reset! state true)
    (reset! state false)))

(defn appbar
  []
  (let [scroll? (r/atom nil)]
    (r/create-class
     {:component-did-mount (fn []
                             (events/listen js/document
                                            event-type/SCROLL
                                            #(on-scroll scroll? %)))
      :reagent-render
      (fn []
        [:header {:class (<class appbar-style :root)}
         [:div {:class (<class appbar-style :column)}]
         [:div {:class (<class appbar-style :column)}
          [text {:variant :title
                 :margin :none
                 :align :center}
           "Herb"]]
         [:div {:class (<class appbar-style :column)}]
         [:div {:class (<class divider-style @scroll?)}]])})))
