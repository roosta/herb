(ns herb-demo.tutorials.metadata
  (:require
   [herb-demo.components.code :refer [code]]
   [herb-demo.components.paper :refer [paper]]
   [herb-demo.components.text :refer [text]]
   [herb-demo.snippets.metadata])
  (:require-macros [herb-demo.macros :as macros]))

(defn metadata
  []
  (let [e1 (macros/example-src "metadata.cljs")]
    [paper {:id "metadata"}
     [text {:variant :heading}
      "Metadata"]
     [text
      "Herb uses metadata for various tasks related to but not part of a style
      map. To attach metadata the caret notation `^{:some :meta}` can be
      used or clojure.core's "
      [:a {:href "https://clojuredocs.org/clojure.core/with-meta"} "with-meta"] " and "
      [:a {:href "https://clojuredocs.org/clojure.core/vary-meta"} "vary-meta"] "."]
     [code {:lang :clojure}
      e1]
     [text
      "Dont worry if this doen't make a ton of sense right now, we'll get
      into spesific examples next."]]) )
