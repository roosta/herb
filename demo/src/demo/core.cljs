(ns demo.core
  (:require
   [herb.core :refer-macros [with-style]]
   [garden.selectors :as s]
   [garden.core :refer [css]]
   [garden.stylesheet :refer [at-media]]
   [taoensso.tufte :as tufte :refer-macros [defnp p profiled profile]]
   [garden.core :refer [css]]
   [garden.units :refer [px]]
   [reagent.debug :as d]
   [reagent.core :as r]))

(tufte/add-basic-println-handler! {})
(enable-console-print!)

(def red {:color "red"})
(def green {:color "green"})

(defn state-hover
  [color]
  ^{:mode {:hover {:color "yellow"}}}
  {:margin-bottom 0
   :background-color color})

(defn toggle-color
  [color]
  (case color
    "red" "green"
    "green" "red"))

(defn asd
  []
  ^{:mode {:hover {:background-color "black"}}}
  {:color "red"})

(defn extend-mode-meta
  []
  ^{:extend asd
    :mode {:hover {:background-color "green"}}}
  {:color "purple"})

(defn hover-focus
  []
  ^{:extend extend-mode-meta
    :mode {:hover {:background-color "blue"}
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
    {:extend [font-weight "bold"]}))

(defn margin
  []
  {:margin "5px"})

(defn italic
  []
  ^{:mode {:hover {:font-style "normal"}}}
  {:font-style "italic"})

(defn bold
  []
  ^{:extend [[italic] [margin]]}
  {:font-weight "bold"})

(defn cycle-color
  [color]
  ^{:key color
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
    :media {{:screen true} {:color "black"
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
                ^{:mode {:active {:color "white"}
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

(defn tmp-1
  [color]
  ^{:key color}
  {:background-color color})

(defn tmp-2
  [color]
  ^{:key color
    :extend [tmp-1 "green"]}
  {:color color})

(defn tmp-3
  []
  ^{:extend [tmp-2 "red"]}
  {:color "yellow"})

;; (def some-other-style {:background-color "black"})
;; (def some-more-style {:border-radius "5px"})
;; (def some-style
;;   ^{:extend [some-other-style some-more-style]}
;;   {:color "blue"})

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
                                                                      :border-radius "5px"}])))
         )

      [:div
       [:div {:class (with-style tmp-3)}
        "hello"]

       [:div

        [:div
         [:div {:class (with-style (fn []
                                     ^{:extend button}
                                     {:background-color "black"
                                      :color "white"}))}
          "Anon function with meta"]
         [:div {:class (with-style (fn [color]
                                     {:background-color color
                                      :color "white"})
                         "black")}
          "Anon function without meta"]]

        [:input {:class (with-style hover-focus)
                 :default-value "Hello world"}]
        [:div
         [:h2 {:class (with-style state-hover @state)}
          "Welcome to Reagent"]
         [:button {:class (with-style button)
                   :on-click #(reset! state (toggle-color @state))}
          "Toggle"]
         (for [c ["yellow" "blue" "green" "purple"]]
           ^{:key c}
           [:div {:class (with-style cycle-color c)}
            c])
         [:br]
         [:div {:class (with-style cyan-div)}
          "inheritance test"]]
        [:div {:class (with-style keyed :paper)}]

        [:div {:class (with-style keyed :sheet)}]

        [:div {:class (with-style media-query-test)}
         "Media query test"]]
       #_[:div {:class (with-style some-style)}
        "Regular maps"]
       ])))

(defn mount-root []
  (r/render [home-page] (.getElementById js/document "app")))

(defn init! []
  (mount-root))

(init!)
