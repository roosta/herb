(ns ^:figwheel-no-load site.dev
  (:require
    [site.core :as core]
    [devtools.core :as devtools]))


(enable-console-print!)

(devtools/install!)

(core/init!)
