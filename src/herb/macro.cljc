(ns herb.macro
  #?(:clj
     (:require
       [garden.core :refer [css]]
       [clojure.string :as str]
       [garden.selectors :as s]
       [garden.stylesheet :refer [at-media at-keyframes]]))
  #?(:cljs
     (:require
       [garden.core :refer [css]]
       [clojure.string :as str]
       [garden.selectors :as s]
       [garden.stylesheet :refer [at-media at-keyframes]]
       [herb.runtime])))

(defn cljs-env?
  "Take the &env from a macro, and tell whether we are expanding into cljs."
  [env]
  (boolean (:ns env)))

(defn convert-modes
  [modes]
  (mapv #(-> [(keyword (str "&" %)) (% modes)])
        (keys modes)))

(defmacro with-style
  [style-fn & args]
  (let [css (symbol "garden.core" "css")
        ns# (str/replace (name (ns-name *ns*)) #"\." "_")
        classname (str ns# "_" style-fn)
        inject-style-fn (symbol "herb.runtime" "inject-style!")]
    `(let [resolved# (~style-fn ~@args)
           meta# (meta resolved#)
           modes# (:mode meta#)
           key# (:key meta#)
           classname# (str ~classname (when key# (str "-" key#)))
           css# (css [(str "." classname#) resolved#
                      (convert-modes modes#)])]
       (~inject-style-fn classname# css#)
       ;; (.log js/console classname#)
       classname#)
    ;; `(do (.log js/console ~classname)
    ;;      "asd")

    )
  )
