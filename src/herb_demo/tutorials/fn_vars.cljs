(ns herb-demo.tutorials.fn-vars
  (:require-macros [herb-demo.macros :as macros])
  (:require [herb-demo.components.code :refer [code]]
            [herb-demo.components.paper :refer [paper]]
            [herb-demo.snippets.anon :as anon-example]
            [herb-demo.snippets.local-binding :as binding-example]
            [herb-demo.components.text :refer [text]]
            [herb-demo.snippets.state-fn :as e1]))

(defn fn-vars
  []
  (let [e1 (macros/example-src "anon.cljs")
        e2 (macros/example-src "anon.html")
        e3 (macros/example-src "local_binding.cljs")]
    [paper {:id "fn-vars"}
     [text {:variant :heading}
      "Function variations"]
     [text
      "Herb allows for different function variations, one of the being anonymous
      functions: "]
     [code {:lang :clojure}
      e1]
     [text {:variant :subheading}
      "Output:"]
     [anon-example/link]
     [text
      "As you can see anonymous functions can take the same meta data as a
     defined one, the only real difference between the two is how Herb handles
     the classname. Lets look at a DOM approximation:"]
     [code {:lang :html}
      e2]
     [text
      "The classname that the anonymous function gets is the namespace in which
      it is contained, and `anonymous-HASH`. The hash is calculated from the
      style map it receives."]
     [text
      "Lets take a look at another variation, the local binding form:"]
     [code {:lang :clojure}
      e3]
     [text {:variant :subheading}
      "Output:"]
     [binding-example/component]
     ]))
