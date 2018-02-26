(ns herb.core
  (:require
   [garden.core :refer [css]]
   [clojure.string :as str]
   [garden.selectors :as s]
   [garden.stylesheet :refer [at-media at-keyframes]]
   [herb.runtime])
  (:require-macros [herb.core :refer [with-style]]))

(defn set-global-style!
  "Take garden style vectors, and create or update the global style"
  [& styles]
  (let [element (.querySelector js/document "style[data-herb=\"global\"]")
        head (.-head js/document)
        css-str (css styles)]
    (assert (some? head) "An head element is required in the dom to inject the style.")
    (if element
      (do
        (.log js/console element)
        (set! (.-innerHTML element) css-str))
      (let [element (.createElement js/document "style")]
        (set! (.-innerHTML element) css-str)
        (.setAttribute element "type" "text/css")
        (.setAttribute element "data-herb" "global")
        (.appendChild head element)))))

(defn convert-modes
  "Takes a map of modes and returns a formatted vector fed into garden using
  the :&:mode parent selector syntax"
  [modes]
  (map
   (fn [[kw mode]]
     [(keyword (str "&" kw)) mode])
   modes))

(defn convert-media
  "Takes a vector of media queries i.e [{:screen true} {:color \"green\"}] and
  calls at-media for each query, and use garden's ancestor selector (:&) to
  target current classname."
  [media]
  (map (fn [[query style]]
         (at-media query [:& style]))
       media))

(defn resolve-style-fns
  "Calls each function provided in a collection of style-fns. Input can take
  multiple forms depending on how it got called from the consumer side either
  using the macro directly or via extend meta data"
  [style-fns result]
  (if (empty? style-fns)
    result
    (let [input (first style-fns)]
      (cond
        (fn? input)
        (conj result (apply input (rest style-fns)))

        (and (coll? input) (fn? (first input)))
        (let [style-fn (first input)
              style-args (rest input)]
          (recur
           (rest style-fns)
           (conj result (apply style-fn style-args))))
        :else (recur
               (rest style-fns)
               (into result (resolve-style-fns input [])))))))

(defn process-meta-xform
  "Return a transducer that pulls out a given meta type from a sequence and filter
  out nil values"
  [meta-type]
  (comp
   (map meta)
   (map meta-type)
   (filter identity)))

(defn extract-styles
  "Walk the entire style tree, resolving each ancestor via the :extend meta data
  The end result of this function is a vector of resolved styles in the form:
  [{:color \"green\"} {:font-weight \"bold\"}]"
  [style-fns result]
  (cond

    (fn? style-fns)
    (recur [style-fns] result)

    (and (vector? style-fns) (not (empty? style-fns)))
    (let [styles (resolve-style-fns style-fns [])
          new-meta (into [] (process-meta-xform :extend) styles)]
      (recur new-meta
             (into styles result)))
    :else result))

(defn extract-meta
  "Takes a group of resolved styles and a meta type. Pull out each meta obj and
  merge to prevent duplicates, finally convert to garden acceptable input and
  return"
  [styles meta-type]
  (let [convert-fn (case meta-type
                     :media convert-media
                     :mode convert-modes)
        extracted (into [] (process-meta-xform meta-type) styles)
        merged (apply merge {} extracted)
        converted (convert-fn merged)]
    converted))

(defn prepare-styles
  "Takes a styles vector and applies merge to remove duplicate entries while
  preserving inheritance precedence, while also extracting metadata"
  [styles]
  [(apply merge {} styles)
   (extract-meta styles :mode)
   (extract-meta styles :media)])

(defn garden-data
  "Takes a classnames and a resolved style vector and returns a vector with
  classname prepended"
  [classname styles]
  (into [(str "." classname)] styles))

(defn sanitize
  [k]
  (when k
    (cond
      (keyword? k) (name k)
      :else (str/replace (str k) #"[^A-Za-z0-9-_]" "_"))))
