(ns herb-demo.release
  (:require
    [herb-demo.core :as core]))

;;ignore println statements in prod
(set! *print-fn* (fn [& _]))

(core/init!)
