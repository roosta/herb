(ns site.snippets.vendor-init
  (:require [herb.core :as herb]))

;; vendors is a vector or string or keyword. Can be one of
;; auto-prefix is a set of CSS properties as string or keyword
(defn init! []
  (herb/init! {:vendors ["webkit" :moz]
               :auto-prefix #{:transition :animation}}))
