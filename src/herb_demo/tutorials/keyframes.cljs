(ns herb-demo.tutorials.keyframes
  (:require [garden.units :refer [em px rem]]
            [herb-demo.components.code :refer [code]]
            [herb-demo.components.paper :refer [paper]]
            [herb-demo.components.text :refer [text]]
            [herb.core :as herb :refer-macros [<class <id]]
            [reagent.core :as r])
  (:require-macros [herb-demo.macros :as macros]))

(defn main
  []
  (let [e1 (macros/example-src "supports.cljs")
        e2 (macros/example-src "supports.html")]
    [paper {:id "keyframes"}
     [text {:variant :heading}
      "Keyframes and animations"]
     [text
      ""]
]
    ))
