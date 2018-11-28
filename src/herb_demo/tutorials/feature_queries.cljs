(ns herb-demo.tutorials.feature-queries
  (:require [garden.units :refer [em px rem]]
            [herb-demo.components.code :refer [code]]
            [herb-demo.components.paper :refer [paper]]
            [herb-demo.snippets.supports :as example]
            [herb-demo.components.text :refer [text]]
            [herb.core :as herb :refer-macros [<class <id]]
            [reagent.core :as r])
  (:require-macros [herb-demo.macros :as macros])
  )

(defn main
  []
  (let [e1 (macros/example-src "supports.cljs")
        e2 (macros/example-src "supports.html")]
    [paper {:id "feature-queries"}
     [text {:variant :heading}
      "Feature queries"]
     [text
      "Attach feature queries to a style function by using the `:supports`
   metadata. The syntax is: "]
     [code {:lang :clojure}
      (pr-str {:supports {{:query :here} {:style :here}}})]

     [text "Example: "]
     [code {:lang :clojure}
      e1]
     [text "The DOM"]
     [example/component]
     [code {:lang :html}
      e2]]
    ))
