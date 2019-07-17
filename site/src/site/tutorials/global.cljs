(ns site.tutorials.global
  (:require
   [garden.units :refer [em px rem]]
   [site.components.code :refer [code]]
   [site.components.paper :refer [paper]]
   [site.components.text :refer [text]]
   [site.snippets.global :as syntax]
   [herb.core :as herb :refer [<class <id]]
   [reagent.core :as r])
  (:require-macros
   [site.macros :as macros])
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
