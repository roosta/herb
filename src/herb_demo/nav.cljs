(ns herb-demo.nav
  (:require [garden.units :refer [px]]
            [herb-demo.components.text :refer [text]]
            [herb.core :refer-macros [<class defgroup]]))

(def items {:intro {:label "1. Introduction"}
            :why-fns {:label "2. Why functions?"}
            :extending {:label "3. Extending style functions"}
            :key-meta {:label "4. The key matadata"}
            :group-meta {:label "5. The group metadata"}})

(defgroup nav-style
  {:root {:background "#2e3138"
          :height "100%"
          :color "white"}
   :container {:padding (px 16)}
   :row {:padding-bottom (px 8)}
   :a
   ^{:pseudo {:hover {:color "#3BABFF"}}}
   {:color "white"}})

(defn nav
  []
  [:div {:class (<class nav-style :root)}
   [:div {:class (<class nav-style :container)}
    (for [[k v] items]
      ^{:key (:label v)}
      [:div {:class (<class nav-style :row)}
       [:a {:class (<class nav-style :a)
            :href (str "#" (name k))}
        (:label v)]])]])
