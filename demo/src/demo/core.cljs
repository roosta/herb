(ns demo.core
  (:require
   [herb.macro :refer-macros [with-style]]
   [garden.selectors :as s]
   [garden.core :refer [css]]
   ;; [taoensso.tufte :as tufte :refer-macros (defnp p profiled profile)]
   [garden.core :refer [css]]
   [garden.units :refer [px]]
   [reagent.debug :as d]
   [reagent.core :as r]))

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

(defn hover-focus
  []
  ^{:mode {:hover {:background-color "green"}
           :focus {:background-color "yellow"}}}
  {:background-color "red"}
  )

(defn dynamic-text-color
  [color]
  (case color
    (or "blue" "purple") {:color "white"}
    {:color "black"}))

(defn margin
  []
  {:margin "5px"})

(defn italic
  []
  {:font-style "italic"})

(defn bold
  []
  ^{:extend [italic margin]}
  {:font-weight "bold"})

(defn cycle-color
  [color]
  ^{:key color
    :extend [[dynamic-text-color color] bold]}
  {:background-color color}
  )

(defn button
  []
  {:margin (px 10)})

(defn blue-div
  []
  {:background-color "blue"})

(defn red-div
  []
  ^{:extend blue-div}
  {:background-color "red"})

(defn cyan-div
  []
  ^{:extend red-div}
  {:background-color "cyan"})

(defn keyed
  [k]
  (let [styles {:paper {:background-color "grey"
                        :width (px 50)
                        :height (px 50)
                        :border-radius (px 5)}
                :sheet {:background-color "white"
                        :width (px 50)
                        :height (px 50)
                        :box-shadow "0 3px 6px rgba(0, 0, 0, 0.16), 0 3px 6px rgba(0, 0, 0, 0.23)"}}]
    (with-meta
      (k styles)
      {:key (name k)})))

(defn profile
  [n]
  ^{:key n}
  {:width (px 100)
   :height (px 100)
   :background-color "magenta"
   :border-radius "5px"})

(defn home-page []
  (let [state (r/atom "green")]
    (fn []
      #_(time (doseq [n (range 500)]
                (with-style profile n)))

      #_(time (doseq [n (range 500)]
                (.appendChild (.-head js/document)
                              (.createElement js/document (str "style")))))
      #_(time (doseq [n (range 500)]
                (css [:.classname {:width (px 100)
                                   :height (px 100)
                                   :background-color "magenta"
                                   :border-radius "5px"}])))
      [:div
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
       [:div {:class (with-style keyed :sheet)}]])))

(defn mount-root []
  (r/render [home-page] (.getElementById js/document "app")))

(defn init! []
  (mount-root))

(init!)
