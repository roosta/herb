(ns site.observer)

(defn create-observer [state]
  (js/IntersectionObserver.
   (fn [entries]
     (reset! state (= (.-intersectionRatio (first entries)) 0)))
   #js {:threshold [0 1]}))
