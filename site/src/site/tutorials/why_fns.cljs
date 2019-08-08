(ns site.tutorials.why-fns
  (:require
   [herb.core :refer [<class <id] :as herb]
   [site.components.text :refer [text]]
   [site.components.code :refer [code]]
   [site.snippets.state-fn :as e1]
   [site.components.paper :refer [paper]]
   [garden.units :refer [rem em px]]
   [reagent.debug :as d]
   [reagent.core :as r])
  (:require-macros
   [site.macros :as macros]))

(defn main
  []
  (let [e1 (macros/example-src "state_fn.cljs")
        e2 (macros/example-src "state_fn.html")
        e3 (macros/example-src "state_fn_clicked.html")]
    [paper {:id "why-fns"}
     [text {:variant :heading}
      "Why functions?"]
     [text
      "The whole idea for Herb came from the fact that alot of the time I needed
      some kind of stateful CSS. I had a component or element that in some way
      or another needed its style changed based on some state. There are a bunch
      of ways to deal with this, but most of them involve either inline styles
      or swapping out classnames, either of which is ideal in my opinion."]
     [text
      "The way Herb tackles this is by using functions as the style delivery
      method so to speak."]
     [code {:lang :clojure}
      e1]
     [text {:variant :subheading}
      "Output:"]
     [e1/button]
     [text
      "Initially before clicking the button the DOM looks something like this:"]
     [code {:lang :html}
      e2]
     [text
      "Here we can see that the classname reflects the namespace of
      the style function, and importantly a numeric sequence at the
      end, being the hash of the computed styles based on passed
      arguments. When we click the button, the style data changes and
      new styles are appended to the DOM:"]
     [code {:lang :html}
      e3]
     [text
      "If you think these classnames are super verbose, don't
     worry. Herb "
      [:a {:href "https://clojurescript.org/reference/advanced-compilation#access-from-javascript"} "munges"]
      " function names in line with the closure compiler so on a
     production build the classnames are much shorter. More on that "
      [:a {:href "#advanced-compile"} "here"]
      "."]]))
