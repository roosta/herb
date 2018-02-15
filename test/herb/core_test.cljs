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
        (is (= (core/extract-styles button) expected))))))
