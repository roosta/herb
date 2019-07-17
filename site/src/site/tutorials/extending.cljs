(ns site.tutorials.extending
  (:require-macros [site.macros :as macros])
  (:require [garden.units :refer [em px rem]]
            [site.components.code :refer [code]]
            [site.components.paper :refer [paper]]
            [site.snippets.extend :as ext]
            [site.snippets.extend-args :as ext-args]
            [site.snippets.extend-multi :as ext-multi]
            [site.components.text :refer [text]]
            [herb.core :as herb :refer [<class <id]]
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
