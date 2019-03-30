(ns site.tutorials.selectors
  (:require
   [site.components.code :refer [code]]
   [site.components.paper :refer [paper]]
   [site.components.text :refer [text]]
   [site.snippets.selectors :as example])
  (:require-macros [site.macros :as macros]))

(defn main
  []
  (let [e1 (macros/example-src "selectors.cljs")
        e2 (macros/example-src "selectors.html")]
    [paper {:id "selectors"}
     [text {:variant :heading}
      "Selectors"]
     [text
      "Herb supports a subset of selectors, namely combinators from "
      [:a {:href "https://github.com/noprompt/garden/blob/master/src/garden/selectors.cljc"}
       "garden.selectors"]
      " namespace. These include `descendant`, `+`, `-`, and `>`.
      The reason for this subset is that it made sense in the context of what
      Herb is trying to do, but expanded support for more selectors could be possible."]
     [text
      "To use these selectors metadata is employed once again, this time under
      the `:combinators` key."]
     [code {:lang :clojure}
      e1]
     [text {:variant :subheading}
      "Output:"]
     [example/component]
     [text
      "The DOM would look something this:"]
     [code {:lang :html}
      e2]
     [text
      "The syntax is a map with a vector of variable length as a key, starting
      with whatever combinator function you want to run as a keyword (`:>`, `:+` `:-`
      `:descendant`). Some combinators takes multiple elements as arguments. After
      that put whatever style map you'd like to be applied."]
     ]) )
