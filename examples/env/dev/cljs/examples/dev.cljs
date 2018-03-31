(ns ^:figwheel-no-load examples.dev
  (:require
    [examples.core :as core]
    [devtools.core :as devtools]))


(enable-console-print!)

(devtools/install!)

(core/init!)
