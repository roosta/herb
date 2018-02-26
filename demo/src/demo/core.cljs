(ns demo.core
  (:require
   [herb.core :refer-macros [with-style] :as herb]
   [garden.selectors :as s]
   [garden.core :refer [css]]
   [garden.stylesheet :refer [at-media]]
   [taoensso.tufte :as tufte :refer-macros [defnp p profiled profile]]
   [garden.core :refer [css]]
   [garden.units :refer [rem em px]]
   [reagent.debug :as d]
   [reagent.core :as r]))

(tufte/add-basic-println-handler! {})
(enable-console-print!)

(defn container-style
  []
  ^{:media {{:screen :only :min-width (em 48)} {:width (rem 49)}
            {:screen :only :min-width (em 64)} {:width (rem 65)}
            {:screen :only :min-width (em 75)} {:width (rem 76)}}}
  {:margin-right "auto"
   :margin-left "auto"})

(defn paper
  []
  )

(defn container
  []
  (into [:div {:class (with-style container-style)}]
        (r/children (r/current-component))))

(def global-style
  (list [:body {:font-family ["Helvetica Neue" "Verdana" "Helvetica" "Arial" "sans-serif"]}]
        [:a {:text-decoration "none"
             :color "#09f"}]))

(defn flex-container
  []
  {:display "flex"
   :justify-content "center"})

(defn header-style
  [component]
  (let [styles {:container {:display "inline-block"}
                :heading
                {:font-size  (px 112)
                 :margin-bottom 0
                 :color "#333"}
                :box
                {:height (px 80)
                 :display "flex"
                 :justify-content "center"
                 :align-items "center"
                 :text-transform "uppercase"
                 :background "#333"
                 :font-size (px 56)
                 :color "white"}
                }]
    (with-meta (component styles) {:key component})))

(defn header
  []
  [:div {:class (with-style header-style :container)}
   [:h4 {:class (with-style header-style :heading)}
    "Herb demo"]
   [:div {:class (with-style header-style :box)}
    [:div
     "styling library"]]])

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

      [container
       [:div {:class (with-style flex-container)}
        [header]]
       ]
      ;; TODO demo inheritance variants
      ;; TODO demo state
      ;; TODO demo media
      ;; TODO demo keys
      ;; TODO demo anon
      )))

(defn mount-root []
  (r/render [home-page] (.getElementById js/document "app")))

(defn init! []
  (herb/set-global-style! global-style)
  (mount-root))

(init!)
