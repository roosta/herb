(ns herb-demo.components.navigation
  (:require [garden.units :refer [px]]
            [herb-demo.components.text :refer [text]]
            [herb.core :refer-macros [<class defgroup]]
            [reagent.core :as r]))

(def items {:intro {:label "Introduction"}
            :why-fns {:label "Why functions?"}
            :extending {:label "Extending style functions"}
            :key-meta {:label "The key matadata"}
            :group-meta {:label "The group metadata"
                         :sub {:defgroup {:label "defgroup macro"}}}})

(defgroup nav-style
  {:root {:background "#333"
          :position "fixed"
          :overflow-y "auto"
          :width (px @width)
          :height "100%"
          :color "white"}
   :container {:padding (px 16)}
   :row {:padding-bottom (px 16)}})

(defn a-style []
  ^{:pseudo {:hover {:color "#3BABFF"}}}
  {:color "white"})

(defn sub-a-style []
  ^{:extend a-style}
  {:padding-left (px 16)})

(defn sidebar []
  [:div {:class (<class nav-style :root)}
   [:div {:class (<class nav-style :container)}
    (map (fn [[k v] index]
           ^{:key (:label v)}
           [:div
            [:div  {:class (<class nav-style :row)}
             [:a {:class (<class a-style)
                  :href (str "#" (name k))}
              [:span [:strong (str index ". ")] (:label v)]]]
            (when-let [sub (:sub v)]
              [:div {:class (<class nav-style :row)}
               (map (fn [[k v] sub-index]
                      ^{:key (:label v)}
                      [:a {:class (<class sub-a-style)
                           :href (str "#" (name k))}
                       [:span [:strong (str index "." sub-index " ")] (:label v)]])
                    sub
                    (map inc (range)))])])
         items
         (map inc (range)))]])
