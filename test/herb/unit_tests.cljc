(ns herb.unit-tests
  (:require [clojure.test :as t :refer [deftest testing is]]
            [garden.stylesheet :refer [at-media at-keyframes]]
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
                  {:media {{:screen true} {:background-color "blue"}}})]
        expected {:style {:color "black"
                          :background-color "red"
                          :border-radius "5px"
                          :font-style "italic"}
                  :pseudo (list [:&:hover {:color "magenta"}])
                  :media (list (at-media {:screen true} [:& {:background-color "blue"}]))}
        actual (impl/prepare-data styles)]
    (testing "Prepare styles"
      (is (= actual expected)))))
