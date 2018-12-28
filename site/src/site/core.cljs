(ns site.core
    (:require [reagent.core :as reagent :refer [atom]]
              [reagent.session :as session]
              [herb.core :as herb :refer [defglobal] :include-macros true]
              [garden.units :refer [em]]
              [site.tutorial :as tutorial]
              [reitit.frontend :as reitit]
              [clerk.core :as clerk]
              [accountant.core :as accountant]))

(defglobal global
  [:body {:background "#eee"
          :box-sizing "border-box"
          :margin 0}]
  [:button {:border "none"}]
  [:code {:background-color "#eee"
          :border-radius "2px"
          :padding (em 0.125)}]
  [:a {:text-decoration "none"
       :color "#09f"}])

(def router
  (reitit/router
   [["/" :index]]))

(defn path-for [route & [params]]
  (if params
    (:path (reitit/match-by-name router route params))
    (:path (reitit/match-by-name router route))))

(defn page-for [route]
  (case route
    :index #'tutorial/main))

(defn appframe []
  (let [page (:current-page (session/get :route))]
    [page]))
;; -------------------------
;; Initialize app

(defn mount-root []
  (reagent/render [appframe] (.getElementById js/document "app")))

(defn init! []
  (clerk/initialize!)
  (accountant/configure-navigation!
   {:nav-handler
    (fn [path]
      (let [match (reitit/match-by-path router path)
            current-page (:name (:data  match))
            route-params (:path-params match)]
        (reagent/after-render clerk/after-render!)
        (session/put! :route {:current-page (page-for current-page)
                              :route-params route-params})
        (clerk/navigate-page! path "top")))
    :path-exists?
    (fn [path]
      (boolean (reitit/match-by-path router path)))})
  (accountant/dispatch-current!)
  (mount-root))
