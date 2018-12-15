(ns herb-demo.tutorials.examples
  (:require
   [garden.units :refer [em px rem]]
   [herb-demo.components.code :refer [code]]
   [herb-demo.components.paper :refer [paper]]
   [herb-demo.components.text :refer [text]]
   [herb-demo.snippets.involved :as involved]
   [herb-demo.snippets.global :as syntax]
   [herb.core :as herb :refer-macros [<class <id]]
   [reagent.core :as r])
  (:require-macros
   [herb-demo.macros :as macros]))

(defn main []
  (let [e1 (macros/example-src "involved.cljs")]
    [paper {:id "examples"}
     [text {:variant :heading}
      "Examples and extras"]
     [text
      "I'm including this extra example here, it never fit anywhere else because
    it was to involved to used in a tutorial. It does nevertheless demonstrate
    herb advanced usage and pairing with garden"]
     [code {:lang :clojure}
      e1]
     [text {:variant :subheading}
      "Output:"]
     [involved/component]
     [text "Need more examples? This site is made using Herb. It's located under "
      [:a {:href "https://github.com/roosta/herb/tree/master/src/herb_demo"} "src/herb_demo"]
      ". There is also "
      [:a {:href "https://github.com/roosta/herb/blob/master/src/herb_demo/examples.cljs"} "examples.cljs"]
      " and "
      [:a {:href "https://github.com/roosta/herb/blob/master/src/herb_demo/examples.cljs"} "examples.clj"]
      " that is used during development and contain example components
     for each of Herbs features. It need some serious attention to be truly
     useful to anyone but for now it is what it is. To check out the examples output go to "
      [:a {:href "/examples"} "/examples"]]]))
