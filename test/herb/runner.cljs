(ns herb.runner
  (:require
    [herb.core-test]
    [doo.runner :refer-macros [doo-tests]]))

(doo-tests 'herb.core-test)
