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

(deftest join
  (testing "Joining classnames"
    (is (= (core/join "class-1") "class-1"))
    (is (= (core/join "class-1" "class-2") "class-1 class-2"))
    (is (= (core/join "class-1" nil nil "class-2") "class-1 class-2"))
    (try (core/join 1 :key)
         (catch js/Error e
           (is (= (.-message e) "join takes one or more strings as arguments"))
           (is (= (-> (.-data e) :cljs.spec.alpha/spec) :herb.spec/classes))
           (is (= (-> (.-data e) :cljs.spec.alpha/value) '(1 :key)))))))


(deftest defkeyframes
  (testing "defkeyframes macro"
    (let [expansion (macroexpand '(core/defkeyframes pulse-animation
                                    [:from {:opacity 1}]
                                    [:to {:opacity 0}]))]
      (is (= (-> expansion first) 'do))
      (is (= (-> expansion second first) 'herb.runtime/inject-obj!))
      (is (= (-> expansion second second) '(clojure.core/str "herb.core-test" "/" 'pulse-animation)))
      (is (= (nth (second expansion) 2) :keyframes))
      (is (= (nth (second expansion) 3) '(garden.types.CSSAtRule.
                                          :keyframes
                                          {:identifier (clojure.core/str 'pulse-animation),
                                           :frames (clojure.core/list [:from {:opacity 1}] [:to {:opacity 0}])})))
      (is (= (-> (nth expansion 2) first) 'def))
      (is (= (-> (nth expansion 2) second) 'pulse-animation))
      (is (= (-> (nth expansion 2) last) '(garden.types.CSSAtRule.
                                           :keyframes
                                           {:identifier (clojure.core/str 'pulse-animation),
                                            :frames (clojure.core/list [:from {:opacity 1}] [:to {:opacity 0}])})))

      (core/defkeyframes pulse-animation
        [:from {:opacity 1}]
        [:to {:opacity 0}])

      (let [result (get (deref (deref #'herb.runtime/injected-keyframes)) "herb.core-test/pulse-animation")
            el-html (.-innerHTML (.item (.querySelectorAll js/document "[data-herb='keyframes']") 0))]
        (is (= (-> result :data first :identifier) :keyframes))
        (is (= (-> result :data first :value) {:identifier "pulse-animation",
                                               :frames '([:from {:opacity 1}] [:to {:opacity 0}])}))

        (is (= el-html "\n@keyframes pulse-animation {\n\n  from {\n    opacity: 1;\n  }\n\n  to {\n    opacity: 0;\n  }\n\n}"))
        ))))

(deftest defglobal
  (testing "defglobal macro"
    (let [expansion (macroexpand '(core/defglobal global
                                    [:.global {:color "magenta"
                                               :font-size "24px"}]))]
      (is (= (-> expansion first) 'do))
      (is (= (-> expansion second first) 'herb.runtime/inject-obj!))
      (is (= (-> expansion second second) '(clojure.core/str "herb.core-test" "/" 'global)))
      (is (= (nth (second expansion) 2) :global))
      (is (= (nth (second expansion) 3) '(clojure.core/list [:.global {:color "magenta", :font-size "24px"}])))

      (is (= (-> (nth expansion 2) first) 'def))
      (is (= (-> (nth expansion 2) second) 'global))
      (is (= (-> (nth expansion 2) last) '(clojure.core/list [:.global {:color "magenta", :font-size "24px"}])))

      (core/defglobal global
        [:.global {:color "magenta"
                   :font-size "24px"}])

      (let [result (get (deref (deref #'herb.runtime/injected-global)) "herb.core-test/global")
            el-html (.-innerHTML (.item (.querySelectorAll js/document "[data-herb='global']") 0))]
        (is (= (-> result :data ffirst) [:.global {:color "magenta", :font-size "24px"}]))
        (is (= (-> result :css) ".global {\n  color: magenta;\n  font-size: 24px;\n}"))
        (is (= el-html "\n.global {\n  color: magenta;\n  font-size: 24px;\n}"))))))
