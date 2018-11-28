(ns herb-demo.tutorials.pseudo
  (:require [garden.units :refer [em px rem]]
            [herb-demo.components.code :refer [code]]
            [herb-demo.components.paper :refer [paper]]
            [herb-demo.snippets.pseudo :as pseudo]
            [herb-demo.components.text :refer [text]]
            [herb.core :as herb :refer-macros [<class <id]]
            [reagent.core :as r]
            [reagent.debug :as d])
  (:require-macros [herb-demo.macros :as macros])
  )

(defn main
  []
  (let [e1 (macros/example-src "pseudo.cljs")
        e2 (macros/example-src "pseudo.html")]
    [paper {:id "pseudo"}
     [text {:variant :heading}
      "Pseudo classes and elements"]
     [text
      "When you want to attach pseudo classes or elements in Herb you do so by
      attaching `:pseudo {:CLASS/ELEMENT {:style :map}}` metadata to your returned
      style map. Better to demonstrate with an example:"]
     [code {:lang :clojure}
      e1]
     [text {:variant :subheading}
      "Output:"]
     [pseudo/component]
     [text
      "Theres our a element, and if you hover over it the gets underlined. Lets
      look at the DOM:"]
     [code {:lang :html}
      e2]
     [text
      "As you can see the rendered CSS has our psudo class and element"]]))
