(ns herb-demo.core
  (:require [accountant.core :as accountant]
            [garden.core :refer [css]]
            [garden.selectors :as s]
            [garden.stylesheet :refer [at-media]]
            [garden.units :refer [em px]]
            [herb-demo.tests :refer [tests]]
            [herb-demo.tutorial :refer [site]]
            [herb.core
             :refer
             [<class <id defgroup global-style!]
             :include-macros
             true]
            [reagent.core :as r]
            [reagent.debug :as d]
            [secretary.core :as secretary :include-macros true]))

(def global-style
  (list [:body {:background "#eee"
                :box-sizing "border-box"
                :font-size (px 14)
                :font-family ["Lato" "Helvetica Neue" "Arial" "Helvetica" "sans-serif"]}]
        [:button {:border "none"}]
        [:code {:background-color "#eee"
                :border-radius "2px"
                :padding (px 2)}]
        [:a {:text-decoration "none"
             :color "#09f"}]))

(defn home-page []
  [:div
   [:ul
    [:li [:a {:href "/tests"}
          "Tests"]]
    [:li [:a {:href "/site"}
           "Site"]]]])

(defonce page (r/atom #'home-page))

(secretary/defroute "/" []
  (reset! page #'home-page))

(secretary/defroute "/tests" []
  (reset! page #'tests))

(secretary/defroute "/site" []
  (reset! page #'site))

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
