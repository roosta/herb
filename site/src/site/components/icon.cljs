(ns site.components.icon
  (:require
   [herb.core :refer-macros [<class]]
   [clojure.string :as str]
   [reagent.core :as r]))

(defn svg-icon-style
  []
  {:display "inline-block"
   :fill "currentColor"
   :user-select "none"
   :flex-shrink 0})

(defn svg-icon
  [{:keys [class viewbox]}]
  (into
   [:svg {:view-box viewbox
          :class [class (<class svg-icon-style)]}]
   (r/children (r/current-component))))

(defn menu
  [{:keys [class]}]
  [svg-icon {:class class
             :viewbox "0 0 24 24"}
   [:path {:d "M0 0h24v24H0z" :fill "none"}]
   [:path {:d "M3 18h18v-2H3v2zm0-5h18v-2H3v2zm0-7v2h18V6H3z"}]])
