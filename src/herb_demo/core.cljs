(ns herb-demo.core
  (:require [accountant.core :as accountant]
            [garden.units :refer [px]]
            [herb-demo.examples :as examples]
            [herb-demo.tutorial :as tutorial]
            [reagent.session :as session]
            [clerk.core :as clerk]
            [bidi.bidi :as bidi]
            [herb.core :as herb :refer [defglobal] :include-macros true]
            [reagent.core :as r]))

(defglobal global
  [:body {:background "#eee"
          :box-sizing "border-box"
          :margin 0}]
  [:button {:border "none"}]
  [:code {:background-color "#eee"
          :border-radius "2px"
          :padding (px 2)}]
  [:a {:text-decoration "none"
       :color "#09f"}])

(def app-routes
  ["/" {"" :tutorial
        "examples" :examples
        true :four-o-four}])

(defmulti page-contents identity)
(defmethod page-contents :tutorial [] tutorial/main)
(defmethod page-contents :examples [] examples/main)
(defmethod page-contents :four-o-four []
  (fn []
    [:span
     [:h1 "404: It is not here"]]))

(defn appframe []
  (let [page (:current-page (session/get :route))]
    ^{:key page}
    [page-contents page]))

(defn mount-root []
  (r/render [appframe] (.getElementById js/document "demo")))

(defn init!
  []
  (clerk/initialize!)
  (accountant/configure-navigation!
   {:nav-handler (fn
                   [path]
                   (r/after-render clerk/after-render!)
                   (let [match (bidi/match-route app-routes path)
                         current-page (:handler match)
                         route-params (:route-params match)]
                     (session/put! :route {:current-page current-page
                                           :route-params route-params}))
                   (clerk/navigate-page! path))
    :path-exists? (fn [path]
                    (boolean (bidi/match-route app-routes path)))})
  (accountant/dispatch-current!)

  (herb/init! {:vendors ["o"]
               :auto-prefix #{:transition}})
  (mount-root))
