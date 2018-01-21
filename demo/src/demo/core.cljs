(ns demo.core
  (:require
   [herb.macro :refer-macros [defstyle with-style]]
   [reagent.core :as r]))


(defstyle green {:color "green"})

(def red {:color "red"})

(defn home-page []
  [:div
   [:h2 {:class (with-style red)}
    "Welcome to Reagent"]])

(defn mount-root []
  (r/render [home-page] (.getElementById js/document "app")))

(defn init! []
  (mount-root))

(init!)
