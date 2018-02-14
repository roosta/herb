(ns herb.core
  (:require
   [garden.core :refer [css]]
   [clojure.string :as str]
   [garden.selectors :as s]
   [garden.stylesheet :refer [at-media at-keyframes]]
   [herb.runtime])
  (:require-macros [herb.core :refer [with-style]]))

(defn convert-modes
  "Takes a map of modes and returns a formatted vector fed into garden using
  the :&:mode parent selector syntax"
  [modes]
  (map #(-> [(keyword (str "&" %)) (% modes)])
        (keys modes)))

(defn convert-media
  "Takes a vector of media queries i.e [{:screen true} {:color \"green\"}] and
  calls at-media for each query, and use garden's ancestor selector (:&) to
  target current classname."
  [media]
  (map (fn [[query style]]
         (at-media query [:& style]))
       media))

(defn resolve-styles
  "Calls each function provided in extend-meta to resolve style maps for each"
  [extend-meta result]
  (if (empty? extend-meta)
    result
    (let [input (first extend-meta)]
      (cond

        ;; herb supports passing only the name of an fn
        (fn? input)
        (recur (rest extend-meta)
               (conj result (input)))

        ;; if you want to pass fns with args the syntax is [[style-fn args]]
        (vector? (first input))
        (let [style-fn (first (first input))
              style-args (rest (first input))]
          (recur
           (rest extend-meta)
           (conj result (apply style-fn style-args))))

        ;; lastly if you want to pass multiple style fns with no arguments the syntax is [first-fn second-fn]
        :else (let [style-fn (first input)
                    style-args (rest input)]
                (recur
                 (rest extend-meta)
                 (conj result (apply style-fn style-args))))))))

(defn process-meta-xform
  "Return a transducer that pulls out a given meta type from a sequence"
  [meta-type]
  (comp
   (map (comp meta-type meta))
   (filter identity)))

(defn walk-ancestors
  "Recursivly go through each extend function provided in extend meta and resolve
  style for each until we have nothing left, then return a flat vector of the
  extend chain ready to be fed into garden"
  [style-fns result]
  (cond

    (fn? style-fns)
    (recur [style-fns] result)

    (and (vector? style-fns) (not (empty? style-fns)))
    (let [styles (resolve-styles style-fns [])
          new-meta (into [] (process-meta-xform :extend) styles)]
      (.log js/console new-meta)
      (recur new-meta
             (into styles result)))
    :else result))

;; (apply merge {} (into ancestors# resolved#))

(defn extract-meta
  "Takes a group of ancestors and the root style fn meta and meta type. Pull out
  each meta obj and merge to prevent duplicates, finally convert to garden
  acceptable input and return"
  [ancestors# root-meta meta-type]
  (let [convert-fn (case meta-type
                     :media convert-media
                     :mode convert-modes)
        extracted (into [] (process-meta-xform meta-type) ancestors#)
        merged (apply merge {} (conj extracted (meta-type root-meta)))
        converted (convert-fn merged)]
    converted))
