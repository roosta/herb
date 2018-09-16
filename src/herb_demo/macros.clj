(ns herb-demo.macros
  (:require [clojure.java.io :as io]))

(defmacro example-src [example]
  (let [g (->>
           (str "src/herb_demo/examples/" example)
           (slurp))]
    `~g))
