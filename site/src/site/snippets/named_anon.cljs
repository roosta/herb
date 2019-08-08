(ns site.snippets.named-anon
  (:require
   [garden.units :refer [px percent rem]]
   [herb.core :refer [<class]]))

(defn avatar
  [initial]
  (let [colors ["#6c0e23" "#c42021" "#d58936" "#ef2d56"]]
    [:div {:class (<class (fn style []
                            (let [c (rand-nth colors)]
                              {:width (rem 4)
                               :height (rem 4)
                               :background c
                               :display "flex"
                               :justify-content "center"
                               :align-items "center"
                               :color "white"
                               :font-size (rem 2)
                               :border-radius (percent 50)})))}
     initial]))

(defn component
  []
  [:div {:class (<class (fn row []
                          {:display :flex}))}
   [avatar "L"]
   [avatar "R"]])
