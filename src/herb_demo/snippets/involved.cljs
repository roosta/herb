(ns herb-demo.snippets.involved
  (:require-macros [garden.def :refer [defcssfn]])
  (:require
   [herb.core :refer-macros [<class]]
   [garden.units :refer [px percent]]))

;; Use gardens cssfn macro to create a linear gradient this makes
;; linear-gradient callable and the vectors denote whether its space or comma
;; separated. Look at garden.def/defcssfn for more details.
(defcssfn linear-gradient
  [dir c1 p1 c2 p2]
  [dir [c1 p1] [c2 p2]])

;; Define a style that uses a color pair and creates a gradient from them.
;; Note the meta data ^{:key (str c1 "-" c2)}
(defn style
  [[c1 c2]]
  ^{:key (str c1 "-" c2)}
  {:width (px 56)
   :height (px 32)
   :background (linear-gradient "to right" c1 (percent 0) c2 (percent 100))})

;; inline color gradients
(defn flex-container
  []
  {:display "flex"})

(defn component
  []
  (let [colors (partition 2 1 ["#6c0e23" "#c42021" "#d58936" "#ef2d56"])]
    [:div {:class (<class flex-container)}
     (for [pair colors]
       ^{:key pair}
       [:div {:class (<class style pair)}])]))
