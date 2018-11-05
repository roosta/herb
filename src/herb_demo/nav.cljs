(ns herb-demo.nav
  (:require [garden.units :refer [px]]
            [herb-demo.components.text :refer [text]]
            [herb.core :refer-macros [<class defgroup]]
            [reagent.core :as r]))

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
   })

(defn a [active?]
  (let [active-color "#3BABFF"]
    ^{:key active?
      :pseudo {:hover {:color active-color}}}
    {:color (if active? active-color "white")})
  )

(defn nav []
  (let [state (r/atom nil)]
    (fn []
      [:div {:class (<class nav-style :root)}
       [:div {:class (<class nav-style :container)}
        (doall
         (for [[k v] items]
           (let [active? (= @state k)]
             (.log js/console active?)
             ^{:key (:label v)}
             [:div {:class (<class nav-style :row)}
              [:a {:on-click #(reset! state k)
                   :class (<class a active?)
                   :href (str "#" (name k))}
               (:label v)]])))]])))
