(ns site.snippets.anon
  (:require [herb.core :refer [<class]]))

(defn link []
  [:a {:href "https://clojurescript.org/"
       :class (<class (fn []
                        ^{:pseudo {:hover {:text-decoration "underline"}}}
                        {:text-decoration "none"
                         :color "#09f"}))}
   "Go to clojurescript.org"])
