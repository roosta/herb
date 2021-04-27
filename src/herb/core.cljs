(ns herb.core
  (:require-macros [herb.core])
  (:require [herb.impl :as impl]
            [herb.spec]
            [clojure.spec.alpha :as s]
            [clojure.string :as str]
            [herb.runtime :as runtime]))

(defn init!
   "Initialize herb, takes a map of options:
  :vendors - a vector of vendor prefixes, ie [:webkit :moz]
  :auto-prefix - A set of CSS properties to auto prefix, ie #{:transition :border-radius} "
  [options]
  (let [parsed (s/conform :herb.spec/options options)]
    (if (= parsed ::s/invalid)
      (throw (ex-info "Invalid input" (s/explain-data :herb.spec/options options)))
      (reset! runtime/options {:vendors (-> (mapv (fn [[_ v]] v) (:vendors parsed))
                                            (impl/convert-vendors))
                               :auto-prefix (:auto-prefix options)}))))

(defn join
  "Joins multiple classes together, filtering out nils:
  ```clojure
  (join (<class fn-1) (<class fn-2))
  ```"
  [& classes]
  (if (s/valid? :herb.spec/classes classes)
    (->> classes
         (filter identity)
         (str/join " "))
    (throw (ex-info "join takes one or more strings as arguments" (s/explain-data :herb.spec/classes classes)))))
