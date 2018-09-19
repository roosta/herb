(ns herb-demo.components.code
  (:require [reagent.core :as r]
            [cljsjs.highlight]
            [cljsjs.highlight.langs.clojure]
            [cljsjs.highlight.langs.xml]
            ))

(defn code
  [{:keys [lang]} content]
  (let [el (r/atom nil)]
    (r/create-class
     {:component-did-mount #(.highlightBlock js/hljs @el)
      :reagent-render
      (fn []
        [:pre {:class (name lang)
               :ref #(reset! el %)}
         [:code
          content]])})))
