(ns demo.core
  (:require
   [herb.macro :refer-macros [with-style]]
   [garden.selectors :as s]
   [garden.core :refer [css]]
   [garden.units :refer [px]]
   [reagent.debug :as d]
   [reagent.core :as r]))

(enable-console-print!)

(def red {:color "red"})
(def green {:color "green"})

(defn state-hover
  [color]
  ^{:mode {:hover {:color "yellow"}}}
  {:margin-bottom 0
   :background-color color})

(defn toggle-color
  [color]
  (case color
    "red" "green"
    "green" "red"))

(defn hover-focus
  []
  ^{:mode {:hover {:background-color "green"}
           :focus {:background-color "yellow"}}}
  {:background-color "red"}
  )

(defn dynamic-text-color
  [color]
  (case color
    (or "blue" "purple") {:color "white"}
    {:color "black"}))

(defn margin
  []
  {:margin "5px"})

(defn italic
  []
  {:font-style "italic"})

(defn bold
  []
  ^{:extend [italic margin]}
  {:font-weight "bold"})

(defn cycle-color
  [color]
  ^{:key color
    :extend [[dynamic-text-color color] bold]}
  {:background-color color}
  )

(defn button
  []
  {:margin (px 10)})

(defn home-page []
  (let [state (r/atom "green")]
    (fn []
      [:div
       [:input {:class (with-style hover-focus)
                :default-value "Hello world"}]
       [:div
        [:h2 {:class (with-style state-hover @state)}
         "Welcome to Reagent"]
        [:button {:class (with-style button)
                  :on-click #(reset! state (toggle-color @state))}
         "Toggle"]
        (for [c ["yellow" "blue" "green" "purple"]]
          ^{:key c}
          [:div {:class (with-style cycle-color c)}
           c])]])))

(defn mount-root []
  (r/render [home-page] (.getElementById js/document "app")))

(defn init! []
  (mount-root))

(init!)
