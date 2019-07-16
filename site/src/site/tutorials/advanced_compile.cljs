(ns site.tutorials.advanced-compile
  (:require-macros [site.macros :as macros])
  (:require [site.components.code :refer [code]]
            [site.components.paper :refer [paper]]
            [site.components.text :refer [text]]))


(defn main
  []
  (let [e1 (macros/example-src "project.txt")
        e2 (macros/example-src "advanced_compile.html")
        e3 (macros/example-src "pseudo-names.txt")]
    [paper {:id "advanced-compile"}
     [text {:variant :heading}
      "Advanced Compilation"]
     [text "With `:optimizations :advanced` enabled, the output Javascript is
     `munged`, meaning that all names are renamed to something as short as
     possible, something we've all dealt with at some point. Herb uses the
     munged names on advanced compile, and classnames reflect this."]
     [text "Herb also uses a debug flag that that toggles of various development
     features, most notably disables the `data-herb` attribute in style
     elements, and disables pretty-print in Garden. To enable this include in
     your compiler config for the production build `:closure-defines {\"goog.debug\" false}`:"]
     [code {:lang :clojure}
      e1]
     [text
      "An example DOM might look something like this when doing advanced
       optimizations:"]
     [code {:lang :html}
      e2]
     [text {:variant :subheading}
      "Debugging advanced compile"]
     [text "During advanced compilation Herb minify styles and removes the
     `data-herb` attribute. If you need to debug production build it can be
     helpful to see the function namespaces unmunged to get a clearer image
     of what is happening."

      [text "To do this remove the `goog.DEBUG false` from production build and
       enable `:pseudo-names`: "]]

     [code {:lang :clojure}
      e3]
     [text
      "That way you will see both full classnames and the namespace reflected
       in the `data-herb` HTML attribute."]]))
