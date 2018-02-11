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
                    (name (ns-name *ns*)))]
    `(do
       (assert (fn? ~style-fn) (str (pr-str ~style-fn) " is not a function. with-style only takes a function as its first argument"))
       (let [resolved# (~style-fn ~@args)
             fqn# (str ~caller-ns "/" ~fn-name)
             meta# (meta resolved#)
             ancestors# (herb.core/walk-ancestors (:extend meta#) [])
             key# (if (keyword? (:key meta#))
                    (name (:key meta#))
                    (:key meta#))
             fn-name# (if-let [f# ~fn-name]
                        f#
                        (str "anonymous-" (hash (str resolved# meta#))))
             classname# (str (clojure.string/replace ~caller-ns #"\." "_") "_" fn-name# (when key# (str "-" key#)))]
         (assert (map? resolved#) "with-style functions must return a map")
         (let [garden-data# [(str "." classname#)
                               (apply merge {} (into ancestors# resolved#))
                               (herb.core/extract-meta ancestors# meta# :mode)
                               (herb.core/extract-meta ancestors# meta# :media)]]
             (herb.runtime/inject-style! classname# garden-data# fqn#)
             classname#)))))
