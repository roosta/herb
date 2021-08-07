(ns site.handler
  (:require [reitit.ring :as reitit-ring]
            [site.middleware :refer [middleware]]
            [hiccup.page :refer [include-js include-css html5]]
            [config.core :refer [env]]))

(def mount-target
  [:div#app])

(defn head []
  [:head
   [:meta {:charset "utf-8"}]
   [:title "Herb: ClojureScript styling using functions"]
   [:meta {:name "viewport"
           :content "width=device-width, initial-scale=1"}]
   (include-css (if (env :dev) "/css/atelier-forest-light.css" "/css/atelier-forest-light.min.css"))])

(defn loading-page []
  (html5
   (head)
   [:body {:class "body-container"}
    mount-target
    (include-js "/js/app.js")]))

(defn index-handler
  [_request]
  {:status 200
   :headers {"Content-Type" "text/html"}
   :body (loading-page)})

(def app
  (reitit-ring/ring-handler
   (reitit-ring/router
    [["/" {:get {:handler index-handler}}]
     #_["/items"
      ["" {:get {:handler index-handler}}]
      ["/:item-id" {:get {:handler index-handler
                          :parameters {:path {:item-id int?}}}}]]
     #_["/about" {:get {:handler index-handler}}]]
    {:data {:middleware middleware}})
   (reitit-ring/routes
    (reitit-ring/create-resource-handler {:path "/" :root "/public"})
    (reitit-ring/create-default-handler))))
