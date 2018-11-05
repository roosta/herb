(ns herb-demo.nav
  (:require [garden.units :refer [px]]
            [herb-demo.components.text :refer [text]]
            [herb.core :refer-macros [<class defgroup]]
            [reagent.core :as r]))

(def items {:intro {:label "Introduction"}
            :why-fns {:label "Why functions?"}
            :extending {:label "Extending style functions"}
            :key-meta {:label "The key matadata"}
            :group-meta {:label "The group metadata"}})

(defgroup nav-style
  {:root {:background "#2e3138"
          :height "100%"
          :color "white"}
   :container {:padding (px 16)}
   :row {:padding-bottom (px 8)}
   :a
   ^{:pseudo {:hover {:color "#3BABFF"}}}
   {:color "white"}
   })

(defn nav []
  [:div {:class (<class nav-style :root)}
   [:div {:class (<class nav-style :container)}
    (doall
     (map (fn [[k v] index]
              ^{:key (:label v)}
              [:div {:class (<class nav-style :row)}
               [:a {:class (<class nav-style :a)
                    :href (str "#" (name k))}
                [:span [:strong (str index ". ")] (:label v)]]])
          items
          (range)))]])
