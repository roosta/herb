(ns demo.core
  (:require
   [herb.macro :refer-macros [with-style]]
   [reagent.core :as r]))


(def red {:color "red"})
(def green {:color "green"})

(defn background-color
  [color]
  {:background-color color})

(defn home-page []
  (let [state (r/atom "green")]
    (fn []
      [:div
       [:h2 {:class (with-style background-color @state)}
        "Welcome to Reagent"]
       [:button {:on-click #(reset! state "red")}
        "Toggle"]])))

(defn mount-root []
  (r/render [home-page] (.getElementById js/document "app")))

(defn init! []
  (mount-root))

(init!)
