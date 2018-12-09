(ns herb-demo.utils
  (:require
   [goog.events :as gevents]
   [herb-demo.async :as async]
   [goog.events.EventType :as event-type]
   [reagent.core :as r])
  (:import
   (goog.dom ViewportSizeMonitor)))

(defonce vsm (ViewportSizeMonitor.))

(defn get-width []
  (let [size (.getSize vsm)]
    [(.-width size) (.-height size)]))

(def viewport-size (r/atom (get-width)))

(defn on-resize
  [timer]
  (reset! viewport-size (get-width)))

(defonce vsm-listener
  (gevents/listen vsm
                  event-type/RESIZE
                  (async/debounce
                   on-resize
                   200)))
