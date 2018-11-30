(ns herb-demo.tutorials.global
  (:require
   [garden.units :refer [em px rem]]
   [herb-demo.components.code :refer [code]]
   [herb-demo.components.paper :refer [paper]]
   [herb-demo.components.text :refer [text]]
   [herb-demo.snippets.global :as syntax]
   [herb.core :as herb :refer-macros [<class <id]]
   [reagent.core :as r])
  (:require-macros
   [herb-demo.macros :as macros])
  )

(defn main []
  (let [e1 (macros/example-src "global.cljs")
        e2 (macros/example-src "global.html")]
    [paper {:id "global"}
     [text {:variant :heading}
      "Global styles"]
     [text
      "Even though Herb is all about scoped CSS sometimes you can't get around
      needing to target spesific elements using a selector. That's where the
      macro `defglobal` comes in"]

     [text
      "`defglobal` provides an interface to Gardens selector syntax, and also
      ensures that the style is added to the DOM:"]
     [code {:lang :clojure}
      e1]
     [code {:lang :html}
      e2]
     [text
      [:a {:href "https://github.com/noprompt/garden"} "Garden"]
      " syntax applies to the selectors, and no concession is made to ensure
     that there does not exist duplicates."] ]))
