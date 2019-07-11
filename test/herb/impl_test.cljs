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
      (let [expected [{:border-radius "5px"} {:background-color "green"} {:background-color "red"}]]
        (is (= (#'herb.impl/resolve-style-fns [[[fn-1] [fn-2 "green"] [fn-3]]])
               expected))))))

(deftest extract-styles
  (testing "extracting extended style functions"
    (letfn [(parent [] {:font-size "24px"})
            (extend-1 [color] ^{:extend parent} {:background-color color})
            (extend-2 [] ^{:extend [[extend-1 "red"]]} {:border-radius "5px"})]
      (let [expected [{:font-size "24px"} {:background-color "red"} {:border-radius "5px"}]]
        (is (= (#'herb.impl/extract-styles extend-2) expected))))))

(deftest extract-meta-pseudo
  (testing "extracting meta data"
    (let [styles [^{:pseudo {:hover {:color "red"}
                             :focus {:color "blue"}}}
                  {:color "white"}

                  ^{:pseudo {:hover {:color "green"}}}
                  {:border "2px solid black"}]
          actual (#'herb.impl/extract-meta styles :pseudo)
          expected '([:&:hover {:color "green"}] [:&:focus {:color "blue"}])]
      (is (= actual expected)))))

(deftest extract-meta-media
  (testing "Extracting media meta data"
    (let [styles [^{:media {{:screen true} {:color "magenta"}
                            {:max-width "412px"} {:color "blue"}}}
                  {:color "white"}

                  ^{:media {{:screen true} {:color "purple"}}}
                  {:color "white"}]
          expected (list (at-media {:screen true} [:& {:color "purple"}])
                         (at-media {:max-width "412px"} [:& {:color "blue"}]))
          actual (#'herb.impl/extract-meta styles :media)]
      (is (= actual expected)))))

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
          expected {:style {:color "black",
                            :background-color "red",
                            :border-radius "5px",
                            :font-style "italic",
                            :font-weight "bold"
                            :transition "all 1s ease-out"
                            :box-sizing "border-box"}
                    :vendors ["ms" "webkit"]
                    :prefix true
                    :supports (list (at-supports {:display :grid} [:& {:font-size "24px"}]))
                    :pseudo (list [:&:hover {:color "magenta"}])
                    :media (list (at-media {:screen true} [:& {:background-color "blue"}]))
                    :combinators {[:> :div :span] {:background "red"}
                                  [:+ :p] {:background "purple"}
                                  [:- :div] {:background "yellow"}
                                  [:descendant :div] {:background "green"}}}
          actual (#'herb.impl/prepare-data styles)]
      (is (= actual expected)))))

(deftest sanitize
  (testing "Sanitize names"
    (is (= (#'herb.impl/sanitize "demo/examples/main-/-nested-fn!")
           "demo_examples_main-_-nested-fn_"))
    (is (= (#'herb.impl/sanitize :some$keyword*!)
           "some_keyword__"))))

(deftest compose-selector
  (testing "Composing selector"
    (is (= (#'herb.impl/compose-selector "demo.examples/anonymous-660680338" nil)
           "demo_examples_anonymous-660680338"))

    (is (= (#'herb.impl/compose-selector "demo/examples/cycle-color" "purple")
           "demo_examples_cycle-color_purple"))

    (is (= (#'herb.impl/compose-selector "demo/examples/style-group-static" :keyword)
           "demo_examples_style-group-static_keyword"))))

(deftest create-data-string
  (testing "Creating a data string from cljs function name"
    (is (= (#'herb.impl/create-data-string "demo/examples/width-vary-component" :keyed)
           "demo.examples/width-vary-component[:keyed]"))
    (is (= (#'herb.impl/create-data-string "demo/examples/pulse-component-two" nil)
           "demo.examples/pulse-component-two" ))
    (is (= (#'herb.impl/create-data-string "demo/examples/main/nested-fn" nil)
           "demo.examples.main/nested-fn"))))

(defn test-fn-1
  []
  (letfn [(fn-binding [] {:some :map})]
    (#'herb.impl/get-name fn-binding "herb.impl-test" {})))

(defn test-fn-2
  []
  (#'herb.impl/get-name test-fn-1 "herb.impl-test" {}))

(defn test-fn-3
  []
  (#'herb.impl/get-name #({}) "herb.impl-test" {}))

(defn test-fn-4
  []
  ;; ^{:pseudo {:hover {:color "yellow"}}
  ;;   :vendors ["webkit"]
  ;;   :prefix true
  ;;   :supports {{:display :grid} {:font-size "20px"}}
  ;;   :media {{:screen true} {:color "magenta"}}
  ;;   :combinators {[:- :div] {:background "yellow"}}}
  {:background :red})

(deftest get-name
  (testing "getting function name"
    (is (= (subs (test-fn-1) 0 36)
           "herb/impl-test/test-fn-1-/-fn-bindin"))
    (is (= (test-fn-2)
           "herb/impl-test/test-fn-1"))
    (is (= (test-fn-3)
           "herb.impl-test/anonymous-15128758"))))

(deftest with-style!
  (testing "with-style! entry point"
    (is (= (impl/with-style! {} "test-fn-1" "herb.unit-tests" test-fn-4)
           "herb_impl-test_test-fn-4"))
    (is (= (impl/with-style! {:style? true} "test-fn-1" "herb.unit-tests" test-fn-4)
           ".herb_impl-test_test-fn-4 {
  background: red;
}"))

    (let [result (deref (deref #'herb.runtime/injected-styles))
          data (first (:data (val (first result))))]

      (is (= (key (first result)) "herb_impl-test_test-fn-4"))
      (is (= (key data) ".herb_impl-test_test-fn-4"))
      (is (= (:style (val data)) {:background :red}))
      (is (= (:data-string (val (first result))) "herb.impl-test/test-fn-4"))
      (is (= (:css (val (first result))) ".herb_impl-test_test-fn-4 {\n  background: red;\n}")))
    ))
