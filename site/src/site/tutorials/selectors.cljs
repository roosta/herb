(ns site.tutorials.selectors
  (:require
   [site.components.code :refer [code]]
   [site.components.paper :refer [paper]]
   [site.components.text :refer [text]]
   [site.snippets.selectors :as example])
  (:require-macros [site.macros :as macros]))

(defn main
  []
  (let [e1 (macros/example-src "selectors.cljs")
        e2 (macros/example-src "selectors.html")]
    [paper {:id "selectors"}
     [text {:variant :heading}
      "Selectors"]
     [text
      "Herb supports a subset of selectors, namely combinators from "
      [:a {:href "https://github.com/noprompt/garden/blob/master/src/garden/selectors.cljc"}
       "garden.selectors"]
      " namespace. These include `descendant`, `+`, `-`, and `>`.
      The reason for this subset is that it made sense in the context of what
      Herb is trying to do, but expanded support for more selectors could be possible."]
     [text
      "To use these selectors metadata is employed once again, this time under
      the `:combinators` key."]
     [code {:lang :clojure}
      e1]
     [text {:variant :subheading}
      "Output:"]
     [example/component]
     [text
      "The DOM would look something this:"]
     [code {:lang :html}
      e2]
     [text
      "The syntax for the combinators key value, is a map with vectors as keys,
     where the first element in the vector is the combinator function that you
     want used as a keyword (`:>`, `:+` `:-` `:descendant`). The remaining
     elements are keywords for whatever elements/classnames/ids you want
     selected. The different selector functions takes different amount of
     arguments. Refer to "
      [:a {:href "https://github.com/noprompt/garden/blob/master/src/garden/selectors.cljc"}
       "garden.selectors"]
      " documentation for their use. You can view the vector as really a
      function call, where the generated classname is part of the arguments.
      What follows the vector as the value in our combinator map is the style
      map you want applied to the selected element."]
     ]) )
