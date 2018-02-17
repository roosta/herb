(ns herb.core)

(defmacro with-style
  "Takes a function that returns a map. Arguments can be passed along with
  function as additional arguments to with-style i.e (with-style some-fn arg1 arg2)

  The style function is resolved and the map it returns is what is used
  by [garden](https://github.com/noprompt/garden) to create a stylesheet.

  Returns a unique classname based of the fully qualified name of the passed
  function, with the exception of anonymous functions that will get a unique
  name based on the hash of the resolved styles.

  The syntax for the style map is wholesale garden, but since our selector is
  the returned classname some adaptations needed to be made regarding
  pseudo-class selectors, media/feature queries, vendor prefixes and keyframes.
  This takes the form of meta data passed along with the returned style map

  :mode is pseudo-class selectors:
  `{:mode {:hover {:color \"red\"}
           :focus {:color \"green}}}`

  :media queries
  `{:media {{:screen true} {:width \"200px\"}}
            {:max-width (px 412)} {:display \"none\"}}`

  On more meta type that is supported is the :extend meta. A style-fn can
  inherit from an arbitrary number of other style functions. The syntax is as
  follows:
  `{:extend single-style-fn}
   {:extend [style-fn1 style-fn2]}
   {:extend [[style-fn arg1 arg2]]}`"
  [style-fn & args]
  (let [fn-name (if (instance? clojure.lang.Named style-fn)
                  `(-> #'~style-fn meta :name str) ; `'~style-fn
                  nil)
        caller-ns (if (instance? clojure.lang.Named style-fn)
                    `(-> #'~style-fn meta :ns str)
                    (name (ns-name *ns*)))]
    `(do
       (assert (fn? ~style-fn) (str (pr-str ~style-fn) " is not a function. with-style only takes a function as its first argument"))
       (let [resolved-styles# (herb.core/extract-styles [[~style-fn ~@args]] [])
             meta# (meta (last resolved-styles#))
             key# (if (keyword? (:key meta#))
                    (name (:key meta#))
                    (:key meta#))
             fn-name# (if-let [f# ~fn-name]
                        f#
                        (str "anonymous-" (hash resolved-styles#)))
             fqn# (str ~caller-ns "/" fn-name#)
             classname# (str (clojure.string/replace ~caller-ns #"\." "_")
                             "_"
                             fn-name#
                             (when key# (str "-" key#)))
             garden-data# (herb.core/garden-data classname# resolved-styles#)]
         (herb.runtime/inject-style! classname# garden-data# fqn#)
         classname#))))
