(ns site.core
  (:require
   [herb.core :refer-macros [<class <id] :as herb]
   [site.components.text :refer [text]]
   [site.examples.intro :as intro]
   [site.components.container :refer [container]]
   [site.intro :refer [intro]]
   [site.css-garden :refer [css-garden]]
   [site.why-fns :refer [why-fns]]
   [site.components.code :refer [code]]
   [site.components.paper :refer [paper]]
   [garden.units :refer [rem em px]]
   [reagent.debug :as d]
   [reagent.core :as r])
  (:require-macros
   [site.macros :as macros]))

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
    "Clojurescript styling library site"]])

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
  (herb/set-global-style global-style)
  (mount-root))
