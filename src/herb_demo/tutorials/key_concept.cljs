(ns herb-demo.tutorials.key-concept
  (:require [garden.units :refer [em px rem]]
            [herb-demo.components.code :refer [code]]
            [herb-demo.components.paper :refer [paper]]
            [herb-demo.components.text :refer [text]]
            [herb-demo.snippets.involved :as involved]
            [herb.core :as herb :refer-macros [<class <id]]
            [reagent.core :as r]
            [reagent.debug :as d])
  (:require-macros [herb-demo.macros :as macros]))

(defn key-concept
  []
  (let [involved (macros/example-src "involved.cljs")]
    [paper
     [text {:variant :title}
      "A key concept"]
     [text
      "Another example for Herb's use of functions are the ability to iterate
     over some data, and have the CSS reflect this: "]
     [code {:lang :clojure}
      ]
     [text {:variant :subheading}
      "Output:"]

     ]))
