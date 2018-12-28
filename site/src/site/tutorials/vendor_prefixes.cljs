(ns site.tutorials.vendor-prefixes
  (:require
   [garden.units :refer [em px rem]]
   [site.components.code :refer [code]]
   [site.components.paper :refer [paper]]
   [site.components.text :refer [text]]
   [site.snippets.vendor-init :as init]
   [site.snippets.vendor-local :as local]
   [site.snippets.inline-prefixes :as inline]
   [herb.core :as herb :refer-macros [<class <id]]
   [reagent.core :as r])
  (:require-macros
   [site.macros :as macros])
  )

(defn main []
  (let [e1 (macros/example-src "vendor_init.cljs")
        e2 (macros/example-src "vendor_init.html")
        e3 (macros/example-src "vendor_local.cljs")
        e4 (macros/example-src "inline_prefixes.cljs")
        e5 (macros/example-src "inline_prefixes.html")]
    [paper {:id "vendor-prefixes"}
     [text {:variant :heading}
      "Vendor prefixes"]
     [text "You can define vendor prefixes several ways, the first one is by
      using `herb.core/init!` and passing the options `:vendors` and
      `:auto-prefix`"]
     [code {:lang :clojure}
      e1]
     [text "By using auto-prefix all selected properties are prefixed with the
      selection in `:vendor`"]
     [code {:lang :html}
      e2]
     [text "You can also place `:vendors` and `:prefix` in a style map metadata"]
     [code {:lang :clojure}
      e3]
     [text "Bear in mind that any property in a stylemap marked with prefix are
     going to be prefixed, so if I had `:color \"red\"` in there that would be
     prefixed as well."]
     [text "You can also inline the prefixes using garden syntax: "]
     [code {:lang :clojure}
      e4]
     [code {:lang :html}
      e5
      ]]))
