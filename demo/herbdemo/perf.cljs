(ns herbdemo.perf
  (:require [taoensso.tufte :as tufte :refer-macros [defnp p profiled profile]]
            [herb.core :refer [<class]]
            [garden.stylesheet :refer [at-media at-keyframes]]
            [garden.core :refer [css]]
            [garden.units :refer [rem em px]]))

;; (tufte/add-basic-println-handler! {})

(defn profile-comp
  [n]
  ^{:key n
    :group true}
  {:width (px 100)
   :height (px 100)
   :background-color "magenta"
   :border-radius "5px"})

(defn performance
  []
  (profile
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
                                                                :border-radius "5px"}])))))
