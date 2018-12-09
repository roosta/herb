(ns herb-demo.utils
  (:require
   [goog.events :as gevents]
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
  (js/clearTimeout @timer)
  (reset! timer (js/setTimeout
                 #(reset! viewport-size (get-width))
                 100)))

(defonce vsm-listener
  (let [timer (atom nil)]
    (gevents/listen vsm
                    event-type/RESIZE
                    #(on-resize timer))))
