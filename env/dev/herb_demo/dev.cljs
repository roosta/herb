(ns ^:figwheel-no-load herb-demo.dev
  (:require
    [herb-demo.core :as core]
    [taoensso.tufte :as tufte]
    [devtools.core :as devtools]))

(devtools/install!)

(tufte/add-basic-println-handler! {})

(enable-console-print!)

(core/init!)
