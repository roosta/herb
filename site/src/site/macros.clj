(ns site.macros
  (:require [clojure.java.io :as io]))

(defmacro example-src [example]
  (let [g (->>
           (str "src/site/examples/" example)
           (slurp))]
    `~g))
