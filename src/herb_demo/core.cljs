(ns herb-demo.core
  (:require
   [herb.core :include-macros true :refer [<class defgroup <id global-style!]]
   [garden.selectors :as s]
   [secretary.core :as secretary :include-macros true]
   [accountant.core :as accountant]
   [garden.core :refer [css]]
   [garden.stylesheet :refer [at-media]]
   [garden.core :refer [css]]
   [herb-demo.tests :refer [tests]]
   [garden.units :refer [px em]]
   [reagent.debug :as d]
   [reagent.core :as r]))

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
