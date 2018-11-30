(ns herb-demo.snippets.global
  (:require [herb.core :refer-macros [defglobal]]))

(defglobal global
  [:body :html {:margin 0}]
  [:.some-class {:font-size "24px"}]
  [:a {:text-decoration "none"
       :color "#09f"}])
