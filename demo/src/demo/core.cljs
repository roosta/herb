(ns demo.core
  (:require
   [herb.core :refer-macros [<class <id] :as herb]
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
  ^{:media {{:screen :only :min-width (em 32)} {:width (rem 33)}
            {:screen :only :min-width (em 52)} {:width (rem 53)}}}
  {:margin-right "auto"
   :margin-left "auto"})


(defn container
  []
  (into [:div {:class (<class container-style)}]
        (r/children (r/current-component))))

(defn paper-style
  []
  {:background "white"
   :flex-basis "100%"
   :box-sizing "border-box"
   :box-shadow [[0 (px 10) 0 0 "#333"]]})

(defn paper
  []
  (into [:div {:class (<class paper-style)}]
        (r/children (r/current-component))))

(def global-style
  (list [:body {:background "#EFEFEF"
                :font-family ["Open sans" "sans-serif"]}]
        [:a {:text-decoration "none"
             :color "#09f"}]))

(defn flex-container
  []
  {:display "flex"
   :flex-wrap "wrap"
   :justify-content "center"})

(defn header-style
  [component]
  (let [styles {:container {:flex-basis "100%"
                            :margin-bottom (px 10)}
                :heading
                {:font-size  (px 112)
                 :margin [[0 0 (px -35) (px -11)]]
                 :color "#333"}
                :box
                {:display "flex"
                 :align-items "center"
                 :text-transform "uppercase"
                 :background "#333"
                 :font-weight "bold"
                 :font-size (px 56)
                 :color "white"}
                :subheading {:margin-left (px -4)}}]
    (with-meta (component styles) {:key component})))

(defn header
  []
  [:div {:class (<class header-style :container)}
   [:h4 {:class (<class header-style :heading)}
    "Herb"]
   [:div {:class (<class header-style :box)}
    [:div {:class (<class header-style :subheading)}
     "styling library demo"]]])

(defn home-page []
  (let [state (r/atom "green")]
    (fn []
      #_(profile
         {}
         (doseq [n (range 500)]
           (p ::<class (<class profile-comp n)))

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
       [:div {:class (<class flex-container)}
        [header]
        [paper "hello"]]
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
