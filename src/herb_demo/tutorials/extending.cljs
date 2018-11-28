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

(defn main
  []
  (let [e1 (macros/example-src "extend.cljs")
        e2 (macros/example-src "extend_args.cljs")
        e3 (macros/example-src "extend_multi.cljs")]
    [paper {:id "extending"}
     [text {:variant :heading}
      "Extending functions"]
     [text
      "In Herb you can extend a style functions from another. You can build a
      hierarchy of styles and Herb ensures the proper precedence. To extend a
      style function use the metadata `:extend`, like so:"]
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
