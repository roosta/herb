(ns herb.core
  (:require [herb.impl :as impl]
            [clojure.spec.alpha :as s]
            [clojure.string :as str]
            [herb.runtime :as runtime]
            #?@(:clj [[debux.core :refer [dbg]]]
                :cljs [[debux.cs.core :as d :refer-macros [clog clogn dbg dbgn break]]]))
  #?(:clj
     (:import garden.types.CSSAtRule)))

(s/def ::auto-prefix (s/coll-of keyword? :kind set?))
(s/def ::vendors (s/coll-of (s/or :string string? :keyword keyword?) :kind vector?))
(s/def ::options (s/keys :opt-un [::vendors ::auto-prefix]))

(defn init!
   "Initialize herb, takes a map of options:
  :vendors - a vector of vendor prefixes, ie [:webkit :moz]
  :auto-prefix - A set of CSS properties to auto prefix, ie #{:transition :border-radius} "
  [options]
  (let [parsed (s/conform ::options options)]
    (if (= parsed ::s/invalid)
      (throw (ex-info "Invalid input" (s/explain-data ::options options)))
      (reset! runtime/options {:vendors (-> (mapv (fn [[k v]] v) (:vendors parsed))
                                            (impl/convert-vendors))
                               :auto-prefix (:auto-prefix options)}))))


(defn join-classes
  "Joins multiple classes together, filtering out nils:
  ```
  (join-classes (<class fn-1) (<class fn-2))
  ```"
  [& classes]
  (->> classes
       (filter identity)
       (str/join " ")))

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
           s# `'~sym
           n# (name (ns-name *ns*))
           obj `(CSSAtRule. :keyframes ~value)]
       `(do
          (runtime/inject-obj! (str ~n# "/" ~s#) :keyframes ~obj)
          (def ~sym ~obj)))))

#?(:clj
   (defmacro defglobal
     "Define global CSS:
  ```
  (defglobal some-global-style
    [:body {:box-sizing \"border-box\"
            :font-size (px 14)
    [:button {:border \"none\"}])
  ```
  The CSS output of garden style vectors gets appended to head under
  data-herb=\"global\"
  "

     [sym & styles]
     (let [styles# `(list ~@styles)
           s# `'~sym
           n# (name (ns-name *ns*))]
       `(do
          (runtime/inject-obj! (str ~n# "/" ~s# ) :global ~styles#)
          (def ~sym ~styles#)))))

#?(:clj
   (defmacro <keyframes
     "Returns a CSS string from defined keyframes using the defkeyframes macro.
  Intended to be used from clojure
  ```
  (defkeyframes pulse
    [:from {:opacity 1}]
    [:to {:opacity 0}])

  user=> (<keyframes pulse)
  @keyframes anime {

    from {
      opacity: 1;
    }
    to {
      opacity: 0;
    }
}
  ```"
     [sym]
     `(-> @runtime/injected-keyframes
          (get (str '~sym))
          :css)))

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
          (throw (str "Herb error: failed to get component: " ~'component " in stylegroup: " '~n))))))

#?(:clj
   (defmacro <style
     "Takes a function `style-fn` that returns a map. Arguments `args` can be passed
  along with the function as additional arguments to <style i.e
  `(<style some-fn arg1 arg2)`.
  Returns a CSS string that is the result of calling passed function"
     [style-fn & args]
     (let [f `'~style-fn
           n (name (ns-name *ns*))]
       `(cond
          (not (fn? ~style-fn)) (throw (str "herb error in ns \"" ~n "\" the first argument to <style needs to be a function."))
          (not (map? (~style-fn ~@args))) (throw (str "herb error: style function \"" ~n "/" ~f "\" needs to return a map."))
          :else (herb.impl/with-style! {:style? true} ~f ~n ~style-fn ~@args)))))

#?(:clj
   (defmacro <id
     "Takes a function `style-fn` that returns a map. Arguments `args` can be passed
  along with the function as additional arguments to <id i.e
  `(<id some-fn arg1 arg2)`.
  Returns a unique id based on the fully qualified name of the passed function "
     [style-fn & args]
     (let [f `'~style-fn
           n (name (ns-name *ns*))]
       `(cond
          (not (fn? ~style-fn)) (throw (str "herb error in ns \"" ~n "\" the first argument to <id needs to be a function."))
          (not (map? (~style-fn ~@args))) (throw (str "herb error: style function \"" ~n "/" ~f "\" needs to return a map."))
          :else (herb.impl/with-style! {:id? true} ~f ~n ~style-fn ~@args)))))

#?(:clj
   (defmacro <class
     "Takes a function `style-fn` that returns a map. Arguments `args` can be passed
  along with the function as additional arguments to <class i.e
  `(<class some-fn arg1 arg2)`.
  Returns a unique class based on the fully qualified name of the passed function"
     [style-fn & args]
     (let [f `'~style-fn
           n (name (ns-name *ns*))]
       `(cond
          (not (fn? ~style-fn)) (throw (str "herb error in ns \"" ~n "\" the first argument to <class needs to be a function."))
          (not (map? (~style-fn ~@args))) (throw (str "herb error: style function \"" ~n "/" ~f "\" needs to return a map."))
          :else (herb.impl/with-style! {} ~f ~n ~style-fn ~@args)))))
