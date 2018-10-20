(ns herb-demo.tutorials.key-concept
  (:require [garden.units :refer [em px rem]]
            [herb-demo.components.code :refer [code]]
            [herb-demo.components.paper :refer [paper]]
            [herb-demo.components.text :refer [text]]
            [herb-demo.snippets.involved :as involved]
            [herb-demo.snippets.keyfn :as keyfn]
            [herb.core :as herb :refer-macros [<class <id]]
            [reagent.core :as r]
            [reagent.debug :as d])
  (:require-macros [herb-demo.macros :as macros]))

(defn key-concept
  []
  (let [e1 (macros/example-src "keyfn.cljs")
        e2 (macros/example-src "keyfn.html")]
    [paper
     [text {:variant :title}
      "A key concept"]
     [text
      "Another example for Herb's use of functions are the ability to iterate
     over some data, and have the CSS reflect this: "]
     [code {:lang :clojure}
      e1]
     [text {:variant :subheading}
      "Output:"]
     [keyfn/component]
     [text
      "Take note of the use of meta data here, both " [:a
     {:href "https://github.com/reagent-project/reagent"} "Reagent"] " and Herb
     uses a key to identify each of the divs/styles that gets
     created. " [:code "Herb"] " uses this meta data to tell the difference
     between each iteration, and creates a unique classname based on the key. We
     can take a look at a DOM aproximation to see the result: "]
     [code {:lang :html}
      e2]
     ]))
