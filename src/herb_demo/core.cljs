(ns herb-demo.core
  (:require [accountant.core :as accountant]
            [garden.units :refer [px]]
            [herb-demo.examples :as examples]
            [herb-demo.tutorial :as tutorial]
            [herb.core :refer [defglobal] :include-macros true]
            [reagent.core :as r]
            [secretary.core :as secretary :include-macros true]))

(defglobal global
  [:body {:background "#eee"
          :box-sizing "border-box"
          :font-size (px 14)
          :font-family ["Lato" "Helvetica Neue" "Arial" "Helvetica" "sans-serif"]}]
  [:button {:border "none"}]
  [:code {:background-color "#eee"
          :border-radius "2px"
          :padding (px 2)}]
  [:a {:text-decoration "none"
       :color "#09f"}])

(defn home-page []
  [:div
   [:ul
    [:li [:a {:href "/examples"}
          "Examples"]]
    [:li [:a {:href "/tutorial"}
           "Tutorial"]]]])

(defonce page (r/atom #'home-page))

(secretary/defroute "/" []
  (reset! page #'home-page))

(secretary/defroute "/examples" []
  (reset! page #'examples/main))

(secretary/defroute "/tutorial" []
  (reset! page #'tutorial/main))

(defn appframe []
  [:div [@page]])

(defn mount-root []
  (r/render [appframe] (.getElementById js/document "demo")))

(defn init!
  []
  (accountant/configure-navigation!
   {:nav-handler
    (fn [path]
      (secretary/dispatch! path))
    :path-exists?
    (fn [path]
      (secretary/locate-route path))})
  (accountant/dispatch-current!)
  (mount-root))
