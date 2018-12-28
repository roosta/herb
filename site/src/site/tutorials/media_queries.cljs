(ns site.tutorials.media-queries
  (:require
   [site.components.code :refer [code]]
   [site.components.paper :refer [paper]]
   [site.snippets.media-queries :as example]
   [site.components.text :refer [text]])
  (:require-macros [site.macros :as macros])
  )

(defn main
  []
  (let [e1 (macros/example-src "media_queries.cljs")
        e2 (macros/example-src "media_queries.html")]
    [paper {:id "media-queries"}
     [text {:variant :heading}
      "Media queries"]
     [text
      "Like with pseudo classes and elements, to attach a media query to a style
      map we use the `:media` metadata. The syntax is: "]
     [code {:lang :clojure}
      (pr-str {:media {{:query :here} {:style :here}}})]
     [text
      "To demonstrate lets make a simple container that is responsive:"]
     [code {:lang :clojure}
      e1]
     [text
      "Checking out the DOM:"]
     [code {:lang :html}
      e2]
     [text
      "The query syntax is the same as in Garden, refer to it's "
      [:a
       {:href "https://github.com/noprompt/garden/wiki/Media-Queries"} "documentation"]
      " for more details on that."]]))
