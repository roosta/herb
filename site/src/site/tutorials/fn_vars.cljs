(ns site.tutorials.fn-vars
  (:require-macros [site.macros :as macros])
  (:require [site.components.code :refer [code]]
            [site.components.paper :refer [paper]]
            [site.snippets.anon :as anon-example]
            [site.snippets.local-binding :as binding-example]
            [site.snippets.named-anon :as named-anon-example]
            [site.snippets.symbol-fn]
            [site.components.text :refer [text]]
            [site.snippets.state-fn :as e1]))

(defn main
  []
  (let [e1 (macros/example-src "anon.cljs")
        e2 (macros/example-src "anon.html")
        e3 (macros/example-src "local_binding.cljs")
        e4 (macros/example-src "local_binding.html")
        e5 (macros/example-src "named_anon.cljs")
        e6 (macros/example-src "symbol_fn.cljs")
        ]
    [paper {:id "fn-vars"}
     [text {:variant :heading}
      "Function variations"]
     [text {:id "anon"
            :variant :subheading}
      "Anonymous functions"]
     [text
      "Herb allows for different function variations, one of them being anonymous
      functions: "]
     [code {:lang :clojure}
      e1]
     [text {:variant :subheading}
      "Output:"]
     [anon-example/link]
     [text
      "As you can see anonymous functions can take the same meta data
     as a defined one, there isn't much of a difference on how the
     classnames are handled other than we don't have a fully qualified
     name to use. Instead herb just calls the classnames `anonymous`
     and as with everything else appends a hash of the computed styles
     to the end of it. All anonymous styles in a given namespace is
     grouped. Lets look at a DOM approximation:"]
     [code {:lang :html}
      e2]
     [text {:id "local"
            :variant :subheading}
      "Local binding"]
     [text
      "Lets take a look at another variation, one that solves the previous
      problem with anonymous functions, the local binding form:"]
     [code {:lang :clojure}
      e3]
     [text {:variant :subheading}
      "Output:"]
     [binding-example/component]
     [text
      "Lets again have a look at the DOM:"]
     [code {:lang :html}
      e4]
     [text
      "The classname is verbose but it is "
      [:a {:href "https://clojurescript.org/reference/advanced-compilation#access-from-javascript"} "munged"]
      " on `:advanced` compile, resulting in something munch shorter. More on that "
      [:a {:href "#advanced-compile"} "here"]
      "."]
     [text {:id "named"
            :variant :subheading}
      "Named anonymous"]
     [text
      "Another variation is the named anonymous function, and it is treated the
      same as the local binding variation: "]
     [code {:lang :clojure}
      e5]
     [text {:variant :subheading}
      "Output:"]
     [named-anon-example/component]
     [text
      "The DOM looks the same as the local binding variation."]
     [text {:variant :subheading}
      "Bound anonymous"]
     [text {:id "bound"}
      "Anonymous functions bound to a symbol are still treated as anonymous:"]
     [code {:lang :clojure}
      e6]
     [text
      "Herb does not capture what the symbol name is."]]))
