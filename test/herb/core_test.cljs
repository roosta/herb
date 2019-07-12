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

(deftest defgroup
  (testing "creating groups using defgroup macro"
    (let [expansion (macroexpand-1 '(core/defgroup a-group
                                      {:text {:font-weight "bold"}
                                       :box {:background-color "#333"}}))]

      (is (= (-> expansion first) 'clojure.core/defn))
      (is (= (-> expansion second) 'a-group))
      (is (= (nth expansion 2) '[component  & args]))
      (is (= (-> (nth expansion 3) first) 'clojure.core/if-let))
      (is (= (-> (nth expansion 3) second second) '(clojure.core/get
                                                    {:text {:font-weight "bold"}, :box {:background-color "#333"}}
                                                    component)))
      (is (= (first (nth (nth expansion 3) 2)) 'clojure.core/vary-meta))
      (is (= (nth (nth (nth expansion 3) 2) 2) 'clojure.core/assoc))
      (is (= (nth (nth (nth expansion 3) 2) 3) :key))
      (is (= (nth (nth (nth expansion 3) 2) 4) 'component))
      (is (= (nth (nth (nth expansion 3) 2) 5) ':group))
      (is (= (nth (nth (nth expansion 3) 2) 6) true))
      (is (= (first (nth (nth expansion 3) 3)) 'throw))
      (is (= (second (nth (nth expansion 3) 3)) '(clojure.core/str "Herb error: failed to get component: " component " in stylegroup: " 'a-group))))

      (core/defgroup a-group
        {:text {:font-weight "bold"}
         :box {:background-color "#333"}})
      (is (= (core/<class a-group :text) "herb_core-test_a-group_text"))
      (is (= (core/<class a-group :box) "herb_core-test_a-group_box"))

      (let [result  (get (deref (deref #'herb.runtime/injected-styles)) "herb_core-test_a-group")
            el-html (.-innerHTML (.item (.querySelectorAll js/document "[data-herb='herb.core-test/a-group']") 0))]
        (is (= (:style (get (:data result) ".herb_core-test_a-group_text")) {:font-weight "bold"}))
        (is (= (:style (get (:data result) ".herb_core-test_a-group_box")) {:background-color "#333"}))

        (is (= (:data-string result) "herb.core-test/a-group"))
        (is (= (:css result) ".herb_core-test_a-group_text {\n  font-weight: bold;\n}\n.herb_core-test_a-group_box {\n  background-color: #333;\n}"))
        (is (= el-html "\n.herb_core-test_a-group_text {\n  font-weight: bold;\n}\n.herb_core-test_a-group_box {\n  background-color: #333;\n}")))))

(defn top-level-fn [] {:background "red"})

(deftest <class
  (testing "using <class macro to create a classname and appending style to DOM"
    (letfn [(nested-fn [color]
              {:background color})]
      (let [expansion (macroexpand-1 '(core/<class top-level-fn
                                                   {:text {:font-weight "bold"}
                                                    :box {:background-color "#333"}}))]
        (is (= (-> expansion first) 'clojure.core/cond))
        (is (= (-> expansion second) '(clojure.core/not (clojure.core/fn? top-level-fn))))
        (is (= (nth (nth expansion 2) 0) 'throw))
        (is (= (nth (nth expansion 2) 1)
               '(clojure.core/ex-info
                 (clojure.core/str
                  "herb error in ns \""
                  "herb.core-test"
                  "\" the first argument to <class needs to be a function.")
                 {:function 'top-level-fn,
                  :return-value
                  (top-level-fn
                   {:text {:font-weight "bold"}, :box {:background-color "#333"}}),
                  :namespace "herb.core-test"})))
        (is (= (nth expansion 3) '(clojure.core/not
                                   (clojure.core/map?
                                    (top-level-fn
                                     {:text {:font-weight "bold"}, :box {:background-color "#333"}})))))
        (is (= (nth (nth expansion 4) 0) 'throw))
        (is (= (nth (nth expansion 4) 1)
               '(clojure.core/ex-info
                 (clojure.core/str
                  "herb error: style function \""
                  "herb.core-test"
                  "/"
                  'top-level-fn
                  "\" needs to return a map.")
                 {:function 'top-level-fn,
                  :return-value
                  (top-level-fn
                   {:text {:font-weight "bold"}, :box {:background-color "#333"}}),
                  :namespace "herb.core-test"})))
        (is (= (nth expansion 5) :else))
        (is (= (nth expansion 6) '(herb.impl/with-style!
                                    {}
                                    'top-level-fn
                                    "herb.core-test"
                                    top-level-fn
                                    {:text {:font-weight "bold"}, :box {:background-color "#333"}})))


        ))))
