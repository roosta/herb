(ns site.tutorials.defgroup
  (:require [garden.units :refer [em px rem]]
            [site.components.code :refer [code]]
            [site.components.paper :refer [paper]]
            [site.components.text :refer [text]]
            [site.snippets.my-group :as my-group]
            [herb.core :as herb :refer [<class <id]]
            [reagent.core :as r]
            [reagent.debug :as d])
  (:require-macros [site.macros :as macros]))

(defn main
  []
  (let [e1 (macros/example-src "my_group.cljs")
        e2 (macros/example-src "my_group_args.cljs")
        e3 (macros/example-src "my_group.html")
        e4 (macros/example-src "my_group_expand.cljs")]
    [paper {:id "defgroup"}
     [text {:variant :heading}
      "Defgroup macro"]
     [text
      "A common pattern in herb is to group styles together, imagine
      you are writing a bunch of static css, but you don't wand a DOM
      style element for each class. Theres a macro `defgroup` that is
      sugar around this pattern:"]
     [code {:lang :clojure}
      e1]
     [text
      "The DOM would look something like this:"]
     [code {:lang :html}
      e3]
     [text
      "The group macro uses the `:hint` meta data to add a hint to the
      classnames, denoting which component each classname uses."]
     [text
      "You can pass arguments other than the component to group, it
      is stored in the var `args`:"]
     [code {:lang :clojure}
      e2]
     [text
      "The `defgroup` macro expands to something very similar to this:"]
     [code {:lang :clojure}
      e4]
     [text "As you can see there isn't all that much going on here,
     this patten used to be more useful in previous versions of
     herb `(< v0.10.0)` where grouping wasn't implicit. It still
     serves a purpose though, and removing this would've broken a lot
     of code relying on the herb API being consistent, and it does
     simplify what I experience as a very common pattern."]]))
