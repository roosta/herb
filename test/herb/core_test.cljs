(ns herb.core-test
  (:require [cljs.test :refer-macros [deftest testing is]]
            [herb.core :as core]
            [goog.object :as gobj]
            [herb.runtime :as runtime]))

(deftest init!
  (testing "Testing setting options via init function"
    (core/init! {:vendors ["webkit" :moz]
                 :auto-prefix #{"transition" :animation}})
    (let [result @runtime/options]
      (is (= (:vendors result) ["webkit" "moz"]))
      (is (= (:auto-prefix result) #{:animation "transition"})))

    (try (core/init! {:vendors :key})
         (catch js/Error e
           (is (= (.-message e) "Invalid input"))
           (is (= (-> (.-data e) :cljs.spec.alpha/spec) :herb.spec/options))
           (is (= (-> (.-data e) :cljs.spec.alpha/value) {:vendors :key}))))
    (try (core/init! {:auto-prefix [1 2 3]})
         (catch js/Error e
           (is (= (.-message e) "Invalid input"))
           (is (= (-> (.-data e) :cljs.spec.alpha/spec) :herb.spec/options))
           (is (= (-> (.-data e) :cljs.spec.alpha/value) {:auto-prefix [1 2 3]}))))))
