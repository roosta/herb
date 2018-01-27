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

(defn- extract-styles
  "Extracts styles from vector provided in the :merge metadata."
  [mergers result]
  (if (empty? mergers)
    result
    (let [input (first mergers)]
      (if (fn? input)
        (recur (rest mergers)
               (conj result (input)))
        (let [style-fn (first input)
              style-args (rest input)]
          (recur
           (rest mergers)
           (conj result (apply style-fn style-args))))))))

(defn- extract-ancestors
  [mergers]
  (cond
    (fn? mergers) [(mergers)]
    (vector? mergers) (extract-styles mergers [])
    (nil? mergers) []
    :else (throw `(js/Error. ~(str "Herb: merge vector does not conform to spec: " (pr-str mergers)))))
  )

(defmacro with-style
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
             mergers# (:merge meta#)
             ancestors# (extract-ancestors mergers#)
             key# (:key meta#)
             classname# (str ~classname (when key# (str "-" key#)))
             out# (apply merge resolved# ancestors#)]
         (assert (map? resolved#) "with-style functions must return a map")
         (let [css# (css [(str "." classname#) out#
                          (convert-modes modes#)])]
           (~inject-style-fn classname# css#)
           classname#)))))