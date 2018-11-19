(ns herb-demo.components.navigation
  (:require [garden.units :refer [px rem percent]]
            [goog.events :as events]
            [debux.cs.core :as d :refer-macros [clog clogn dbg dbgn break]]
            [clojure.string :as str]
            [herb-demo.components.icon :as icon]
            [herb-demo.easing :as easing]
            [garden.color :refer [rgba]]
            [goog.dom :as dom]
            [goog.events.EventType :as event-type]
            [herb-demo.components.text :refer [text]]
            [herb.core :refer-macros [<class defgroup]]
            [reagent.core :as r])
  (:require-macros [garden.def :refer [defcssfn]])
  (:import [goog.async Throttle]))

(def appbar-height (rem 3))
(defonce sidebar-open? (r/atom true))
(def sidebar-width (rem 16))
(def items {:intro {:label "Introduction"}
            :why-fns {:label "Why functions?"}
            :extending {:label "Extending style functions"}
            :key-meta {:label "The key matadata"}
            :group-meta {:label "The group metadata"
                         :sub {:defgroup {:label "defgroup macro"}}}

            :fn-vars {:label "Function variations"
                      :sub {:anon {:label "Anonymous functions"}
                            :local {:label "Local binding"}
                            :named {:label "Named anonymous"}
                            :bound {:label "Bound anonymous"}}}
            })

(defgroup sidebar-style
  {:container {:padding (rem 1)}
   :row {:padding-bottom (rem 1)}})

(defn sidebar-root-style
  []
  {:background "#333"
   :position "fixed"
   :transform (if @sidebar-open?
                "translate(0, 0)"
                "translate(-100%, 0)")
   :transition (str "transform 400ms " (:ease-in-out-quad easing/easing))
   :width sidebar-width
   :overflow-y "auto"
   :height "100%"
   :color "white"})

(defn nav-item-style [padding?]
  (let [c "#69BFFF"]
    ^{:pseudo {:hover {:color c}}
      :key padding?}
    {:color "white"
     :padding-left (if padding? (rem 1) 0)}))

(defn nav-item
  [k v index sub?]
  [:div {:class (<class sidebar-style :row)}
   [:a {:href (str "#" (name k))}
    [text {:class (<class nav-item-style sub?)
           :color :white
           :variant :a}
     [:strong (str index ". ")]
     (:label v)]]])

(defn sidebar []
  (r/create-class
   {:reagent-render
    (fn []
      [:section {:class (<class sidebar-root-style)}
       [:nav {:class (<class sidebar-style :container)}
        (map (fn [[k v] idx]
               [:div {:key k}
                [nav-item k v idx false]
                (when-let [sub (:sub v)]
                  (map (fn [[k v] sidx]
                         ^{:key k}
                         [nav-item k v (str idx "." sidx) true])
                       sub
                       (map inc (range))))])
             items
             (map inc (range)))]])}))

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

(defn icon-style
  [scroll?]
  ^{:pseudo {:hover {:background (rgba 0 0 0 0.1)}}}
  {:color "#333"
   :padding (rem 0.625)
   :border-radius (percent 100)
   :cursor "pointer"
   :width (rem 1.5)
   :transition (str "transform 400ms " (:ease-in-out-quad easing/easing) ", "
                    "background 100ms " (:ease-in-out-quad easing/easing))
   :height (rem 1.5)
   :transform (if scroll? "translate(0, 0)" "translate(0, -100%)")
   })

(defn icon-column
  []
  ^{:extend [appbar-style :column]}
  {:padding-left (rem 0.5)
   :display "flex"
   :align-items "center"})

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
  (reset! state (> (.-y (dom/getDocumentScroll)) 0)))

(defn title-style
  [scroll?]
  {:transition (str "transform 200ms " (:ease-out-quad easing/easing))
   :transform (if scroll? "scale(1)" "scale(1.5)" )})

(defn throttle [listener interval]
  "https://medium.com/@alehatsman/clojurescript-throttle-debounce-a651dfb66ac"
  (let [new (Throttle. listener interval)]
    (fn [& args]
      (.apply (.-fire new) new (to-array args)))))

(defn appbar
  []
  (let [scroll? (r/atom nil)]
    (r/create-class
     {:component-did-mount (fn []
                             (on-scroll scroll? nil)
                              (events/listen js/document
                                             event-type/SCROLL
                                             (throttle
                                              #(on-scroll scroll? %)
                                              200)))
      :reagent-render
      (fn []
        [:header {:class (<class appbar-style :root)}
         [:div {:on-click #(swap! sidebar-open? not)
                :class [(<class icon-column)]}
          [icon/menu {:class (<class icon-style @scroll?)}]]
         [:div {:class (<class appbar-style :column)}
          [text {:variant :title
                 :class (<class title-style @scroll?)
                 :align :center}
           "Herb"]]
         [:div {:class (<class appbar-style :column)}]
         [:div {:class (<class divider-style @scroll?)}]])})))
