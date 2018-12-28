(ns site.tutorials.intro
  (:require [garden.units :refer [em px rem]]
            [site.components.code :refer [code]]
            [site.components.paper :refer [paper]]
            [site.components.text :refer [text]]
            [site.snippets.intro :as intro]
            [herb.core :as herb :refer-macros [<class <id]]
            [reagent.core :as r]
            [reagent.debug :as d])
  (:require-macros [site.macros :as macros]))

(defn intro-style
  [component]
  (let [styles {:body {}
                :p {}}]
    (with-meta (component styles) {:key component})))

(defn main
  []
  (let [intro-cljs (macros/example-src "intro.cljs")
        intro-html (macros/example-src "intro.html")]
    [paper {:id "intro"}
     [text {:variant :heading}
      "Introduction"]
     [text
      "Herb is a CSS styling library for " [:a {:href "https://clojurescript.org"} "Clojurescript"]
      ", built using " [:a {:href "https://github.com/noprompt/garden"} "Garden"]
      ", whose main focus is on component level styling using functions."]
     [text
      "Lets start of with a basic example, I'm using "
      [:a {:href "https://github.com/reagent-project/reagent"}
       "Reagent"]
      " here but it's not a requirement for Herb."]
     [code {:lang :clojure}
      intro-cljs]
     [text {:variant :subheading}
      "Output:"]
     [intro/example-component]
     [text {:variant :subheading}
      "Lets have a look at the DOM"]
     [text
      "A style element is appended to the DOM containing the input functions
      computed styles. Each function gets its own DOM element, and is updated
      only if necessary. Our example DOM might now look something like this:"]
     [code {:lang :html}
      intro-html]
     [text
      "The classname is a sanitized version of the input functions fully
      qualified name. This way we avoid name collisions but keep a deterministic
      classname that can be targeted."]
     [text
      "The " [:code "data-herb"] " attribute reflects the current namespace for
     a given style, and only in development. It is removed on advanced compile.
     "]]))
