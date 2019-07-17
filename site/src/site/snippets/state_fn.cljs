;; Herb is mostly just a thin wrapper around Garden, so we can require in
;; various garden namespaces, and use them in our style function
(ns site.snippets.state-fn
  (:require
   [herb.core :refer [<class <id]]
   [garden.units :refer [px]]
   [clojure.string :as str]
   [reagent.core :refer [atom]]
   [garden.color :refer [darken as-hex]]))

;; Lets define a button style, that takes a color-key and creates a color hex and an
;; accent based on that hex
(defn button-style
  [state]
  (let [color (if state "#F02311" "#1693A5")
        accent (-> color
                   (darken 12)
                   as-hex)]
    {:background-color color
     :display "inline-block"
     :color "white"
     :text-transform "uppercase"
     :cursor "pointer"
     :padding (px 12)
     :border-bottom (str "5px solid " accent)}))

(defn button
  []
  (let [state (atom false)]
    (fn []
      [:div {:on-click #(swap! state not)
             :class (<class button-style @state)}
       "Click me"])))
