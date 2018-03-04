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
                            :margin-bottom (px 10)}
                :heading
                {:font-size  (rem 7)
                 :font-weight 400
                 :line-height (em 1.14286)
                 :margin 0
                 :text-align :center
                 :color "#333"}
                :subheading
                {:font-size (rem 1.5)
                 :font-weight 400
                 :line-height (em 1.16667)
                 :text-align "center"
                 :color "#333"
                 }
                }]
    (with-meta (component styles) {:key component})))

(defn header
  []
  [:div {:class (<class header-style :container)}
   [:h4 {:class (<class header-style :heading)}
    "Herb"]
   [:div {:class (<class header-style :subheading)}
    "Clojurescript styling library demo"]])

(defn introduction-style
  [component]
  (let [styles {:headline {:font-size (rem 1.5)
                           :font-weight 400
                           :margin 0}
                :body {:font-size (rem 0.875)}}]
    (with-meta (component styles) {:key component}))
  )

(defn introduction
  []
  [paper
   [:h2 {:class (<class introduction-style :headline)}
    "Introduction"]])

(defn home-page []
  (let [state (r/atom "green")]
    (fn []
      [container
       [:div {:class (<class flex-container)}
        [header]
        [introduction]]
       ]
      ;; TODO demo inheritance variants
      ;; TODO demo state
      ;; TODO demo media
      ;; TODO demo keys
      ;; TODO demo anon
      ;; TODO demo nested
      )))

(defn mount-root []
  (r/render [home-page] (.getElementById js/document "app")))

(defn init! []
  (herb/set-global-style! global-style)
  (mount-root))
