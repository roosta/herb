(ns herb.core)

(comment
  (defn fn-name
    [style-fn]
    `(.-name ~style-fn)
    (if (instance? clojure.lang.Named style-fn)
      `(-> #'~style-fn meta :name str) ;`'~style-fn
      nil)))

(defmacro with-style
  "**DEPRECATED** Takes a function that returns a map. Arguments can be passed
  along with function as additional arguments to with-style i.e
  `(with-style some-fn arg1 arg2)`.

  Returns a unique identifier based the fully qualified name of the passed
  function"
  [style-fn & args]
  (let [f `'~style-fn
        n (name (ns-name *ns*))]
    `(herb.core/with-style! nil ~f ~n ~style-fn ~@args)))

(defmacro <id
  "Takes a function that returns a map. Arguments can be passed along with
  function as additional arguments to <id i.e
  `(<id some-fn arg1 arg2)`.

  Returns a unique id based on the fully qualified name of the passed function "
  [style-fn & args]
  (let [f `'~style-fn
        n (name (ns-name *ns*))]
    `(herb.core/with-style! {:id? true} ~f ~n ~style-fn ~@args)))

(defmacro <class
  "Takes a function that returns a map. Arguments can be passed along with
  function as additional arguments to <class i.e
  `(<class some-fn arg1 arg2)`.

  Returns a unique class based on the fully qualified name of the passed
  function "
  [style-fn & args]
  (let [f `'~style-fn
        n (name (ns-name *ns*))]
    `(herb.core/with-style! {} ~f ~n ~style-fn ~@args)))
