(ns herb-demo.examples
  (:require [herb.core :refer [<class <style defgroup]]))

(defgroup group-test
  {:group-1 {:color "red"}
   :group-2 {:color "green"}})

(defn colors
  []
  {:color "red"
   :background-color "cyan"})

(defn test-class
  []
  (<class colors))

(defn test-anon
  []
  (<class (fn [] {:color "red"})))
