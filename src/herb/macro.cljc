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

(defn extract-extend-meta
  "Parses extend metadata to return a vector of style maps"
  [extend-meta]
  (cond
    (fn? extend-meta) [(extend-meta)]
    (vector? extend-meta) (resolve-styles extend-meta [])
    (nil? extend-meta) []
    ;; TODO fix js/error
    :else (throw `(js/Error. ~(str "extend metadata does not conform to spec: " (pr-str extend-meta))))))

;; 1. input: [[dynamic-text-color color] bold]
;; 2. extracted [{:color "black"} {:font-weight "bold"}]
;; 3. new meta: [fn italic]
(defn parse-ancestors
  "Recursivly go through each extend function provided in extend meta and resolve
  style for each until we have nothing left, then return a flat vector of the
  extend chain ready to be fed into garden"
  [extend-meta result]
  (if (empty? extend-meta)
    result
    (let [extracted (extract-extend-meta extend-meta)
          new-meta (into [] (filter identity (map (comp :extend meta) extracted)))]
      (recur new-meta
             (apply conj result extracted)))))

(defmacro with-style
  "Takes a function that returns a map and transform into CSS using garden, inject
  into DOM and return classname"
  [style-fn & args]
  (let [css (symbol "garden.core" "css")
        ns# (str/replace (name (ns-name *ns*)) #"\." "_")
        classname (str ns# "_" style-fn)
        inject-style-fn (symbol "herb.runtime" "inject-style!")]
    `(do
       (assert (fn? ~style-fn) (str (pr-str ~style-fn) " is not a function. with-style only takes a function as its first argument"))
       (let [resolved# (~style-fn ~@args)
             meta# (meta resolved#)
             modes# (:mode meta#)
             ancestors# (parse-ancestors (:extend meta#) [])
             key# (:key meta#)
             classname# (str ~classname (when key# (str "-" key#)))
             out# (apply merge resolved# ancestors#)]
         (assert (map? resolved#) "with-style functions must return a map")
         (let [css# (css [(str "." classname#) out#
                          (convert-modes modes#)])]
           (~inject-style-fn classname# css#)
           classname#)))))
