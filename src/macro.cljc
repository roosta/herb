(ns herb.macro
  #?(:clj
     (:require
       [garden.core :refer [css]]
       [garden.stylesheet :refer [at-media at-keyframes]]))
  #?(:cljs
     (:require
       [garden.core :refer [css]]
       [garden.stylesheet :refer [at-media at-keyframes]]
       [herb.runtime]))) 
