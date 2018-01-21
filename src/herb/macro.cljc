(ns herb.macro
  #?(:clj
     (:require
       [garden.core :refer [css]]
       [garden.stylesheet :refer [at-media at-keyframes]]))
  #?(:cljs
     (:require
       [garden.core :refer [css]]
       [garden.stylesheet :refer [at-media at-keyframes]]
       [herb.runtime])))

(defn cljs-env?
  "Take the &env from a macro, and tell whether we are expanding into cljs."
  [env]
  (boolean (:ns env)))

(def hello "world")

(defmacro defstyle
  [id style]
  (let [css (symbol "garden.core" "css")
        inject-style-fn (symbol "cljs-css-modules.runtime" "inject-style!")]
    `(.log js/console ~id)
    )
  )
