(ns herb-demo.tutorials.vendor-prefixes
  (:require
   [garden.units :refer [em px rem]]
   [herb-demo.components.code :refer [code]]
   [herb-demo.components.paper :refer [paper]]
   [herb-demo.components.text :refer [text]]
   [herb-demo.snippets.vendor-init :as init]
   [herb-demo.snippets.vendor-local :as local]
   [herb.core :as herb :refer-macros [<class <id]]
   [reagent.core :as r])
  (:require-macros
   [herb-demo.macros :as macros])
  )

(defn main []
  (let [e1 (macros/example-src "vendor_init.cljs")
        e2 (macros/example-src "vendor_init.html")
        e3 (macros/example-src "vendor_local.cljs")]
    [paper {:id "vendor-prefixes"}
     [text {:variant :heading}
      "Vendor prefixes"]
     [text "You can define vendor prefixes in two ways, the first one is by using
      `herb.core/init!` and passing the options `:vendors` and `:auto-prefix`"]
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
     prefixed as well."]]))
