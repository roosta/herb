(ns herb.benchmark-runner
  (:require [herb.core :refer [<class]]
            [garden.stylesheet :refer [at-media at-keyframes]]
            [goog.dom :as dom]
            [herb.impl :as impl]
            [garden.core :refer [css]]
            [garden.units :refer [rem em px]]))

(defn extend-more []
  {:width "20px"})

(defn extend-me []
  ^{:extend extend-more}
  {:color "red"})

(defn extended-style []
  ^{:extend extend-me}
  {:width (px 100)
   :height (px 100)
   :background-color "magenta"
   :border-radius "5px"})

(defn function-with-args [n]
  ^{:extend extend-me}
  {:width (px n)})

(def resolved-styles [^{:pseudo {:hover {:color "yellow"}}
                        :vendors ["webkit"]
                        :prefix true} {:color "black", :background-color "blue"}

                      ^{:media {{:screen :only :min-width "32em"} {:width "33rem"}
                                {:screen :only :min-width "52em"} {:width "53rem"}}
                        :supports {{:display :grid} {:font-size "20px"}}}
                      {:color "white", :background-color "red"}])


(defn -main [& args]

  (println "<class macro with arguments")
  (let [runs 1000
        start (.getTime (js/Date.))
        _ (dotimes [n runs]
              (<class function-with-args n))
        end (.getTime (js/Date.))
        elapsed (- end start)]
    (println (str runs " runs, " elapsed " msecs")))
  (println)

  (println "<class macro")
  (simple-benchmark [] (<class extended-style) 10000)
  (println)

  (println "impl/get-name")
  (simple-benchmark [] (#'impl/get-name extended-style "perf.ns") 10000)
  (println)

  (println "impl/create-data-string")
  (simple-benchmark [] (#'impl/create-data-string extended-style "perf/test/ns") 10000)
  (println)

  (println "impl/compose-selector")
  (simple-benchmark [] (#'impl/compose-selector "perf/test/ns" "1234567") 10000)
  (println)

  (println "impl/prepare-data/extract-meta")
  (simple-benchmark [] (#'impl/prepare-data resolved-styles) 10000)
  (println)

  (println "impl/extract-extended-styles/resolve-style-fns")
  (simple-benchmark [] (#'impl/extract-extended-styles [extended-style]) 10000)

  (println "append to DOM")
  (simple-benchmark [element (.createElement js/document "style")]
                    (dom/append element ".test {color: \"red\"}")
                    10000)
  (println)

  (println "Create css string using garden")
  (simple-benchmark [] (css [:.classname {:width (px 100)
                                          :height (px 100)
                                          :background-color "magenta"
                                          :border-radius "5px"}])
                    10000)
  (println)

  (println "creating media queries using garden")
  (simple-benchmark [] (at-media {:max-width "256px"}
                                 [:.classname {:width (px 100)
                                               :height (px 100)
                                               :background-color "magenta"
                                               :border-radius "5px"}])
                    10000))
