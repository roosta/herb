(ns site.tutorials.selectors
  (:require
   [site.components.code :refer [code]]
   [site.components.paper :refer [paper]]
   [site.components.text :refer [text]]
   [site.snippets.selectors :as example])
  (:require-macros [site.macros :as macros]))

(defn main
  []
  (let [e1 (macros/example-src "selectors.cljs")]
    [paper {:id "selectors"}
     [text {:variant :heading}
      "Selectors"]
     [text
      "Herb supports a subset of selectors, namly combinators from "
      [:a {:href "https://github.com/noprompt/garden/blob/master/src/garden/selectors.cljc"}
       "garden.selectors"]
      " namespace. These include `descendant`, `+`, `-`, and `>`.
      The reason for this subset is that it made sense in the context of what
      Herb is trying to do, but expanded support for more selectors is possble."]
     [text
      "To use these selectors metadata is enployed once again, this time under
      the `:combinators` key."]
     [code {:lang :clojure}
      e1]
     [text {:variant :subheading}
      "Output:"]
     [example/component]
     [text
      "The syntax is a map with vectors as keys"]

     ]) )
