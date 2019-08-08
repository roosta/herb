(ns site.snippets.my-group-expand)

(defn group-name [component & args]
  (let [style
        (get {:container {:display :flex}
              :component-1 {:background-color :black}
              :component-2 {:background-color :grey}}
             component)]
    (vary-meta
     style assoc
     :hint (name component))))
