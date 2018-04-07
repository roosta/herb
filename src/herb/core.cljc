(ns herb.core
  (:require [herb.impl :as impl]
            #?@(:cljs [[herb.runtime :as runtime]]
                :clj  [[garden.core :as garden]
                       [clojure.tools.analyzer.jvm :as ana.jvm]])))

(def join-classes impl/join-classes)
#?(:cljs (def set-global-style! runtime/set-global-style!)
   :clj (def set-global-style! garden/css))

(defmacro defgroup
  "Define a style group, everything defined in a group is grouped in the same
  style element, It takes a name and a map of styles in the form:
  ```
  (defgroup my-group
    {:a-component {:color \"red\"}})
  ```
  To use a group, use one of `<class` or `<id` macro, where the first argument is
  the key for whatever component stylesheet you want:
  ```
  [:div {:class (<class my-group :a-component)}]
  ```"
  [n c]
  `(defn ~n [~'component & ~'args]
     (if-let [style# (get ~c ~'component)]
       (vary-meta
         style#
         assoc
         :key ~'component
         :group true)
       #?(:cljs (.error js/console "Herb error: failed to get component: " ~'component " in stylegroup: " '~n)
          :clj (throw (str "Herb error: failed to get component: " ~'component " in stylegroup: " '~n))))))

(defmacro with-style
  "**DEPRECATED** Takes a function that returns a map. Arguments can be passed
  along with function as additional arguments to with-style i.e
  `(with-style some-fn arg1 arg2)`.

  Returns a unique identifier based the fully qualified name of the passed
  function"
  [style-fn & args]
  (let [f `'~style-fn
        n (name (ns-name *ns*))]
    `(herb.impl/with-style! nil ~f ~n ~style-fn ~@args)))

(defmacro <id
  "Takes a function `style-fn` that returns a map. Arguments `args` can be passed
  along with the function as additional arguments to <id i.e
  `(<id some-fn arg1 arg2)`.
  Returns a unique id based on the fully qualified name of the passed function "
  [style-fn & args]
  (let [f `'~style-fn
        n (name (ns-name *ns*))]
    `(herb.impl/with-style! {:id? true} ~f ~n ~style-fn ~@args)))

(defmacro <class
  "Takes a function `style-fn` that returns a map. Arguments `args` can be passed
  along with the function as additional arguments to <class i.e
  `(<class some-fn arg1 arg2)`.
  Returns a unique class based on the fully qualified name of the passed function"
  [style-fn & args]
  (let [f `'~style-fn
        n (name (ns-name *ns*))]
    `(herb.impl/with-style! {} ~f ~n ~style-fn ~@args)))


(comment
  (defn fn-name
    [style-fn]
    `(.-name ~style-fn)
    (if (instance? clojure.lang.Named style-fn)
      `(-> #'~style-fn meta :name str) ;`'~style-fn
      nil)))
