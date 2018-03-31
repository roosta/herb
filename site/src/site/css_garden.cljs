(ns site.css-garden
  (:require
   [herb.core :refer-macros [<class <id] :as herb]
   ;; [site.macros :refer-macros [example-src]]
   [site.components.text :refer [text]]
   [site.components.code :refer [code]]
   [site.components.paper :refer [paper]]
   [garden.units :refer [rem em px]]
   [reagent.debug :as d]
   [reagent.core :as r])
  (:require-macros
   [site.macros :as macros]))

(defn css-garden
  []
  (let [e1 (macros/example-src "intro.cljs")]
    [paper
     [text {:variant :title}
      ""]
     [text
      "Now Herb is just a thin wrapper around " [:a {:href "https://github.com/noprompt/garden"} "Garden"]
      ", so most of Garden's syntax applies to Herb styles as well. "]
     ]))
