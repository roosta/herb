(ns demo.macros
  (:require [clojure.java.io :as io]))

(defmacro example-src [example]
  (let [g (->>
           (str "src/demo/examples/" example)
           (slurp))]
    `~g))
