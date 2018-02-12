(ns herb.runtime
  (:require [garden.core :refer [css]]
            [goog.dom :as dom]
            [clojure.string :as str]))

(def injected-styles (atom {}))

(defn update-style!
  "Create css string and update DOM"
  [classname element new]
  (let [css (css new)]
    (swap! injected-styles assoc classname {:data new
                                            :element element})
    (set! (.-innerHTML element) css)))

(defn create-style-element!
  "Create a style element in head if classname is not already present Attach a
  data attr with namespace and call update-style with new element"
  [classname new fqn]
  (let [head (.-head js/document)
        element (.createElement js/document "style")]
    (assert (some? head)
            "An head element is required in the dom to inject the style.")
    (.setAttribute element "type" "text/css")
    (.setAttribute element "data-herb" fqn)
    (.appendChild head element)
    (update-style! classname element new)))

(defn inject-style!
  "Main interface to runtime. Takes a classname, new garden style data structure
  and a fully qualified name. Check if classname exist in DOM already, and if it
  does, compare `new` with `current` to make sure garden is not called to create
  the same style string again"
  [classname new fqn]
  (if-let [injected (get @injected-styles classname)]
    (let [current (:data injected)]
      (when (not= current new)
        (let [element (:element injected)]
          (update-style! classname element new))))
    (create-style-element! classname new fqn)))
