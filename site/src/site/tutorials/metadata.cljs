(ns site.tutorials.metadata
  (:require
   [site.components.code :refer [code]]
   [site.components.paper :refer [paper]]
   [site.components.text :refer [text]]
   [site.snippets.metadata])
  (:require-macros [site.macros :as macros]))

(defn main
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
