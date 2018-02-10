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

(defn- cljs-env?
  "Take the &env from a macro, and tell whether we are expanding into cljs."
  [env]
  (boolean (:ns env)))

(defn- convert-modes
  "Takes a map of modes and returns a formatted vector fed into garden using
  the :&:mode parent selector syntax"
  [modes]
  (mapv #(-> [(keyword (str "&" %)) (% modes)])
        (keys modes)))

(defn convert-media
  [media]
  (mapv (fn [[query style]]
          (at-media query
                    [:& style]))
        (partition 2 media)))

(defn resolve-modes
  [new-meta result]
  (if (empty? new-meta)
    result
    (let [input (first new-meta)]
      (recur (rest new-meta)
             (conj result (convert-modes input))))))

(defn resolve-styles
  "Calls each function provided in extend-meta to resolve style maps for each"
  [parsed-meta result]
  (if (empty? parsed-meta)
    result
    (let [input (first parsed-meta)]
      (if (fn? input)
        (recur (rest parsed-meta)
               (conj result (input)))
        (let [style-fn (first input)
              style-args (rest input)]
          (recur
           (rest parsed-meta)
           (conj result (apply style-fn style-args))))))))

(def process-extend-meta (comp
                          (map (comp :extend meta))
                          (filter identity)))

(def process-mode-meta (comp
                        (map (comp :mode meta))
                        (filter identity)))

(def process-media-meta (comp
                        (map (comp :media meta))
                        (filter identity)))


(defn parse-ancestors
  "Recursivly go through each extend function provided in extend meta and resolve
  style for each until we have nothing left, then return a flat vector of the
  extend chain ready to be fed into garden"
  [extend-meta result]
  (cond

    (fn? extend-meta)
    (recur [extend-meta] result)

    (and (vector? extend-meta) (not (empty? extend-meta)))
    (let [styles (resolve-styles extend-meta [])
          new-meta (into [] process-extend-meta styles)]
      (recur new-meta
             (apply conj result styles)))
    :else result))


(defmacro with-style
  "Takes a function that returns a map and transform into CSS using garden, inject
  into DOM and return classname"
  [style-fn & args]
  (let [css (symbol "garden.core" "css")
        inject-style-fn (symbol "herb.runtime" "inject-style!")]
    `(do
       (assert (fn? ~style-fn) (str (pr-str ~style-fn) " is not a function. with-style only takes a function as its first argument"))
       (let [resolved# (~style-fn ~@args)
             fn-name# (-> #'~style-fn meta :name str)
             caller-ns# (-> #'~style-fn meta :ns str)
             fqn# (str caller-ns# "/" fn-name#)
             meta# (meta resolved#)
             modes# (convert-modes (:mode meta#))
             media# (convert-media (:media meta#))
             ancestors# (parse-ancestors (:extend meta#) [])
             key# (if (keyword? (:key meta#))
                    (name (:key meta#))
                    (:key meta#))
             classname# (str (str/replace caller-ns# #"\." "_") "_" fn-name# (when key# (str "-" key#)))]
         (assert (map? resolved#) "with-style functions must return a map")
         (let [garden-data# [(str "." classname#)
                             (apply merge {} (into ancestors# resolved#))
                             (into modes# (mapv convert-modes (into [] process-mode-meta ancestors#)))
                             (into media# (mapv convert-media (into [] process-media-meta ancestors#)))
                             ]]
           (~inject-style-fn classname# garden-data# fqn#)
           ;; (.log js/console ancestors#)
           classname#)))))
