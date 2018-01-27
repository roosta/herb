(ns demo.core
  (:require
   [herb.macro :refer-macros [with-style]]
   [garden.selectors :as s]
   [garden.core :refer [css]]
   [reagent.debug :as d]
   [reagent.core :as r]))

(def red {:color "red"})
(def green {:color "green"})

(defn button [] )

(defn state-hover
  [color]
  ^{:mode {:hover {:color "yellow"}}}
  {:background-color color})

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

(defn cycle-color
  [color]
  ^{:key color}
  {:background-color color}
  )

(defn home-page []
  (let [state (r/atom "green")]
    (fn []
      [:div
       [:input {:class (with-style hover-focus)
                :default-value "Hello world"}]
       [:div
        [:h2 {:class (with-style state-hover @state)}
         "Welcome to Reagent"]
        [:button {:on-click #(reset! state (toggle-color @state))}
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
