(ns herb.core
  (:require
   [garden.core :refer [css]]
   [clojure.string :as str]
   [garden.selectors :as s]
   [garden.stylesheet :refer [at-media at-keyframes]]
   [herb.runtime :as runtime])
  (:require-macros [herb.core :refer [with-style]]))

(def dev? ^boolean js/goog.DEBUG)

(defn set-global-style!
  "Takes a collection of Garden style vectors, and create or update the global style element"
  [& styles]
  (let [element (.querySelector js/document "style[data-herb=\"global\"]")
        head (.-head js/document)
        css-str (css styles)]
    (assert (some? head) "An head element is required in the dom to inject the style.")
    (if element
      (set! (.-innerHTML element) css-str)
      (let [element (.createElement js/document "style")]
        (set! (.-innerHTML element) css-str)
        (.setAttribute element "type" "text/css")
        (.setAttribute element "data-herb" "global")
        (.appendChild head element)))))

(defn join-classes
  [coll]
  (->> coll
      (filter identity)
      (str/join " " coll)))

(defn- convert-modes
  [modes]
  (map
   (fn [[kw mode]]
     [(keyword (str "&" kw)) mode])
   modes))

(defn- convert-media
  [media]
  (map (fn [[query style]]
         (at-media query [:& style]))
       media))

(defn- resolve-style-fns
  "Calls each function provided in a collection of style-fns. Input can take
  multiple forms depending on how it got called from the consumer side either
  using the macro directly or via extend meta data.
  Takes a collection of `style-fns` and a collection `result` that is returned
  with the resolved style maps."
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

(defn- process-meta-xform
  "Return a transducer that pulls out a given meta type from a sequence and filter
  out nil values"
  [meta-type]
  (comp
   (map meta)
   (map meta-type)
   (filter identity)))

(defn- extract-styles
  "Extract all the `:extend` meta, ensuring what we walk the entire tree, passing
  each extend vector of style-fns to `resolve-style-fns` for resolution.
  Takes a collection of `style-fns` and a result collection that is returned
  with the resolved styles"
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

(defn- extract-meta
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

(defn- prepare-styles
  "Prepare `resolved-styles` so they can be passed to `garden.core/css` Merge
  the styles to remove duplicate entries and ensuring precedence. Extract all
  meta and return a final vector of styles including meta."
  [resolved-styles]
  [(apply merge {} resolved-styles)
   (extract-meta resolved-styles :mode)
   (extract-meta resolved-styles :media)])

(defn- garden-data
  "Takes a classnames and a resolved style vector and returns a vector with
  classname prepended"
  [identifier styles id?]
  (into [(str (if id? "#" ".") identifier)] styles))

(defn- sanitize
  "Takes `input` and remove any non-valid characters"
  [input]
  (when input
    (cond
      (keyword? input) (sanitize (name input))
      :else (str/replace (str input) #"[^A-Za-z0-9-_]" "_"))))

(defn- compose-identifier
  [n k]
  (str (sanitize n)
       (when k
         (str "-" (sanitize k)))))

(defn- compose-data-string
  [n key]
  (str
   (str/replace n #"\." "/")
   (when (and dev? key) (str "[" key "]"))))

(defn with-style!
  "Entry point for macros.
  Takes an `opt` map as first argument, and currently only
  supports `:id true` which appends an id identifier instead of a class to the DOM"
  [opts fn-name ns-name style-fn & args]
  (let [resolved-styles (extract-styles (into [style-fn] args) [])
        prepared-styles (prepare-styles resolved-styles)
        meta-data (-> resolved-styles last meta)
        n (.-name style-fn)
        hash* (.abs js/Math (hash prepared-styles) -1)
        name* (cond
                (and (empty? n) (not dev?)) (str "A-" hash*)
                (and dev? (empty? n)) (str ns-name "/" "anonymous-" hash*)
                (and dev? (not (empty? n))) (demunge n)
                :else n)
        data-str (compose-data-string name* (:key meta-data))
        identifier (compose-identifier name* (:key meta-data))
        garden-data (garden-data identifier prepared-styles (:id? opts))]
    (runtime/inject-style! identifier garden-data data-str)
    identifier))
