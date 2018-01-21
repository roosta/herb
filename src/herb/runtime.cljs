;; https://github.com/matthieu-beteille/cljs-css-modules/blob/master/src/cljs_css_modules/runtime.cljs

(ns herb.runtime
  (:require [garden.core :refer [css]]))

(def injected-styles (atom {}))

(defn update-style! [element style]
  (set! (.-innerHTML element) style))

(defn create-style-element! [classname style]
  (let [head (.-head js/document)
        element (.createElement js/document "style")]
    (assert (some? head)
            "An head element is required in the dom to inject the style.")
    (.appendChild head element)
    (update-style! element style)
    (swap! injected-styles assoc classname element)))

(defn inject-style! [classname style]
  ;; (.log js/console style)
  (let [injected-style (get @injected-styles classname)]
    (if injected-style
      (update-style! injected-style style)
      (create-style-element! classname style))))
