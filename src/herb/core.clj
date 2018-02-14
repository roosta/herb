(ns herb.core)

(defmacro with-style
  "Takes a function that returns a map and transform into CSS using garden, inject
  into DOM and return classname"
  [style-fn & args]
  (let [fn-name (if (instance? clojure.lang.Named style-fn)
                  `(-> #'~style-fn meta :name str)
                  nil)
        caller-ns (if (instance? clojure.lang.Named style-fn)
                    `(-> #'~style-fn meta :ns str)
                    (name (ns-name *ns*)))
        ]
    `(do
       (assert (fn? ~style-fn) (str (pr-str ~style-fn) " is not a function. with-style only takes a function as its first argument"))
       (let [resolved-tree# (herb.core/walk-ancestors [[~style-fn ~@args]] [])
             meta# (meta (last resolved-tree#))
             key# (if (keyword? (:key meta#))
                    (name (:key meta#))
                    (:key meta#))
             fn-name# (if-let [f# ~fn-name]
                        f#
                        (str "anonymous-" (hash resolved-tree#)))
             fqn# (str ~caller-ns "/" fn-name#)
             classname# (str (clojure.string/replace ~caller-ns #"\." "_") "_" fn-name# (when key# (str "-" key#)))
             ]
         ;; (.log js/console "Extend-> "(:extend (meta (~style-fn ~@args))))
         ;; (.log js/console "Ours-> " meta#)
         ;; (.log js/console [~@args])
         ;; (.log js/console "Ours-> " (into [] (herb.core/process-meta-xform :key) resolved-tree#))
         #_(assert (map? resolved#) "with-style functions must return a map")
         (let [garden-data# [(str "." classname#)
                               (apply merge {} resolved-tree#)
                               (herb.core/extract-meta resolved-tree# meta# :mode)
                               (herb.core/extract-meta resolved-tree# meta# :media)]]
             (herb.runtime/inject-style! classname# garden-data# fqn#)
             classname#)))))
