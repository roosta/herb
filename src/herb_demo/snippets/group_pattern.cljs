(ns herb-demo.snippets.group-pattern
  (:require [herb.core :refer-macros [<class]]
            [garden.units :refer [px]]))

;; Here we pick out a component based on a passed key, and modify that styles
;; metadata to include :key and :group.
(defn my-group
  [k & args]
  (let [styles
        {:container {:display :flex}
         :component-1 {:background-color :black
                       :width (px 50)
                       :height (px 50)}
         :component-2 {:background-color :grey
                       :width (px 50)
                       :height (px 50)}}]
    ;; The advantage of using vary-meta here is that we preserve whatever other
    ;; meta data is attached to the style map
    (vary-meta
     (k styles)
     assoc
     :key k
     :group true)))

(defn component
  []
  [:div {:class (<class my-group :container)}
   [:div {:class (<class my-group :component-1)}]
   [:div {:class (<class my-group :component-2)}]])
