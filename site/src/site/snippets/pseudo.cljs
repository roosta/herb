(ns site.snippets.pseudo
  (:require [herb.core :refer [<class]]))

(defn style []
  ;; Add hover pseudo class and after pseudo element
  ;; The syntax is the same for both
  ^{:pseudo {:hover {:text-decoration "underline"}
             :after {:content "'→'"}}}
  {:text-decoration "none"
   :color "#09f"})

(defn component []
  [:a {:class (<class style)
       :href "https://clojurescript.org/"}
   "ClojureScript"])
