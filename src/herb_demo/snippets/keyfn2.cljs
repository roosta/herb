(ns herb-demo.snippets.keyfn2
  (:require [herb.core :refer-macros [<class]]))

(defn style
  [qty]
  (let [c (cond
            (> qty 5) "green"
            (<= qty 5) "red")]
    ^{:key c}
    {:color c}))


(defn component
  []
  [:div
   (map (fn [qty index]
          [:div {:class (<class style qty)}
           [:span (str "Item " index ". quantity: " qty)]])
     [1 5 6 0 3 10]
     (range))])
