(ns site.snippets.vendor-local)

(defn style []
  ^{:vendors [:webkit]
    :prefix true}
  {:transition "all 1s ease-in"})
