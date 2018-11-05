(ns herb-demo.tutorials.extending
  (:require-macros [herb-demo.macros :as macros])
  (:require [garden.units :refer [em px rem]]
            [herb-demo.components.code :refer [code]]
            [herb-demo.components.paper :refer [paper]]
            [herb-demo.snippets.extend :as ext]
            [herb-demo.snippets.extend-args :as ext-args]
            [herb-demo.snippets.extend-multi :as ext-multi]
            [herb-demo.components.text :refer [text]]
            [herb.core :as herb :refer-macros [<class <id]]
            [reagent.core :as r]
            [reagent.debug :as d]))

;; {:href \"https://clojuredocs.org/clojure.core/with-meta\" :title \"with-meta\"} and
;; {:href \"https://clojuredocs.org/clojure.core/vary-meta\" :title \"vary-meta\"}

(defn extending
  []
  (let [e1 (macros/example-src "extend.cljs")
        e2 (macros/example-src "extend_args.cljs")
        e3 (macros/example-src "extend_multi.cljs")]
    [paper {:id "extending"}
     [text {:variant :title}
      "Extending functions"]
     [text
      "Herb uses metadata for various tasks related to but not part of a style
      map. To attach metadata the shorthand `^{:some :meta}` can be used or clojure.core's "
      [:a {:href "https://clojuredocs.org/clojure.core/with-meta"} "with-meta"] " and "
      [:a {:href "https://clojuredocs.org/clojure.core/vary-meta"} "vary-meta"]
      ". To extend a style function use the metadata `:extend`, like so:"]
     [code {:lang :clojure}
      e1]
     [text {:variant :subheading}
      "Output:"]
     [ext/component]
     [text
      "You can pass arguments to extended functions:"]
     [code {:lang :clojure}
      e2]
     [text {:variant :subheading}
      "Output:"]
     [ext-args/component]
     [text
      "You can extend as many functions as you want:"]
     [code {:lang :clojure}
      e3]
     [text {:variant :subheading}
      "Output:"]
     [ext-multi/component]
     ]))
