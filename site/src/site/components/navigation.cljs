(ns site.components.navigation
  (:require [garden.units :refer [px rem percent]]
            [goog.events :as events]
            [clojure.string :as str]
            [site.components.icon :as icon]
            [goog.labs.userAgent.device :as device]
            [site.easing :as easing]
            [garden.color :refer [rgba]]
            [site.utils :as utils]
            [site.async :as async]
            [goog.dom :as dom]
            [goog.events.EventType :as event-type]
            [site.components.text :refer [text]]
            [herb.core :refer [<class defgroup]]
            [reagent.core :as r])
  (:require-macros [garden.def :refer [defcssfn]]))

(def appbar-height (rem 3))
(defonce sidebar-open? (r/atom (device/isDesktop)))
(def sidebar-width (rem 16))
(def items [[:intro "Introduction"]
            [:why-fns "Why functions?"]
            [:metadata "Metadata"]
            [:pseudo "Pseudo classes / elements"]
            [:media-queries "Media queries"]
            [:extending "Extending style functions"]
            [:defgroup "Defgroup macro"]
            [:feature-queries "Feature queries"]
            [:global "Global styles"]
            [:selectors "Selectors"]
            [:vendor-prefixes "Vendor prefixes"]
            [:keyframes "Keyframes"]
            [:fn-vars "Function variations"
             [[:anon "Anonymous functions"]
              [:local "Local binding"]
              [:named "Named anonymous"]
              [:bound "Bound anonymous"]]]
            [:advanced-compile "Advanced compilation"]
            [:examples "Examples and extras"]])

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
    ^{:pseudo {:hover {:color c}}}
    {:color "white"
     :padding-left (if padding? (rem 1) 0)}))

(defn nav-item
  [kw label index sub?]
  [:div {:class (<class sidebar-style :row)}
   [:a {:href (str "#" (name kw))}
    [text {:class (<class nav-item-style sub?)
           :color :white
           :variant :a}
     [:strong (str index ". ")]
     label]]])

(defn sidebar []
  (r/create-class
   {:reagent-render
    (fn []
      [:section {:class (<class sidebar-root-style)}
       [:nav {:class (<class sidebar-style :container)}
        (map (fn [i [kw label sub]]
               [:div {:key kw}
                [nav-item kw label i false]
                (when (seq sub)
                  (map (fn [j [kw label]]
                         ^{:key kw}
                         [nav-item kw label (str i "." j) true])
                       (map inc (range)) sub))])
             (map inc (range)) items)]])}))

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

(defn appbar
  []
  (let [scroll? (r/atom nil)]
    (r/create-class
     {:component-did-mount (fn []
                             (on-scroll scroll? nil)
                             (events/listen js/document
                                            event-type/SCROLL
                                            (async/throttle
                                             #(on-scroll scroll? %)
                                             200)))
      :reagent-render
      (fn []
        [:header {:class (<class appbar-style :root)}
         [:div {:on-click #(swap! sidebar-open? not)
                :class [(<class icon-column)]}
          [icon/menu {:class (<class icon-style @scroll?)}]]
         [:div {:class (<class appbar-style :column)}]
         [:div {:class (<class appbar-style :column)}]
         [:div {:class (<class divider-style @scroll?)}]])})))
