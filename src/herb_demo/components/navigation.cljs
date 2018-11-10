(ns herb-demo.components.navigation
  (:require [garden.units :refer [px rem]]
            [goog.events :as events]
            [debux.cs.core :as d :refer-macros [clog clogn dbg dbgn break]]
            [herb-demo.easing :as easing]
            [garden.color :refer [rgba]]
            [goog.dom :as dom]
            [goog.events.EventType :as event-type]
            [herb-demo.components.text :refer [text]]
            [herb.core :refer-macros [<class defgroup]]
            [reagent.core :as r])
  (:require-macros [garden.def :refer [defcssfn]]))

(def appbar-height (rem 3))
(def sidebar-width (rem 16))

(def items {:intro {:label "Introduction"}
            :why-fns {:label "Why functions?"}
            :extending {:label "Extending style functions"}
            :key-meta {:label "The key matadata"}
            :group-meta {:label "The group metadata"
                         :sub {:defgroup {:label "defgroup macro"}}}})

(defgroup sidebar-style
  {:root {:background "#333"
          :position "fixed"
          :overflow-y "auto"
          :width sidebar-width
          :height "100%"
          :color "white"}
   :container {:padding (rem 1)}
   :row {:padding-bottom (rem 1)}})

(defn a-style []
  ^{:pseudo {:hover {:color "#3BABFF"}}}
  {:color "white"})

(defn sub-a-style []
  ^{:extend a-style}
  {:padding-left (rem 1)})

(defn sidebar []
  [:section {:class (<class sidebar-style :root)}
   [:nav {:class (<class sidebar-style :container)}
    (doall
     (map (fn [[k v] index]
            ^{:key (:label v)}
            [:div
             [:div  {:class (<class sidebar-style :row)}
              [:a {:href (str "#" (name k))}
               [text {:class (<class a-style)
                      :color :white
                      :variant :a}
                [:strong (str index ". ")] (:label v)]]]
             (when-let [sub (:sub v)]
               [:div {:class (<class sidebar-style :row)}
                (map (fn [[k v] sub-index]
                       ^{:key (:label v)}
                       [:a {:class (<class sub-a-style)
                            :href (str "#" (name k))}
                        [text {:variant :a
                               :color :white}
                         [:strong (str index "." sub-index " ")] (:label v)]])
                     sub
                     (map inc (range)))])])
          items
          (map inc (range))))]])

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
                 :align :center}
           "Herb"]]
         [:div {:class (<class appbar-style :column)}]
         [:div {:class (<class divider-style @scroll?)}]])})))
