;; https://github.com/matthieu-beteille/cljs-css-modules/blob/master/src/cljs_css_modules/runtime.cljs

(ns herb.runtime
  (:require [garden.core :refer [css]]
            [goog.dom :as dom]
            [clojure.string :as str]
            [reagent.debug :as d]))

(def injected-styles (atom {}))

(defn update-style! [classname element new]
  (let [css (css new)]
    (swap! injected-styles assoc classname {:data new
                                            :element element})
    (set! (.-innerHTML element) css)))

(defn create-style-element! [classname new fqn]
  (let [head (.-head js/document)
        element (.createElement js/document "style")]
    (assert (some? head)
            "An head element is required in the dom to inject the style.")
    (.setAttribute element "type" "text/css")
    (.setAttribute element "data-herb" fqn)
    (.appendChild head element)
    (update-style! classname element new)))

(defn inject-style! [classname new fqn]
    (if-let [injected (get @injected-styles classname)]
      (let [current (:data injected)]
        (when (not= current new)
          (let [element (:element injected)]
            (update-style! classname element new))))
      (create-style-element! classname new fqn)))
