(ns site.snippets.local-binding
  (:require [herb.core :refer [<class]]
            [garden.units :refer [rem]]
            [reagent.core :as r]))

(defn headline
  [size text]
  (letfn [(style []
            ^{:key size}
            {:font-size (case size
                          :large (rem 3)
                          :small (rem 1))
             :font-family ["Raleway" "sans-serif"]})]
    [(size {:large :h1 :small :h4}) {:class (<class style)}
     text]))

(defn component
  []
  [:div
   [headline :large
    "Large headline"]
   [headline :small
    "Small headline"]])
