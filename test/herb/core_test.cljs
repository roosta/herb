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
      (core/defkeyframes pulse-animation
        [:from {:opacity 1}]
        [:to {:opacity 0}])

      (let [result (get (deref (deref #'herb.runtime/injected-keyframes)) "herb.core-test/pulse-animation")
            el-html (.-innerHTML (.item (.querySelectorAll js/document "[data-herb='keyframes']") 0))]
        (is (= (-> result :data first :identifier) :keyframes))
        (is (= (-> result :data first :value) {:identifier "pulse-animation",
                                               :frames '([:from {:opacity 1}] [:to {:opacity 0}])}))

        (is (= el-html "\n@keyframes pulse-animation {\n\n  from {\n    opacity: 1;\n  }\n\n  to {\n    opacity: 0;\n  }\n\n}"))
        )))

(deftest defglobal
  (testing "defglobal macro"
    (core/defglobal global
      [:.global {:color "magenta"
                 :font-size "24px"}])

    (let [result (get (deref (deref #'herb.runtime/injected-global)) "herb.core-test/global")
          el-html (.-innerHTML (.item (.querySelectorAll js/document "[data-herb='global']") 0))]
      (is (= (-> result :data ffirst) [:.global {:color "magenta", :font-size "24px"}]))
      (is (= (-> result :css) ".global {\n  color: magenta;\n  font-size: 24px;\n}"))
      (is (= el-html "\n.global {\n  color: magenta;\n  font-size: 24px;\n}")))))

(deftest defgroup
  (testing "creating groups using defgroup macro"
      (core/defgroup a-group
        {:text {:font-weight "bold"}
         :box {:background-color "#333"}})
      (is (= (core/<class a-group :text) "herb_core-test_a-group_text_1357205618"))
      (is (= (core/<class a-group :box) "herb_core-test_a-group_box_-1329735411"))

      (let [result (get (deref (deref #'herb.runtime/injected-styles)) "herb_core-test_a-group")
            el-html (.-innerHTML (.item (.querySelectorAll js/document "[data-herb='herb.core-test/a-group']") 0))]
        (is (= (:style (get (:data result) ".herb_core-test_a-group_text_1357205618")) {:font-weight "bold"}))
        (is (= (:style (get (:data result) ".herb_core-test_a-group_box_-1329735411")) {:background-color "#333"}))

        (is (= (:data-string result) "herb.core-test/a-group"))
        (is (= (:css result) ".herb_core-test_a-group_text_1357205618 {\n  font-weight: bold;\n}\n.herb_core-test_a-group_box_-1329735411 {\n  background-color: #333;\n}"))
        (is (= el-html "\n.herb_core-test_a-group_text_1357205618 {\n  font-weight: bold;\n}\n.herb_core-test_a-group_box_-1329735411 {\n  background-color: #333;\n}")))))

(defn top-level-fn [] {:background "red"})

(defn extended-fn []
  ^{:extend top-level-fn}
  {:color "black"})

(deftest <class
  (testing "using <class macro to create a classname and appending style to DOM"
    (letfn [(nested-fn [color]
              {:background color})]
        (is (= (core/<class top-level-fn) "herb_core-test_top-level-fn_-1172868095"))
        (is (= (core/<class nested-fn "blue") "herb_core-test_nested-fn_391792308"))
        (is (= (core/<class (fn [] {:color "blue"})) "herb_core-test_anonymous_-231793611"))
        (is (= (core/<class extended-fn) "herb_core-test_extended-fn_-1721910037"))

        (let [top-level-result (get (deref (deref #'herb.runtime/injected-styles)) "herb_core-test_top-level-fn")
              nested-fn-result (get (deref (deref #'herb.runtime/injected-styles)) "herb_core-test_nested-fn")
              anon-fn-result (get (deref (deref #'herb.runtime/injected-styles)) "herb_core-test_anonymous")
              extended-fn-result (get (deref (deref #'herb.runtime/injected-styles)) "herb_core-test_extended-fn")
              top-level-html (.-innerHTML (.item (.querySelectorAll js/document "[data-herb='herb.core-test/top-level-fn']") 0))
              nested-fn-html (.-innerHTML (.item (.querySelectorAll js/document "[data-herb='herb.core-test/nested-fn']") 0))
              anon-fn-html (.-innerHTML (.item (.querySelectorAll js/document "[data-herb='herb.core-test/anonymous']") 0))
              extended-fn-html (.-innerHTML (.item (.querySelectorAll js/document "[data-herb='herb.core-test/extended-fn']") 0))
              ]
          (is (= (:style (get (:data top-level-result) ".herb_core-test_top-level-fn_-1172868095")) {:background "red"}))
          (is (= (:data-string top-level-result) "herb.core-test/top-level-fn"))
          (is (= (:css top-level-result) ".herb_core-test_top-level-fn_-1172868095 {\n  background: red;\n}"))

          (is (= (:style (get (:data nested-fn-result) ".herb_core-test_nested-fn_391792308")) {:background "blue"}))
          (is (= (:data-string nested-fn-result) "herb.core-test/nested-fn"))
          (is (= (:css nested-fn-result) ".herb_core-test_nested-fn_391792308 {\n  background: blue;\n}"))

          (is (= (:style (get (:data anon-fn-result) ".herb_core-test_anonymous_-231793611")) {:color "blue"}))
          (is (= (:data-string anon-fn-result) "herb.core-test/anonymous"))
          (is (= (:css anon-fn-result) ".herb_core-test_anonymous_-231793611 {\n  color: blue;\n}"))

          (is (= (:style (get (:data extended-fn-result) ".herb_core-test_extended-fn_-1721910037")) {:color "black" :background "red"}))
          (is (= (:data-string extended-fn-result) "herb.core-test/extended-fn"))
          (is (= (:css extended-fn-result) ".herb_core-test_extended-fn_-1721910037 {\n  background: red;\n  color: black;\n}"))

          (is (= top-level-html "\n.herb_core-test_top-level-fn_-1172868095 {\n  background: red;\n}"))
          (is (= nested-fn-html "\n.herb_core-test_nested-fn_391792308 {\n  background: blue;\n}"))
          (is (= anon-fn-html "\n.herb_core-test_anonymous_-231793611 {\n  color: blue;\n}"))
          (is (= extended-fn-html "\n.herb_core-test_extended-fn_-1721910037 {\n  background: red;\n  color: black;\n}"))
          ))))
