(ns demo.core
  (:require
   [herb.core :refer-macros [<class <id] :as herb]
   [demo.components.text :refer [text]]
   [demo.examples.intro :as intro]
   [demo.components.container :refer [container]]
   [demo.intro :refer [intro]]
   [demo.css-garden :refer [css-garden]]
   [demo.why-fns :refer [why-fns]]
   [demo.components.code :refer [code]]
   [demo.components.paper :refer [paper]]
   [garden.units :refer [rem em px]]
   [reagent.debug :as d]
   [reagent.core :as r])
  (:require-macros
   [demo.macros :as macros]))

(def global-style
  (list [:body {:background "#eee"
                :box-sizing "border-box"
                :font-size (px 14)
                :font-family ["Lato" "Helvetica Neue" "Arial" "Helvetica" "sans-serif"]}]
        [:button {:border "none"}]
        [:code {:background-color "#eee"
                :border-radius "2px"
                :padding (px 2)}]
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

(defn app []
  (let [state (r/atom "green")]
    (fn []
      [container
       [header]
       [intro]
       [why-fns]
       [css-garden]])))

(defn mount-root []
  (r/render [app] (.getElementById js/document "app")))

(defn init! []
  (herb/set-global-style! global-style)
  (mount-root))
