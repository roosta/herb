(ns site.components.code
  (:require [js.highlight]
            [goog.dom :as dom]
            [reagent.core :as r]))

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
