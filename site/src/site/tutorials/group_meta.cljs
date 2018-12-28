(ns site.tutorials.group-meta
  (:require-macros [site.macros :as macros])
  (:require [garden.units :refer [em px rem]]
            [site.components.code :refer [code]]
            [site.components.paper :refer [paper]]
            [site.components.text :refer [text]]
            [site.snippets.group :as group]
            [site.snippets.my-group :as my-group]
            [site.snippets.my-group-args :as my-group-args]
            [site.snippets.group-pattern :as group-pattern]
            [herb.core :as herb :refer-macros [<class <id]]
            [reagent.core :as r]
            [reagent.debug :as d]))

(defn main
  []
  (let [e1 (macros/example-src "group.cljs")
        e2 (macros/example-src "group.html")
        e3 (macros/example-src "group_pattern.cljs")
        e4 (macros/example-src "group_pattern.html")
        e5 (macros/example-src "my_group.cljs")
        e6 (macros/example-src "my_group_args.cljs")]
    [paper {:id "group-meta"}
     [text {:variant :heading}
      "Group metadata"]
     [text
      "Another kind of meta data that works in conjuction with `:key` is the
     `:group` meta. With this flag we can group styles that share a function, so
     as to create a bit less noise in the `<head>`. Lets use the example from
     the previous section:"]
     [code {:lang :clojure}
      e1]
     [text
      "In the DOM we now see a single style element:"]
     [code {:lang :html}
      e2]
     [text
      "This meta data can be useful when grouping static styles, observe this
      pattern:"]
     [code {:lang :clojure}
      e3]
     [text {:variant :subheading}
      "Output"]
     [group-pattern/component]
     [text "Here is what the DOM looks like:"]
     [code {:lang :html}
      e4]
     [text {:id "defgroup"
            :variant :subheading}
      "defgroup macro"]
     [text
      "The previous pattern was a bit verbose, but I included it to demonstrate
    how the group meta works. There is a macro `defgroup` in `herb.core` that
    simplify creating style groups:"]
     [code {:lang :clojure}
      e5]
     [text
      "This example and the previous one are exactly the same, just wrapped in a
     sugary macro. Its also possible to pass arguments to a group, the macro
     stores all but the first arguments in a variable calls `args`:"]
     [code {:lang :clojure}
      e6]
     [text
      "I would advice against passing args to a stylegroup though, because if
      you're updating any single run of a stylegroup function the entire group
      has to be re-rendered. Stylegroups are more useful when writing static CSS
      that isn't changed during runtime."]]))
