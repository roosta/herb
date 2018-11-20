(ns herb-demo.tutorials.key-meta
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

(defn key-meta
  []
  (let [e1 (macros/example-src "keyfn.cljs")
        e2 (macros/example-src "keyfn.html")]
    [paper {:id "key-meta"}
     [text {:variant :heading}
      "Key meta data"]
     [text
      "Another feature of Herb is the use of the `:key` meta data,
      say we want to map over some structure, and generate CSS for each
      iteration. Normally Herb wouldn't be able to differentiate between the
      different calls of the same function, but if you supply a `:key` Herb can
      generate class names using that key as input:"]
     [code {:lang :clojure}
      e1]
     [text {:variant :subheading}
      "Output:"]
     [keyfn/component]
     [text
      "We can take a look at a DOM approximation to see the result: "]
     [code {:lang :html}
      e2]
     [text
      "The routine Herb use to sanitize the key isn't fool proof, the key needs
      to resolve to a valid classname but it might not, its worth inspecting the
      DOM when for some reason Herb fails to do what it's supposed to"]]))
