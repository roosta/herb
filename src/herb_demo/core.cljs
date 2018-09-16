(ns herb-demo.core
  (:require
   [herb.core :include-macros true :refer [<class defgroup <id global-style!]]
   [garden.selectors :as s]
   [garden.core :refer [css]]
   [garden.stylesheet :refer [at-media]]
   [taoensso.tufte :as tufte :refer-macros [defnp p profiled profile]]
   [garden.core :refer [css]]
   [garden.units :refer [px em]]
   [reagent.debug :as d]
   [reagent.core :as r]))

(tufte/add-basic-println-handler! {})
(enable-console-print!)

(def red {:color "red"})
(def green {:color "green"})

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

(defn asd
  []
  ^{:pseudo {:hover {:background-color "black"}}}
  {:color "red"})

(defn extend-mode-meta
  []
  ^{:extend asd
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
    {:extend [[font-weight "bold"] [asd]]}))

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
      :box {:background-color "#eee"
            :padding (px 12)}
      :text-2 {:color "cyan"}})
    {:group true
     :key component}))
;; (def some-other-style {:background-color "black"})
;; (def some-more-style {:border-radius "5px"})
;; (def some-style
;;   ^{:extend [some-other-style some-more-style]}
;;   {:color "blue"})

(defgroup group-macro
  {:text {:font-weight "bold"
          :color "white"}
   :box {:background-color "#333"
         :padding (px 5)
         :margin [["10px" 0 "10px" 0]]
         :border-radius "5px"}})

(defn home-page []
  (let [state (r/atom "green")]
    (fn []
      #_(profile
         {}
         (doseq [n (range 500)]
           (p ::with-style (with-style profile-comp n)))

         (doseq [_ (range 500)]
           (p ::manipulate-dom (.appendChild (.-head js/document)
                                             (.createElement js/document (str "style")))))

         (doseq [_ (range 500)]
           (p ::garden (css [:.classname {:width (px 100)
                                          :height (px 100)
                                          :background-color "magenta"
                                          :border-radius "5px"}])))

         (doseq [_ (range 500)]
           (p ::at-media (at-media {:max-width "256px"} [:.classname {:width (px 100)
                                                                      :height (px 100)
                                                                      :background-color "magenta"
                                                                      :border-radius "5px"}]))))

      [:div
       [:div {:class (<class style-group-stateful :box "#eee")}
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

(defn mount-root []
  (r/render [home-page] (.getElementById js/document "demo")))

(defn init!
  []
  (mount-root)
  (global-style! (list [:body
                        {:font-family ["Helvetica Neue" "Verdana" "Helvetica" "Arial" "sans-serif"]
                         :max-width (px 600)
                         :margin "0 auto"
                         :padding-top (px 72)
                         :-webkit-font-smoothing "antialiased"
                         :font-size (em 1.125)
                         :color "#333"
                         :line-height (em 1.5)
                         }])))
