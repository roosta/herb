(ns ^:figwheel-no-load site.dev
  (:require
    [site.core :as core]
    [devtools.core :as devtools]))

(devtools/install!)

(enable-console-print!)

(core/init!)
