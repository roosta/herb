(ns site.async
  "https://medium.com/@alehatsman/clojurescript-throttle-debounce-a651dfb66ac"
  (:import [goog.async Throttle Debouncer]))

(defn disposable->function [disposable listener interval]
  (let [disposable-instance (disposable. listener interval)]
    (fn [& args]
      (.apply (.-fire disposable-instance) disposable-instance (to-array args)))))

(defn throttle [listener interval]
  (disposable->function Throttle listener interval))

(defn debounce [listener interval]
  (disposable->function Debouncer listener interval))
