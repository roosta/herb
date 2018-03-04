(ns demo.core
  (:require
   [herb.core :refer-macros [<class <id] :as herb]
   [demo.components.text :refer [text]]
   [demo.components.container :refer [container]]
   [demo.components.paper :refer [paper]]
   [garden.units :refer [rem em px]]
   [reagent.debug :as d]
   [reagent.core :as r]))

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

(defn introduction-style
  [component]
  (let [styles {:title {:margin [[(px 12) 0 (px 12) 0]]}
                :body {}}]
    (with-meta (component styles) {:key component})))

(defn introduction
  []
  [paper
   [text {:class (<class introduction-style :title)
          :variant :title}
    "Introduction"]
   [text {:variant :body}
    "Herb is a CSS styling library for " [:a {:href "https://clojurescript.org"}
                                          "Clojurescript"]
    " whos main focus is component level styling using functions. It's a bit like "
    [:a {:href "https://github.com/css-modules/css-modules"}
     "CSS modules"]
    " but instead of generating classnames randomly, Herb levages the CLJS
    compiler to ensure no name collisions by using the fully qualified name of
    an input function as its selector."]])

(defn home-page []
  (let [state (r/atom "green")]
    (fn []
      [container
       [header]
       [introduction]])))

(defn mount-root []
  (r/render [home-page] (.getElementById js/document "app")))

(defn init! []
  (herb/set-global-style! global-style)
  (mount-root))
