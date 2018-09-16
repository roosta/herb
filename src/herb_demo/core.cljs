(ns herb-demo.core
  (:require
   [herb.core :include-macros true :refer [<class defgroup <id global-style!]]
   [garden.selectors :as s]
   [garden.core :refer [css]]
   [garden.stylesheet :refer [at-media]]
   [taoensso.tufte :as tufte :refer-macros [defnp p profiled profile]]
   [garden.core :refer [css]]
   [herb-demo.examples :refer [examples]]
   [garden.units :refer [px em]]
   [reagent.debug :as d]
   [reagent.core :as r]))

(defn mount-root []
  (r/render [examples] (.getElementById js/document "demo")))

(defn init!
  []
  (mount-root)
  #_(global-style! (list [:body
                        {:font-family ["Helvetica Neue" "Verdana" "Helvetica" "Arial" "sans-serif"]
                         :max-width (px 600)
                         :margin "0 auto"
                         :padding-top (px 72)
                         :-webkit-font-smoothing "antialiased"
                         :font-size (em 1.125)
                         :color "#333"
                         :line-height (em 1.5)
                         }])))
