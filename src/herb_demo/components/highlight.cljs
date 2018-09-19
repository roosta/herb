(ns herb-demo.components.highlight
  (:require [reagent.core :as r]
            [goog.object :as gobj]
            [cljsjs.highlight]
            [cljsjs.react-highlight]))

(def Highlight (r/adapt-react-class js/Highlight))
