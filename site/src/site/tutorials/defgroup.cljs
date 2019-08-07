(ns site.tutorials.defgroup
  (:require [garden.units :refer [em px rem]]
            [site.components.code :refer [code]]
            [site.components.paper :refer [paper]]
            [site.components.text :refer [text]]
            [herb.core :as herb :refer [<class <id]]
            [reagent.core :as r]
            [reagent.debug :as d])
  (:require-macros [site.macros :as macros]))

(defn main
  []
  (let [e1 (macros/example-src "my_group.cljs")
        e2 (macros/example-src "my_group_args.cljs")]
    [paper {:id "defgroup"}
     [text {:variant :heading}
      "Defgroup macro"]
     [text
      "A common pattern in herb is to group styles together, imagine
      you are writing a bunch of static css, but you don't wand a DOM
      style element for each class. Theres a macro `defgroup` that is
      sugar around this pattern:"]
     [code {:lang :clojure}
      e1]
     [text
      "You can pass arguments other than then component to group, it
      is stored in the var `args`:"]
     [code {:lang :clojure}
      e2]
     ]
    ))
