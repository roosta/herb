(ns demo.components.code
  (:require [js.highlight]
            [goog.dom :as dom]
            [reagent.core :as r]))

(defn code
  [c]
  (let [el (r/atom nil)]
    (r/create-class
     {:component-did-mount #(.highlightBlock js/hljs @el)
      :reagent-render
      (fn []
        [:pre {:ref #(reset! el %)}
         [:code
          c]])})))
