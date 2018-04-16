(ns herb.unit-tests
  (:require [clojure.test :as t :refer [deftest testing is]]
            [garden.stylesheet :refer [at-media at-keyframes]]
            [herb.impl :as impl]))

(deftest convert-modes
  (let [input {:hover {:opacity 1}
               :focus {:color "yellow"}}
        expected (list [:&:hover {:opacity 1}] [:&:focus {:color "yellow"}])]
    (testing "Converting modes"
      (is (= (impl/convert-modes input) expected)))))

(deftest convert-media
  (let [input {{:screen true} {:color "white"}
               {:min-width "100px"} {:color "blue"}}
        expected (list (at-media {:screen true} [:& {:color "white"}])
                       (at-media {:min-width "100px"} [:& {:color "blue"}]))]
    (testing "Converting media queries"
      (is (= (impl/convert-media input) expected)))))

(deftest resolve-style-fns
  (letfn [(button []
            {:border-radius "5px"})
          (box [color] {:background-color color})]
    (let [expected [{:border-radius "5px"} {:background-color "green"}]]
      (testing "Resolve styles"
        (is (= (impl/resolve-style-fns [[[button] [box "green"]]] [])
               expected))))))

(deftest extract-styles
  (letfn [(text [] {:font-size "24px"})
          (box [color] ^{:extend text} {:background-color color})
          (button [] ^{:extend [[box "red"]]} {:border-radius "5px"})]
    (let [expected [{:font-size "24px"} {:background-color "red"} {:border-radius "5px"}]]
      (testing "Extract styles"
        (is (= (impl/extract-styles button []) expected))))))

(deftest extract-meta-mode
  (let [styles [^{:mode {:hover {:color "red"}
                         :focus {:color "blue"}}}
                {:color "white"}

                ^{:mode {:hover {:color "green"}}}
                {:border "2px solid black"}]
        actual (impl/extract-meta styles :mode)
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
                  {:mode {:hover {:color "magenta"}}})
                (with-meta
                  {:color "black"
                   :border-radius "5px"}
                  {:media {{:screen true} {:background-color "yellow"}}})
                {:background-color "red"
                 :font-style "italic"}]
        expected {:style {:color "black"
                          :background-color "red"
                          :border-radius "5px"
                          :font-style "italic"}
                  :mode (list [:&:hover {:color "magenta"}])
                  :media (list (at-media {:screen true} [:& {:background-color "yellow"}]))}
        actual (impl/prepare-data styles)]
    (testing "Prepare styles"
      (is (= actual expected)))))

(deftest attach-selector
  (let [selector "a_namespace_a-fn"
        styles {:style {:color "black",
                        :background-color "green",
                        :border-radius "5px"}}
        expected-id ["#a_namespace_a-fn"
                     {:style {:color "black",
                              :background-color "green",
                              :border-radius "5px"}}]
        expected-class [".a_namespace_a-fn"
                        {:style {:color "black",
                                 :background-color "green",
                                 :border-radius "5px"}}]
        actual-class (impl/attach-selector selector styles false)
        actual-id (impl/attach-selector selector styles true)]
    (testing "Garden data"
      (is (= actual-class expected-class))
      (is (= actual-id expected-id)))))
