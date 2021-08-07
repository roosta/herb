(ns herb.impl-test
  (:require [cljs.test :refer-macros [deftest testing is are async]]
            [garden.stylesheet :refer [at-media at-keyframes at-supports]]
            [herb.runtime :as runtime]
            [herb.impl :as impl]))

(deftest convert-pseudo
  (testing "Converting pseudo classes"
    (let [input {:hover {:opacity 1}
                 :focus {:color "yellow"}}
          result  (#'herb.impl/convert-pseudo input)]
      (is (seq? result))
      (is (= (ffirst result) :&:hover))
      (is (= (-> result first last) {:opacity 1}))
      (is (= (-> result last first) :&:focus))
      (is (= (-> result last last) {:color "yellow"})))))

(deftest convert-media
  (testing "converting media queries"
    (let [input {{:screen true} {:color "white"}
                 {:min-width "100px"} {:color "blue"}}
          result (#'herb.impl/convert-media input)]

      (are [x y] (= x y)
        (:identifier (first result)) :media
        (:media-queries (:value (first result))) {:screen true}
        (:rules (:value (first result))) '([:& {:color "white"}])

        (:identifier (last result)) :media
        (:media-queries (:value (last result))) {:min-width "100px"}
        (:rules (:value (last result))) '([:& {:color "blue"}])))))

(deftest convert-supports
  (testing "converting CSS supports"
    (let [input {{:display :grid} {:display :grid}
                 {:display :flex } {:display :flex}}
          result (#'herb.impl/convert-supports input)]

      (are [x y] (= x y)
        (:identifier (first result)) :feature
        (:feature-queries (:value (first result))) {:display :grid}
        (:rules (:value (first result))) '([:& {:display :grid}])

        (:identifier (second result)) :feature
        (:feature-queries (:value (second result))) {:display :flex}
        (:rules (:value (second result))) '([:& {:display :flex}])))))

(deftest process-meta-xform
  (testing "process metadata transducer"
    (let [input [^{:pseudo {:hover {:color "green"}}
                   :media {{:screen true} {:color "red"}}}
                 {:background "blue"}
                 ^{:vendors ["ms" :webkit]
                   :auto-prefix #{:transition}
                   :supports {{:display :grid} {:display :grid}}}
                 {:background "cyan"}]]
      (are [x y] (= x y)
        (into [] (#'herb.impl/process-meta-xform :pseudo) input) [{:hover {:color "green"}}]
        (into [] (#'herb.impl/process-meta-xform :media) input) [{{:screen true} {:color "red"}}]
        (into [] (#'herb.impl/process-meta-xform :vendors) input) [["ms" :webkit]]
        (into [] (#'herb.impl/process-meta-xform :auto-prefix) input) [#{:transition}]
        (into [] (#'herb.impl/process-meta-xform :supports) input) [{{:display :grid} {:display :grid}}]))))

(deftest resolve-style-fns
  (testing "resolving style functions"
    (letfn [(fn-1 [] {:border-radius "5px"})
            (fn-2 [color] {:background-color color})
            (fn-3 [] {:background-color "red"})]
      (let [result (#'herb.impl/resolve-style-fns [[[fn-1] [fn-2 "green"] [fn-3]]])]
        (is vector? result)
        (are [x y] (= x y)
          (count result) 3
          (nth result 0) {:border-radius "5px"}
          (nth result 1) {:background-color "green"}
          (nth result 2) {:background-color "red"})))))

(deftest extract-extended-styles
  (testing "extracting extended style functions"
    (letfn [(parent [] {:font-size "24px"})
            (extend-1 [color] ^{:extend parent} {:background-color color})
            (extend-2 [] ^{:extend [[extend-1 "red"]]} {:border-radius "5px"})]
      (let [result (#'herb.impl/extract-extended-styles extend-2)]
        (is vector? result)
        (are [x y] (= x y)
          (count result)  3
          (first result)  {:font-size "24px"}
          (second result) {:background-color "red"}
          (last result)   {:border-radius "5px"})))))

(deftest extract-meta-pseudo
  (testing "extracting pesudo meta data"
    (let [styles [^{:pseudo {:hover {:color "red"}
                             :focus {:color "blue"}}}
                  {:color "white"}

                  ^{:pseudo {:hover {:color "green"}}}
                  {:border "2px solid black"}]
          result (#'herb.impl/extract-meta styles :pseudo)]
      (is (seq? result))
      (are [x y] (= x y)
        (count result) 2
        (ffirst result) :&:hover
        (-> result first last) {:color "green"}
        (-> result second first) :&:focus
        (-> result second last) {:color "blue"}))))

(deftest extract-meta-media
  (testing "Extracting media meta data"
    (let [styles [^{:media {{:screen true} {:color "magenta"}
                            {:max-width "412px"} {:color "blue"}}}
                  {:color "white"}

                  ^{:media {{:screen true} {:color "purple"}}}
                  {:color "white"}]
          result (#'herb.impl/extract-meta styles :media)]

      (is (seq? result))
      (are [x y] (= x y)
        (count result) 2
        (:identifier (first result)) :media
        (:media-queries (:value (first result))) {:screen true}
        (:rules (:value (first result))) '([:& {:color "purple"}])

        (:identifier (last result)) :media
        (:media-queries (:value (last result))) {:max-width "412px"}
        (:rules (:value (last result))) '([:& {:color "blue"}])))))

(deftest prepare-data
  (testing "Prepare data for rendering"
    (let [styles [(with-meta
                    {:color "white"
                     :background-color "green"}
                    {:pseudo {:hover {:color "magenta"}}})
                  (with-meta
                    {:color "black"
                     :border-radius "5px"}
                    {:media {{:screen true} {:background-color "yellow"}}})
                  (with-meta
                    {:background-color "red"
                     :font-style "italic"}
                    {:media {{:screen true} {:background-color "blue"}}})
                  (with-meta
                    {:font-weight "bold"}
                    {:supports {{:display :grid} {:font-size "24px"}}})
                  (with-meta
                    {:transition "all 1s ease-out"}
                    {:vendors ["ms" "webkit"]
                     :prefix true})
                  (with-meta
                    {:box-sizing "border-box"}
                    {:combinators {[:> :div :span] {:background "red"}
                                   [:+ :p] {:background "purple"}
                                   [:- :div] {:background "yellow"}
                                   [:descendant :div] {:background "green"}}})]
          result (#'herb.impl/prepare-data styles)]

      (is (vector? (:vendors result)))
      (is (:prefix result))
      (are [x y] (= x y)
        (-> result :style :color) "black"
        (-> result :style :background-color) "red"
        (-> result :style :border-radius) "5px"
        (-> result :style :font-style) "italic"
        (-> result :style :font-weight) "bold"
        (-> result :style :transition) "all 1s ease-out"
        (-> result :style :box-sizing) "border-box"

        (count (-> result :vendors)) 2
        (-> result :vendors first) "ms"
        (-> result :vendors last) "webkit"

        (-> result :supports first :identifier) :feature
        (-> result :supports first :value :feature-queries) {:display :grid}
        (-> result :supports first :value :rules) '([:& {:font-size "24px"}])

        (-> result :pseudo first first) :&:hover
        (-> result :pseudo first last) {:color "magenta"}

        (-> result :media first :identifier) :media
        (-> result :media first :value :media-queries) {:screen true}
        (-> result :media first :value :rules) '([:& {:background-color "blue"}])

        (count (-> result :combinators)) 4
        (get (-> result :combinators) [:> :div :span]) {:background "red"}
        (get (-> result :combinators) [:+ :p]) {:background "purple"}
        (get (-> result :combinators) [:- :div]) {:background "yellow"}
        (get (-> result :combinators) [:descendant :div]) {:background "green"}))))

(deftest sanitize
  (testing "Sanitize names"
    (is (= (#'herb.impl/sanitize "demo/examples/main-/-nested-fn!")
           "demo_examples_main-_-nested-fn_"))
    (is (= (#'herb.impl/sanitize :some$keyword*!)
           "some_keyword__"))))

(deftest compose-selector
  (testing "Composing selector"
    (is (= (#'herb.impl/compose-selector "demo.examples/anonymous" 660680338 :class)
           ".demo_examples_anonymous_660680338"))

    (is (= (#'herb.impl/compose-selector "demo.examples/cycle-color" 1234567 :id)
           "#demo_examples_cycle-color_1234567"))

    (is (= (#'herb.impl/compose-selector "demo.examples/style-group-static" 1234567 :class)
           ".demo_examples_style-group-static_1234567"))))

(deftest create-data-string
  (testing "Creating a data string from cljs function name"
    (is (= (#'herb.impl/create-data-string "demo/examples/width-vary-component")
           "demo.examples/width-vary-component"))
    (is (= (#'herb.impl/create-data-string "demo/examples/pulse-component-two")
           "demo.examples/pulse-component-two" ))
    (is (= (#'herb.impl/create-data-string "demo/examples/main/nested-fn")
           "demo.examples.main/nested-fn"))))

(defn test-fn-1
  []
  (letfn [(fn-binding [] {:some :map})]
    (#'herb.impl/get-name fn-binding "herb.impl-test" {})))

(defn test-fn-2 [] (#'herb.impl/get-name test-fn-1 "herb.impl-test" {}))

(defn test-fn-3 [] (#'herb.impl/get-name #({}) "herb.impl-test" {}))

(defn test-fn-4 [] {:background :red})

(deftest get-name
  (testing "getting function name"
    (is (= (subs (test-fn-1) 0 36)
           "herb/impl-test/test-fn-1-/-fn-bindin"))
    (is (= (test-fn-2)
           "herb/impl-test/test-fn-1"))
    (is (= (test-fn-3)
           "herb.impl-test/anonymous"))))

(deftest with-style!
  (testing "with-style! entry point"
    (is (= (impl/with-style! :class "test-fn-1" "herb.unit-tests" test-fn-4)
           "herb_impl-test_test-fn-4_1275559517"))
    (is (= (impl/with-style! :style "test-fn-1" "herb.unit-tests" test-fn-4 nil)
           ".herb_impl-test_test-fn-4_1275559517 {\n  background: red;\n}"))

    (let [result (get (deref (deref #'herb.runtime/injected-styles)) "herb_impl-test_test-fn-4")]
      (is (= (:style (get (:data result) ".herb_impl-test_test-fn-4_1275559517")) {:background :red}))
      (is (= (:data-string result) "herb.impl-test/test-fn-4"))
      (is (= (:css result) ".herb_impl-test_test-fn-4_1275559517 {\n  background: red;\n}")))))
