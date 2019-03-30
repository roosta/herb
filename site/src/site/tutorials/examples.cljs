(ns site.tutorials.examples
  (:require
   [garden.units :refer [em px rem]]
   [site.components.code :refer [code]]
   [site.components.paper :refer [paper]]
   [site.components.text :refer [text]]
   [site.snippets.involved :as involved]
   [site.snippets.global :as syntax]
   [herb.core :as herb :refer-macros [<class <id]]
   [reagent.core :as r])
  (:require-macros
   [site.macros :as macros]))

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
      [:a {:href "https://github.com/roosta/herb/tree/master/site"} "site"]
      ". There is also "
      [:a {:href "https://github.com/roosta/herb/blob/master/demo/herbdemo/examples.cljs"} "examples.cljs"]
      " and "
      [:a {:href "https://github.com/roosta/herb/blob/master/demo/herbdemo/examples.clj"} "examples.clj"]
      " that is used during development and contain example components for each
     of Herbs features. It need some serious attention to be truly useful to
     anyone but for now it is what it is."]
     [text "I recently implemented a flexbox grid component using Herb, might be
     worth a "
      [:a {:href "https://github.com/roosta/tincture/blob/master/src/tincture/grid.cljs"} "look"]
      "."]]))
