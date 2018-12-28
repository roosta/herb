(ns site.snippets.symbol-fn
  (:require [herb.core :refer [<class]]))

(defn component
  []
  (let [style (fn []
                {:color "white"})]
    [:div {:class (<class style)}]))
