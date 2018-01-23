;; https://github.com/matthieu-beteille/cljs-css-modules/blob/master/src/cljs_css_modules/runtime.cljs

(ns herb.runtime
  (:require [garden.core :refer [css]]
            [goog.dom :as dom]))

(def injected-styles (atom {}))

(defn update-style! [classname element style]
  (swap! injected-styles assoc classname style)
  (set! (.-innerHTML element) style))

(defn create-style-element! [classname style]
  (let [head (.-head js/document)
        element (.createElement js/document "style")]
    (assert (some? head)
            "An head element is required in the dom to inject the style.")
    (set! (.-id element) (str classname "_root"))
    (.appendChild head element)
    (update-style! classname element style)))

(defn inject-style! [classname style]
  (if-let [injected-style (get @injected-styles classname)]
    (when (not= injected-style style)
      (let [element (dom/getElement (str classname "_root"))]
        (update-style! classname element style)))
    (create-style-element! classname style)))
