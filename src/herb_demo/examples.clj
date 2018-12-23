(ns herb-demo.examples
  (:require [herb.core
             :refer [<class <style defgroup defkeyframes <keyframes]]))

(defgroup group-test
  {:group-1 {:color "red"}
   :group-2 {:color "green"}})

(defn colors
  []
  {:color "red"
   :background-color "cyan"})


(defkeyframes anime
  [:from {:opacity 1}]
  [:to {:opacity 0}]
  )

(defn test-class
  []
  (<class colors))

(defn test-anon
  []
  (<class (fn [] {:color "red"})))

(defn test-nested
  []
  (letfn [(nested []
            {:color "red"})]
    (<class nested))
  )
