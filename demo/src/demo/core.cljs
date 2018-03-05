(ns demo.core
  (:require
   [herb.core :refer-macros [<class <id] :as herb]
   ;; [demo.macros :refer-macros [example-src]]
   [demo.components.text :refer [text]]
   [demo.examples.intro :as intro]
   [demo.components.container :refer [container]]
   [demo.components.code :refer [code]]
   [demo.components.paper :refer [paper]]
   [garden.units :refer [rem em px]]
   [reagent.debug :as d]
   [reagent.core :as r])
  (:require-macros
   [demo.macros :as macros]))

(def global-style
  (list [:body {:background "#EEEEEE"
                :box-sizing "border-box"
                :font-size (px 14)
                :font-family ["Lato" "Helvetica Neue" "Arial" "Helvetica" "sans-serif"]}]
        [:a {:text-decoration "none"
             :color "#09f"}]))

(defn header-style
  [component]
  (let [styles {:container {:flex-basis "100%"
                            :margin-bottom (px 10)}}]
    (with-meta (component styles) {:key component})))

(defn header
  []
  [:div {:class (<class header-style :container)}
   [text {:align :center
          :variant :display}
    "Herb"]
   [text {:align :center
          :variant :headline}
    "Clojurescript styling library demo"]])

(defn intro-style
  [component]
  (let [styles {:body {}
                :p {}}]
    (with-meta (component styles) {:key component})))

(defn intro
  []
  (let [e1 (macros/example-src "intro.cljs")]
    [paper
     [text {:variant :title}
      "Intro"]
     [text
      "Herb is a CSS styling library for " [:a {:href "https://clojurescript.org"}
                                            "Clojurescript"]
      " whos main focus is component level styling using functions. It's a bit like "
      [:a {:href "https://github.com/css-modules/css-modules"}
       "CSS modules"]
      " but instead of generating classnames randomly, Herb levages the CLJS
    compiler to ensure no name collisions by using the fully qualified name of
    an input function as its selector."]
     [text 
      "Lets start of with a basic example, I'm using "
      [:a {:href "https://github.com/reagent-project/reagent"}
       "Reagent"]
      " here but it's not a requirement for Herb"]
     [code
      e1]
     [intro/example]
     ]))

(defn home-page []
  (let [state (r/atom "green")]
    (fn []
      [container
       [header]
       [intro]])))

(defn mount-root []
  (r/render [home-page] (.getElementById js/document "app")))

(defn init! []
  (herb/set-global-style! global-style)
  (mount-root))
