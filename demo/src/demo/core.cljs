(ns demo.core
  (:require
   [herb.macro :refer-macros [defstyle]]
   [reagent.core :as r]))

(defn home-page []
  [:div [:h2 {:class "test-class-name"}
         "Welcome to Reagent"]])

(defstyle hello nil)

(defn mount-root []
  (r/render [home-page] (.getElementById js/document "app")))

(defn init! []
  (mount-root))

(init!)
