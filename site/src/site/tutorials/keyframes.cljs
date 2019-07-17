(ns site.tutorials.keyframes
  (:require
   [garden.units :refer [em px rem]]
   [site.components.code :refer [code]]
   [site.components.paper :refer [paper]]
   [site.components.text :refer [text]]
   [site.snippets.keyframes :as syntax]
   [site.snippets.keyframes-use :as kfuse]
   [herb.core :as herb :refer [<class <id]]
   [reagent.core :as r])
  (:require-macros
   [site.macros :as macros]))

(defn main
  []
  (let [e1 (macros/example-src "keyframes.cljs")
        e2 (macros/example-src "keyframes.html")
        e3 (macros/example-src "keyframes_use.cljs")]
    [paper {:id "keyframes"}
     [text {:variant :heading}
      "Keyframes and animations"]
     [text
      "Keyframes in Herb is handled a bit differently than what has been
      demonstrated up until now. Keyframes use a special macro and style
      element. The macro `defkeyframes` resides in `herb.core` and does almost
      the same thing as Gardens `defkeyframes` except that Herb also appends
      rendered styles in a special `<style>` element with the tag
      `data-herb=\"keyframes\"`"]
     [text "The syntax for defkeyframes are the same as in "
      [:a {:href "https://github.com/noprompt/garden/wiki/Compiler"} "Garden"] ":"]
     [code {:lang :clojure}
      e1]
     [text "Here's what the DOM looks like:"]
     [code {:lang :html}
      e2]
     [text
      "Now the animations are globally available so they can be used multiple times.
      To use the animation you can refer to the created symbol or use a string:"]
     [code {:lang :clojure}
      e3]
     [text {:variant :subheading}
      "Output:"]
     [kfuse/component]
     [text
      "Worth noting that this behaviour comes with a caveat, since the
      animation is identified by name alone means that if an animation has the
      same name across multiple namespaces only the last one evaluated is used. A
      benefit with this is that an animation can be referred to using a string
      anywhere so long as the defkeyframes has been evaluated once."]]))
