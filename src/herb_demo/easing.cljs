(ns herb-demo.easing
  (:require [clojure.string :as str]))

(def easing
  "https://gist.github.com/bendc/ac03faac0bf2aee25b49e5fd260a727d"
  {:ease-in-quad "cubic-bezier(.55, .085, .68, .53)"
   :ease-in-cubic "cubic-bezier(.550, .055, .675, .19)"
   :ease-in-quart "cubic-bezier(.895, .03, .685, .22)"
   :ease-in-quint "cubic-bezier(.755, .05, .855, .06)"
   :ease-in-expo "cubic-bezier(.95, .05, .795, .035)"
   :ease-in-circ "cubic-bezier(.6, .04, .98, .335)"

   :ease-out-quad "cubic-bezier(.25, .46, .45, .94)"
   :ease-out-cubic "cubic-bezier(.215, .61, .355, 1)"
   :ease-out-quart "cubic-bezier(.165, .84, .44, 1)"
   :ease-out-quint "cubic-bezier(.23, 1, .32, 1)"
   :ease-out-expo "cubic-bezier(.19, 1, .22, 1)"
   :ease-out-circ "cubic-bezier(.075, .82, .165, 1)"

   :ease-in-out-quad "cubic-bezier(.455, .03, .515, .955)"
   :ease-in-out-cubic "cubic-bezier(.645, .045, .355, 1)"
   :ease-in-out-quart "cubic-bezier(.77, 0, .175, 1)"
   :ease-in-out-quint "cubic-bezier(.86, 0, .07, 1)"
   :ease-in-out-expo "cubic-bezier(1, 0, 0, 1)"
   :ease-in-out-circ "cubic-bezier(.785, .135, .15, .86)"}
  )

(defn transition
  "Helper function that generates a transition string for multiple properties"
  [{:keys [properties durations delays easings]
    :or {durations (take (count properties) (repeat "500ms"))
         easings (take (count properties) (repeat :ease-in-cubic))
         delays (take (count properties) (repeat "0s"))}}]
  (let [transitions (map (fn [p d dl e]
                           (let [f (e easing)]
                             (str/join " " [p d dl f])))
                         properties durations delays easings)]
    (str/join ", " transitions)))
