(ns herb.unit-tests
  (:require [clojure.test :as t :refer [deftest testing is are]]
            [garden.stylesheet :refer [at-media at-keyframes at-supports]]
            [herb.runtime :as runtime]
            [herb.impl :as impl]))

(deftest convert-pseudo
  (let [input {:hover {:opacity 1}
               :focus {:color "yellow"}}
        expected (list [:&:hover {:opacity 1}] [:&:focus {:color "yellow"}])]
    (testing "Converting modes"
      (is (= (impl/convert-pseudo input) expected)))))

(deftest convert-media
  (let [input {{:screen true} {:color "white"}
               {:min-width "100px"} {:color "blue"}}
        expected (list (at-media {:screen true} [:& {:color "white"}])
                       (at-media {:min-width "100px"} [:& {:color "blue"}]))]
    (testing "Converting media queries"
      (is (= (impl/convert-media input) expected)))))

(deftest convert-supports
  (let [input {{:display :grid} {:display :grid}
               {:display :flex } {:display :flex}}
        expected (list (at-supports {:display :grid} [:& {:display :grid}])
                       (at-supports {:display :flex} [:& {:display :flex}]))]
    (testing "Converting support queries"
      (is (= (impl/convert-supports input) expected)))))

(deftest convert-vendors
  (let [input ["webkit" :webkit :ms "moz"]
        expected ["webkit" "webkit" "ms" "moz"]]
    (testing "Converting vendors"
      (is (= (impl/convert-vendors input) expected)))))

(deftest process-meta-xform
  (let [input [^{:pseudo {:hover {:color "green"}}
                 :media {{:screen true} {:color "red"}}}
               {:background "blue"}
               ^{:vendors ["ms" :webkit]
                 :auto-prefix #{:transition}
                 :supports {{:display :grid} {:display :grid}}}
               {:background "cyan"}]]
    (testing "Process meta xform"
      (are [x y] (= x y)
        (into [] (impl/process-meta-xform :pseudo) input) [{:hover {:color "green"}}]
        (into [] (impl/process-meta-xform :media) input) [{{:screen true} {:color "red"}}]
        (into [] (impl/process-meta-xform :vendors) input) [["ms" :webkit]]
        (into [] (impl/process-meta-xform :auto-prefix) input) [#{:transition}]
        (into [] (impl/process-meta-xform :supports) input) [{{:display :grid} {:display :grid}}]))))

(deftest resolve-style-fns
  (letfn [(fn-1 [] {:border-radius "5px"})
          (fn-2 [color] {:background-color color})
          (fn-3 [] {:background-color "red"})]
    (let [expected [{:border-radius "5px"} {:background-color "green"} {:background-color "red"}]]
      (testing "Resolve styles"
        (is (= (impl/resolve-style-fns [[[fn-1] [fn-2 "green"] [fn-3]]])
               expected))))))

(deftest extract-styles
  (letfn [(parent [] {:font-size "24px"})
          (extend-1 [color] ^{:extend parent} {:background-color color})
          (extend-2 [] ^{:extend [[extend-1 "red"]]} {:border-radius "5px"})]
    (let [expected [{:font-size "24px"} {:background-color "red"} {:border-radius "5px"}]]
      (testing "Extract styles"
        (is (= (impl/extract-styles extend-2) expected))))))

(deftest extract-meta-mode
  (let [styles [^{:pseudo {:hover {:color "red"}
                         :focus {:color "blue"}}}
                {:color "white"}

                ^{:pseudo {:hover {:color "green"}}}
                {:border "2px solid black"}]
        actual (impl/extract-meta styles :pseudo)
        expected '([:&:hover {:color "green"}] [:&:focus {:color "blue"}])]
    (testing "Extracting mode meta data"
      (is (= actual expected)))))

(deftest extract-meta-media
  (let [styles [^{:media {{:screen true} {:color "magenta"}
                          {:max-width "412px"} {:color "blue"}}}
                {:color "white"}

                ^{:media {{:screen true} {:color "purple"}}}
                {:color "white"}]
        expected (list (at-media {:screen true} [:& {:color "purple"}])
                       (at-media {:max-width "412px"} [:& {:color "blue"}]))
        actual (impl/extract-meta styles :media)]
    (testing "Extracting media meta data"
      (is (= actual expected)))))

(deftest prepare-data
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
                   :prefix true})]
        expected {:style {:color "black",
                          :background-color "red",
                          :border-radius "5px",
                          :font-style "italic",
                          :font-weight "bold"
                          :transition "all 1s ease-out"}
                  :vendors ["ms" "webkit"]
                  :prefix true
                  :supports (list (at-supports {:display :grid} [:& {:font-size "24px"}]))
                  :pseudo (list [:&:hover {:color "magenta"}])
                  :media (list (at-media {:screen true} [:& {:background-color "blue"}]))}
        actual (impl/prepare-data styles)]
    (testing "Prepare data"
      (is (= actual expected)))))

(deftest sanitize
  (testing "Sanitize names"
    (is (= (impl/sanitize "demo/examples/main-/-nested-fn!")
           "demo_examples_main-_-nested-fn_"))
    (is (= (impl/sanitize :some$keyword*!)
           "some_keyword__"))))

(deftest compose-selector
  (testing "Composing selector"
    (is (= (impl/compose-selector "demo.examples/anonymous-660680338" nil)
           "demo_examples_anonymous-660680338"))

    (is (= (impl/compose-selector "demo/examples/cycle-color" "purple")
           "demo_examples_cycle-color_purple"))

    (is (= (impl/compose-selector "demo/examples/style-group-static" :keyword)
           "demo_examples_style-group-static_keyword"))))

(deftest create-data-string
  (testing "Creating data string"
    (is (= (impl/create-data-string "demo/examples/width-vary-component" :keyed)
           "demo.examples/width-vary-component[:keyed]"))
    (is (= (impl/create-data-string "demo/examples/pulse-component-two" nil)
           "demo.examples/pulse-component-two" ))
    (is (= (impl/create-data-string "demo/examples/main/nested-fn" nil)
           "demo.examples.main/nested-fn"))))

(defn test-fn-1
  []
  (letfn [(fn-binding [] {:some :map})]
    (impl/get-name fn-binding "herb.unit-tests" {})))

(defn test-fn-2
  []
  (impl/get-name test-fn-1 "herb.unit-tests" {}))

(defn test-fn-3
  []
  (impl/get-name #({}) "herb.unit-tests" {}))

(defn test-fn-4
  []
  {:background :red})

(deftest get-name
  (testing "getting function name"
    (is (= (subs (test-fn-1) 0 36)
           "herb.unit-tests/test-fn-1/fn-binding"))
    (is (= (test-fn-2)
           "herb.unit-tests/test-fn-1"))
    (is (= (test-fn-3)
           "herb.unit-tests/test-fn-3/anonymous-15128758"))))

(deftest with-style!
  (testing "with-style! entry point"
    (is (= (impl/with-style! {} "test-fn-1" "herb.unit-tests" test-fn-4)
           "herb_unit-tests_test-fn-4"))
    (is (= (impl/with-style! {:style? true} "test-fn-1" "herb.unit-tests" test-fn-4)
           ".herb_unit-tests_test-fn-4 {
  background: red;
}"))
    (is (= @runtime/injected-styles
           {"herb_unit-tests_test-fn-4"
            {:data
             {".herb_unit-tests_test-fn-4"
              {:style {:background :red},
               :pseudo nil,
               :vendors nil,
               :prefix nil,
               :supports nil,
               :media nil}},
             :data-string "herb.unit-tests/test-fn-4",
             :css ".herb_unit-tests_test-fn-4 {\n  background: red;\n}"}}))))
