(ns herb-demo.examples
  ;; (:require-macros [garden.def :refer [defkeyframes]])
  (:require
   [herb.core
    :include-macros true
    :refer [<class defgroup join-classes <id defkeyframes defglobal]]
   [garden.selectors :as s]
   [garden.core :refer [css]]
   [garden.stylesheet :refer [at-media]]
   [taoensso.tufte :as tufte :refer-macros [defnp p profiled profile]]
   [garden.core :refer [css]]
   [garden.units :refer [px em]]
   [reagent.debug :as d]
   [reagent.core :as r])
  )

(def red {:color "red"})
(def green {:color "green"})

(defglobal global
  [:.global {:color "magenta"
             :font-size (px 24)}])

(defn state-hover
  [color]
  ^{:pseudo {:hover {:color "yellow"}}}
  {:margin-bottom 0
   :background-color color})

(defn toggle-color
  [color]
  (case color
    "red" "green"
    "green" "red"))

(defn inheritance-meta
  []
  ^{:pseudo {:hover {:background-color "black"}}}
  {:color "red"})

(defn extend-mode-meta
  []
  ^{:extend inheritance-meta
    :pseudo {:hover {:background-color "green"}}}
  {:color "purple"})

(defn hover-focus
  []
  ^{:extend extend-mode-meta
    :pseudo {:hover {:background-color "blue"}
             :focus {:background-color "yellow"}}}
  {:background-color "red"}
  )

(defn font-weight
  [weight]
  {:font-weight weight})

(defn dynamic-text-color
  [color]
  (with-meta
    (case color
      (or "blue" "purple") {:color "white"}
      {:color "black"})
    {:extend [[font-weight "bold"] [inheritance-meta]]}))

(defn margin
  []
  {:margin "5px"})

(defn italic
  []
  ^{:pseudo {:hover {:font-style "normal"}}}
  {:font-style "italic"})

(defn bold
  []
  ^{:extend [[italic] [margin]]}
  {:font-weight "bold"})

(defn cycle-color
  [color]
  ^{:key color
    :group true
    :extend [[dynamic-text-color color] [bold]]}
  {:text-align "center"
   :background-color color}
  )

(defn button
  []
  {:border "2px solid red"
   :margin (px 10)})

(defn blue-div
  []
  {:color "black"
   :background-color "blue"})

(defn red-div
  []
  ^{:extend blue-div
    :media {{:screen true} {:color "magenta"}}}
  {:color "white"
   :background-color "red"})

(defn cyan-div
  []
  ^{:extend red-div
    :media {{:screen true} {:color "white"
                            :background-color "purple"}}}
  {:background-color "cyan"})

(defn media-query-test
  []
  ^{:extend cyan-div
    :media {{:screen true} {:color "white"
                            :background-color "green"}
            {:max-width "800px"} {:background-color "yellow"}}}
  {:background "magenta"
   :text-align "center"
   :color "white"})

(defn keyed
  [k]
  (let [styles {:paper
                {:background-color "grey"
                 :width (px 50)
                 :display "inline-block"
                 :margin (px 10)
                 :height (px 50)
                 :border-radius (px 5)}
                :sheet
                ^{:pseudo {:active {:color "white"}
                           :hover {:border-radius (px 5)}}}
                {:background-color "white"
                 :width (px 50)
                 :margin (px 10)
                 :display "inline-block"
                 :height (px 50)
                 :box-shadow "0 3px 6px rgba(0, 0, 0, 0.16), 0 3px 6px rgba(0, 0, 0, 0.23)"}}]
    (vary-meta
     (k styles)
     assoc :key (name k))))

(defn profile-comp
  [n]
  ^{:key n}
  {:width (px 100)
   :height (px 100)
   :background-color "magenta"
   :border-radius "5px"})

(defn extend-1
  [color]
  ^{:key color}
  {:background-color color})

(defn extend-2
  [color]
  ^{:key color
    :extend [extend-1 "green"]}
  {:color color})

(defn extend-3
  []
  ^{:extend [extend-2 "red"]}
  {:color "yellow"})

(defn style-group-stateful
  [component color]
  (with-meta
    (component
     {:text {:color color}
      :box {:background-color color
            :padding (px 12)}
      :text-2 {:color color}})
    {:group true
     :key component}))

(defn style-group-static
  [component]
  (with-meta
    (component
     {:text {:color "magenta"}
      :box {:background "white"
            :padding (px 12)}
      :text-2 {:color "cyan"}})
    {:group true
     :key component}))

(defgroup group-macro
  {:text {:font-weight "bold"
          :color "white"}
   :box {:background-color "#333"
         :padding (px 5)
         :margin [["10px" 0 "10px" 0]]
         :border-radius "5px"}})

(defgroup group-with-args
  {:text {:font-style "italic"
          :color "white"}
   :box {:background (first args)}})

(defn supports-thing
  []
  ^{:supports {{:display :grid} {:font-size (px 20)}}}
  {:font-size (px 12)}
  )

(defn container
  []
  {:max-width (px 600)
   :margin "0 auto"})

(defn extend-group
  []
  ^{:extend [[group-macro :box] [group-macro :text]]}
  {:background-color "red"})

(defn white-text
  []
  {:color "white"})

(defn inline-prefixes
  []
  {:-moz {:border-radius "3px"
          :box-sizing "border-box"}
   :background-color "black"
   :color "white"})

(defn shadow-prefix
  []
  {:transition "all 1s ease-out"
   :font-size (px 24)})

(defn test-global-prefixes
  []
  ^{:extend shadow-prefix}
  {:background-color "black"
   :border-radius (px 3)
   :color "white"})

(defgroup multiple-vendor-group
  {:component-1
   ^{:vendors ["webkit"]
     :prefix true}
   {:transition "all 1s ease-in"}
   :component-2
   ^{:vendors ["ms"]
     :prefix true}
   {:border-radius "50%"}})

(defgroup multiple-meta-group
  {:component-1
   ^{:pseudo {:hover {:border-radius "50%"}}}
   {:width (px 60)
    :transition "all 0.5s ease-in"
    :height (px 60)
    :background "black"}})

(defn transition
  [c]
  {:transition "all 1s ease-out"})

(defkeyframes pulse-animation
  [:from {:opacity 1}]
  [:to {:opacity 0}])

(defkeyframes width-vary
  [:from {:width "100%"}]
  [:to {:width "50%"}])

(defkeyframes percentages
  ["0%" {:top 0 :left 0}]
  ["30%" {:top (px 50)}]
  ["68%" {:left (px 50)}])

(defn pulse-component
  []
  {
   :animation [[pulse-animation "2s" :infinite :alternate]]
   :background-color "black"
   :width (px 20)
   :height (px 20)}
  )

(defn pulse-component-two
  []
  {:animation [[pulse-animation "2s" :infinite :alternate]]
   :background-color "green"
   :width (px 20)
   :height (px 20)})

(defn width-vary-component
  []
  {:animation [[width-vary "2s" "cubic-bezier(.77, 0, .175, 1)" :infinite :alternate]]
   :background-color "red"
   :height (px 20)})

(defn row
  []
  {:display :flex})

(defn simple
  []
  {:background-color "red"})

(def data [{:color :red} {:color :green}])

(defn data-str
  [in]
  ^{:key in}
  {:background-color (:color in)})

(defn main []
  (let [state (r/atom "green")]
    (fn []
      [:div {:class (<class container)}
       (for [d data]
         ^{:key d}
         [:div {:class (<class data-str d)}
          (:color d)])
       [:div {:class (<class group-with-args :box "black")}
        [:span {:class (<class group-with-args :text)}
         "Group that takes args"]]
       [:div.global "global style"]
       [:div {:class (join-classes (<class row) (<class simple))}
        "multiple classes"]
       [:div {:class (<class multiple-meta-group :component-1)}]
       [:div {:class (<class multiple-vendor-group :component-1)}
        "Component-1"]
       [:div {:class (<class multiple-vendor-group :component-2)}
        "Component-2"]
       [:div {:class (<class row)}
        [:div {:class (<class pulse-component)}]
        [:div {:class (<class pulse-component-two)}]
        [:div {:class (<class width-vary-component)}]]
       [:div {:class (<class transition @state)}
        [:button {:on-click #(reset! state (toggle-color @state))}
         "Change color"]]
       [:div {:class (<class inline-prefixes)}
        "inline vendor prefixes"]
       [:div {:class (<class supports-thing)}
        "Testing supports queries"]
       [:div {:class (<class extend-group)}
        "Extend a style group"]
       [:div {:class [(<class group-macro :box) (<class white-text) "hello" nil]}
        "Multiple classes"]

       [:div {:class (<class style-group-stateful :box "#ddd")}
        [:span {:class (<class style-group-stateful :text @state)}
         "group meta test with arguments"]
        [:br]
        [:span {:class (<class style-group-stateful :text-2 "red")}
         "group meta test with arguments"]]

       [:div {:class (<class style-group-static :box)}
        [:span {:class (<class style-group-static :text)}
         "Group meta test without arguments"]
        [:br]
        [:span {:class (<class style-group-static :text-2)}
         "Group meta test without arguments"]]

       [:div {:class (<class group-macro :box)}
        [:span {:class (<class group-macro :text)}
         "Group macro test"]]

       [:div

        [:div {:class (<class extend-3)}
         "Extend test"]

        [:div
         [:div {:class (<class (fn []
                                 ^{:extend button}
                                 {:background-color "black"
                                  :color "white"}))}
          "Anon function with meta"]
         [:div {:class (<class (fn [color]
                                 {:background-color color
                                  :color "white"})
                               "black")}
          "Anon function without meta"]]

        (letfn [(nested-fn [color]
                  {:background-color color
                   :color "white"})]
          [:div {:class (<class nested-fn "black")}
           "letfn block"])

        [:div {:class (<class (fn named-anon []
                                {:background-color "pink"
                                 :color "black"}))}
         "Named anon fn"
         ]

        [:input {:class (<class hover-focus)
                 :default-value "test modes hover, active"}]
        [:div
         [:h2 {:class (<class state-hover @state)}
          "Testing hover and state change via button"]
         [:button {:class (<class button)
                   :on-click #(reset! state (toggle-color @state))}
          "change color"]
         (for [c ["yellow" "blue" "green" "purple"]]
           ^{:key c}
           [:div {:class (<class cycle-color c)}
            c])
         [:br]
         [:div {:class (<class cyan-div)}
          "inheritance test"]]
        [:div {:class (<class keyed :paper)}]

        [:div {:class (<class keyed :sheet)}]

        [:div {:class (<class media-query-test)}
         "Media query test"]]
       #_[:div {:class (with-style some-style)}
          "Regular maps"]
       ])))
