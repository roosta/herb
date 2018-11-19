(ns herb-demo.tutorials.metadata
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

(defn metadata
  []
  (let [e1 (macros/example-src "pseudo.cljs")
        e2 (macros/example-src "pseudo.html")]
    [paper {:id "metadata"}
     [text {:variant :heading}
      "Metadata"]
     [text
      "Herb uses metadata for various tasks related to but not part of a style
      map. To attach metadata the shorthand `^{:some :meta}` can be used or clojure.core's "
      [:a {:href "https://clojuredocs.org/clojure.core/with-meta"} "with-meta"] " and "
      [:a {:href "https://clojuredocs.org/clojure.core/vary-meta"} "vary-meta"] "."]
     [text {:id "pseudo"
            :variant :subheading}
      "Pseudo classes / elements"]
     [text
      "When you want to attach pseudo classes/elements in Herb you do so by
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
      e2]]))
