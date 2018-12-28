(ns site.snippets.keyfn2
  (:require [herb.core :refer-macros [<class]]))

(defn style
  [qty]
  (let [c (cond
            (>= qty 5) "green"
            (and (< qty 5) (> qty 0)) "orange"
            (= qty 0) "red")]
    ^{:key c}
    {:color c}))


(defn component
  []
  [:div
   (map (fn [qty index]
          ^{:key qty}
          [:div {:class (<class style qty)}
           [:span (str "Item " index ". quantity: " qty)]])
     [1 5 6 0 3 10]
     (range))])
