(ns herbdemo.perf
  (:require [herb.core :refer [<class]]
            [garden.stylesheet :refer [at-media at-keyframes]]
            [goog.dom :as dom]
            [garden.core :refer [css]]
            [garden.units :refer [rem em px]]))

(defn profile-comp []
  {:width (px 100)
   :height (px 100)
   :background-color "magenta"
   :border-radius "5px"})

(defn performance
  []
  (simple-benchmark [] (<class profile-comp) 10000)
  (simple-benchmark [element (.createElement js/document "style")]
                    (dom/append element ".test {color: \"red\"}")
                    10000)
  (simple-benchmark [] (css [:.classname {:width (px 100)
                                          :height (px 100)
                                          :background-color "magenta"
                                          :border-radius "5px"}])
                    10000)
  (simple-benchmark [] (at-media {:max-width "256px"} [:.classname {:width (px 100)
                                                                    :height (px 100)
                                                                    :background-color "magenta"
                                                                    :border-radius "5px"}])
                    10000))
