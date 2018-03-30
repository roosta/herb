(ns herb.runtime
  (:require [garden.core :refer [css]]
            [goog.dom :as dom]
            [goog.object :as gobj]
            [debux.cs.core :as d :refer-macros [clog clogn dbg dbgn break]]
            [clojure.string :as str]))

(def injected-styles (atom {}))

(defn update-style!
  "Create css string and update DOM"
  [identifier element new]
  (let [css-str (css new)]
    (swap! injected-styles assoc identifier {:data new
                                             :element element})
    (set! (.-innerHTML element) css-str)))

(defn create-style-element!
  "Create a style element in head if identifier is not already present Attach a
  data attr with namespace and call update-style with new element"
  [identifier new data-str]
  (let [head (.-head js/document)
        element (.createElement js/document "style")]
    (assert (some? head)
            "An head element is required in the dom to inject the style.")
    (.setAttribute element "type" "text/css")
    (.setAttribute element "data-herb" data-str)
    (.appendChild head element)
    (update-style! identifier element new)))

(defn inject-style!
  "Main interface to runtime. Takes an identifier, new garden style data structure
  and a fully qualified name. Check if identifier exist in DOM already, and if it
  does, compare `new` with `current` to make sure garden is not called to create
  the same style string again"
  [identifier new data-str]
  (if-let [injected (get @injected-styles identifier)]
    (let [current (:data injected)]
      (when (not= current new)
        (let [element (:element injected)]
          (update-style! identifier element new))))
    (create-style-element! identifier new data-str)))
