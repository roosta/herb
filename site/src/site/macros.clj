(ns site.macros
  (:require [clojure.java.io :as io]))

(defmacro example-src [example]
  (let [g (slurp (str "src/site/snippets/" example))]
    `~g))
