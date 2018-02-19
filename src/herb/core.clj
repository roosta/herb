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
  [style & args]
  (let [style-name (if (instance? clojure.lang.Named style)
                  `(-> #'~style meta :name str) ; `'~style-fn
                  nil)
        caller-ns (if (instance? clojure.lang.Named style)
                    `(-> #'~style meta :ns str)
                    (name (ns-name *ns*)))]
    `(do
       #_(assert (fn? ~style) (str (pr-str ~style) " is not a function. with-style only takes a function as its first argument"))
       (let [resolved-styles# (herb.core/extract-styles [[~style ~@args]] [])
             prepared-styles# (herb.core/prepare-styles resolved-styles#)
             meta# (meta (last resolved-styles#))
             key# (herb.core/sanitize (:key meta#))
             name# (or ~style-name (str "anonymous-" (hash prepared-styles#)))
             data-str# (str ~caller-ns "/" name# "[" ~@args "]")
             classname# (str (herb.core/sanitize ~caller-ns) "_" name# (str "-" key#))
             garden-data# (herb.core/garden-data classname# prepared-styles#)]
         (herb.runtime/inject-style! classname# garden-data# data-str#)
         classname#))))
