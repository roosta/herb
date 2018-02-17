(ns herb.core-test
  (:require [cljs.test :as t :refer-macros [deftest testing is]]
            [garden.stylesheet :refer [at-media at-keyframes]]
            [herb.core :as core]))

(deftest convert-modes
  (let [input {:hover {:opacity 1}
               :focus {:color "yellow"}}
        expected (list [:&:hover {:opacity 1}] [:&:focus {:color "yellow"}])]
    (testing "Converting modes"
      (is (= (core/convert-modes input) expected)))))

(deftest convert-media
  (let [input {{:screen true} {:color "white"}
               {:min-width "100px"} {:color "blue"}}
        expected (list (at-media {:screen true} [:& {:color "white"}])
                       (at-media {:min-width "100px"} [:& {:color "blue"}]))]
    (testing "Converting media queries"
      (is (= (core/convert-media input) expected)))))

(deftest resolve-style-fns
  (letfn [(button []
            {:border-radius "5px"})
          (box [color] {:background-color color})]
    (let [expected [{:border-radius "5px"} {:background-color "green"}]]
      (testing "Resolve styles"
        (is (= (core/resolve-style-fns [button [box "green"]] [])
               expected))))))

(deftest extract-styles
  (letfn [(text [] {:font-size "24px"})
          (box [color] ^{:extend text} {:background-color color})
          (button [] ^{:extend [[box "red"]]} {:border-radius "5px"})]
    (let [expected [{:font-size "24px"} {:background-color "red"} {:border-radius "5px"}]]
      (testing "Extract styles"
        (is (= (core/extract-styles button []) expected))))))

(deftest extract-meta-mode
  (let [styles [^{:mode {:hover {:color "red"}
                         :focus {:color "blue"}}}
                {:color "white"}

                ^{:mode {:hover {:color "green"}}}
                {:border "2px solid black"}]
        actual (core/extract-meta styles :mode)
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
        actual (core/extract-meta styles :media)]
    (testing "Extracting media meta data"
      (is (= actual expected)))))

(deftest prepare-styles
  (let [styles [(with-meta
                  {:color "white"
                   :background-color "green"}
                  {:mode {:hover {:color "magenta"}}})
                (with-meta
                  {:color "black"
                   :border-radius "5px"}
                  {:media {{:screen true} {:background-color "yellow"}}})
                {:background-color "red"
                 :font-style "italic"}]
        expected [{:color "black"
                   :background-color "red"
                   :border-radius "5px"
                   :font-style "italic"}
                  (list [:&:hover {:color "magenta"}])
                  (list (at-media {:screen true} [:& {:background-color "yellow"}]))]
        actual (core/prepare-styles styles)]
    (testing "Prepare styles"
      (is (= actual expected)))))

(deftest garden-data
  (let [classname "a_namespace_a-fn"
        styles '({:color "black",
                  :background-color "green",
                  :border-radius "5px"})
        expected [".a_namespace_a-fn"
                  {:color "black",
                   :background-color "green",
                   :border-radius "5px"}]
        actual (core/garden-data classname styles)]
    (testing "Garden data"
      (is (= actual expected)))))
