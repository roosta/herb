(ns server
  (:require [ring.middleware.reload :refer [wrap-reload]]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [ring.util.response :as response]
            [compojure.core :refer [GET defroutes]]
            [compojure.route :as route]))

(defroutes app-routes
  ;; NOTE: this will deliver all of your assets from the public directory
  ;; of resources i.e. resources/public
  (route/resources "/" {:root "public"})

  ;; NOTE: this will deliver your index.html
  (GET "*" [] (-> (response/resource-response "index.html" {:root "public"})
                  (response/content-type "text/html")))
  (route/not-found "Not Found"))

;; NOTE: wrap reload isn't needed when the clj sources are watched by figwheel
;; but it's very good to know about
(def app (wrap-reload (wrap-defaults #'app-routes site-defaults)))
