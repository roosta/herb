(ns demo.why-fns
  (:require
   [herb.core :refer-macros [<class <id] :as herb]
   ;; [demo.macros :refer-macros [example-src]]
   [demo.components.text :refer [text]]
   [demo.components.code :refer [code]]
   [demo.components.paper :refer [paper]]
   [garden.units :refer [rem em px]]
   [reagent.debug :as d]
   [reagent.core :as r])
  (:require-macros
   [demo.macros :as macros]))

(defn why-fns
  []
  (let [e1 (macros/example-src "intro.cljs")]
    [paper
     [text {:variant :title}
      "Why functions?"]
     [text
      ""]
     ]))
