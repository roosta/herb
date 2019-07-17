(ns site.tutorials.feature-queries
  (:require [garden.units :refer [em px rem]]
            [site.components.code :refer [code]]
            [site.components.paper :refer [paper]]
            [site.snippets.supports :as example]
            [site.components.text :refer [text]]
            [herb.core :as herb :refer [<class <id]]
            [reagent.core :as r])
  (:require-macros [site.macros :as macros])
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
