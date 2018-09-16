(ns herb-demo.core
  (:require
   [herb.core :include-macros true :refer [<class defgroup <id global-style!]]
   [garden.selectors :as s]
   [secretary.core :as secretary :include-macros true]
   [accountant.core :as accountant]
   [garden.core :refer [css]]
   [garden.stylesheet :refer [at-media]]
   [garden.core :refer [css]]
   [herb-demo.examples :refer [examples]]
   [garden.units :refer [px em]]
   [reagent.debug :as d]
   [reagent.core :as r]))

(def global-style
  [:body {:margin 0
          :font-family ["Helvetica Neue" "Verdana" "Helvetica" "Arial" "sans-serif"]}])

(defn home-page []
  [:div
   [:ul
    [:li [:a {:href "/examples"}
          "Examples"]]
    [:li [:a {:href "/site"}
           "Site"]]]])

(defonce page (r/atom #'home-page))

(secretary/defroute "/" []
  (reset! page #'home-page))

(secretary/defroute "/examples" []
  (reset! page #'examples))

(defn appframe []
  [:div [@page]])

(defn mount-root []
  (r/render [appframe] (.getElementById js/document "demo")))

(defn init!
  []
  (global-style! global-style)
  (accountant/configure-navigation!
   {:nav-handler
    (fn [path]
      (secretary/dispatch! path))
    :path-exists?
    (fn [path]
      (secretary/locate-route path))})
  (accountant/dispatch-current!)
  (mount-root))
