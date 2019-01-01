(ns site.tutorials.advanced-compile
  (:require-macros [site.macros :as macros])
  (:require [site.components.code :refer [code]]
            [site.components.paper :refer [paper]]
            [site.components.text :refer [text]]))


(defn main
  []
  (let [e1 (macros/example-src "project.txt")
        e2 (macros/example-src "advanced_compile.html")]
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
     your compiler config for the production build `:closure-defines
     {\"goog.debug\" false}`:"]
     [code {:lang :clojure}
      e1]
     [text "There are plans to include something like a `pseudo-names` flag to
     enable fully qualified name approximation during advanced compilation to
     facilitate better debugging in production builds."]
     [text
      "An example DOM might look something like this when doing advanced
     optimizations:"]
     [code {:lang :html}
      e2]
     [text "Note that keys are currently not munged and `pretty-printing` is
     disabled due to a "
      [:a {:href "https://github.com/noprompt/garden/issues/168"} "bug in Garden"]
      " affecting media queries."]]))
