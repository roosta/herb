(ns site.snippets.metadata)

;; Attach a pseudo class using metadata shorthand
(defn style-shorthand
  []
  ^{:pseudo {:hover {:text-decoration "underline"}}}
  {:color "blue"})

;; here is the same example using with-meta
(defn style-with-meta
  []
  (with-meta
    {:color "blue"}
    {:pseudo {:hover {:text-decoration "underline"}}}))

;; lastly is vary-meta, this can be useful if you'd want to preserve whatever
;; other meta might be attached.
(defn style-vary-meta
  []
  (vary-meta
   {:color "blue"}
   assoc :key "blue"))
