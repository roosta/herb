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

(defn background-color
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

(defn home-page []
  (let [state (r/atom "green")]
    (fn []
      ; (d/log (css [:.something (s/hover) {:color "red"}]))
      [:div
       [:input {:class (with-style hover-focus)
                :default-value "Hello world"}]
       [:div
        [:h2 {:class (with-style background-color @state)}
         "Welcome to Reagent"]
        [:button {:on-click #(reset! state (toggle-color @state))}
         "Toggle"]]])))

(defn mount-root []
  (r/render [home-page] (.getElementById js/document "app")))

(defn init! []
  (mount-root))

(init!)
