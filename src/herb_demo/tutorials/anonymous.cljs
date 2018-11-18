(ns herb-demo.tutorials.anonymous
  (:require [herb-demo.components.code :refer [code]]
            [herb-demo.components.paper :refer [paper]]
            [herb-demo.components.text :refer [text]]
            [herb-demo.snippets.state-fn :as e1]))

(defn anonymous
  []
    [paper {:id "anonymous"}
     [text {:variant :heading}
      "Anonymous functions"]
     [text
      ""]])
