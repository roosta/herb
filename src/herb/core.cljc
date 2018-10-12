(ns herb.core
  (:require [herb.impl :as impl]
            [herb.runtime :as runtime]
            #?@(:clj [[debux.core :refer [dbg]]]
                :cljs [[debux.cs.core :as d :refer-macros [clog clogn dbg dbgn break]]])
            )

  #?(:clj
     (:import garden.types.CSSAtRule)))

;; Aliases
(def join-classes impl/join-classes)
(def global-style! runtime/global-style!)

#?(:clj
   (defmacro defkeyframes
     "Define a CSS @keyframes animation:
  ```
  (defkeyframes my-animation
        [:from
         {:background \"red\"}]

        [:to
         {:background \"yellow\"}])
  ```
  CLJS: the keyframes CSS gets injected into head under data-herb=\"keyframes\"
  CLJ: Use `<keyframes` macro with the defined keyframes returns a CSS string
       containing the animation"
     [sym & frames]
     (let [value {:identifier `(str '~sym)
                  :frames `(list ~@frames)}
           obj `(CSSAtRule. :keyframes ~value)]
       `(do
          (runtime/inject-keyframes! (str '~sym) ~obj)
          (def ~sym ~obj)))))

#?(:clj
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
             :clj (throw (str "Herb error: failed to get component: " ~'component " in stylegroup: " '~n)))))))

#?(:clj
   (defmacro <style
     "Takes a function `style-fn` that returns a map. Arguments `args` can be passed
  along with the function as additional arguments to <style i.e
  `(<style some-fn arg1 arg2)`.
  Returns a CSS string that is the result of calling passed function"
     [style-fn & args]
     (let [f `'~style-fn
           n (name (ns-name *ns*))]
       `(herb.impl/with-style! {:style? true} ~f ~n ~style-fn ~@args))))

#?(:clj
   (defmacro <id
     "Takes a function `style-fn` that returns a map. Arguments `args` can be passed
  along with the function as additional arguments to <id i.e
  `(<id some-fn arg1 arg2)`.
  Returns a unique id based on the fully qualified name of the passed function "
     [style-fn & args]
     (let [f `'~style-fn
           n (name (ns-name *ns*))]
       `(herb.impl/with-style! {:id? true} ~f ~n ~style-fn ~@args))))

#?(:clj
   (defmacro <class
     "Takes a function `style-fn` that returns a map. Arguments `args` can be passed
  along with the function as additional arguments to <class i.e
  `(<class some-fn arg1 arg2)`.
  Returns a unique class based on the fully qualified name of the passed function"
     [style-fn & args]
     (let [f `'~style-fn
           n (name (ns-name *ns*))]
       `(herb.impl/with-style! {} ~f ~n ~style-fn ~@args))))
