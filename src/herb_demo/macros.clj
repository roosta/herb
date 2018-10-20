(ns herb-demo.macros
  (:require [clojure.java.io :as io]))

(defmacro example-src [example]
  (let [g (slurp (str "src/herb_demo/snippets/" example))]
    `~g))
