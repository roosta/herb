(ns site.components.text
  (:require [garden.units :refer [rem em px]]
            [herb.core :refer [<class]]
            [clojure.string :as str]
            [reagent.core :as r]))

(def mappings
  {:display :h1
   :headline :h1
   :title :h2
   :subheading :h3
   :body :p})

(defn colors
  [c]
  (c {:black {:color "#333"}
      :white {:color "white"}}))

(defn variants
  [variant]
  (let [v {:display {:font-size  (rem 7)
                     :font-weight 400
                     :margin 0
                     :line-height (em 1.14286)
                     :letter-spacing (em -0.04)}
           :headline {:font-size (rem 1.5)
                      :font-weight 400
                      :line-height (em 1.16667)
                      :text-align "center"}
           :title {:font-size (rem 1.3125)
                   :font-weight 500
                   :margin 0
                   :margin-bottom (em 0.35)
                   :line-height (em 1.16667)
                   :text-align "center"}
           :subheading {:font-size (rem 1)
                        :font-weight 400
                        :line-height (em 1.5)}
           :body {:font-size (rem 0.875)
                  :margin-bottom (px 16)
                  :font-weight 400
                  :line-height (em 1.46429)}}]
     (variant v)))

(defn aligns
  [align]
  (align {:left {:text-align "left"}
          :right {:text-align "right"}
          :center {:text-align "center"}
          :justify {:text-align "justify"}}))

(defn text-style
  [variant color align]
  (let [k (str/join "-" [(name variant) (name color) (name align)])]
    (with-meta
      (merge
       (variants variant)
       (aligns align)
       (colors color))
      {:key k})))

(defn text
  [{:keys [variant class color align]
    :or {variant :body
         color :black
         align :left}}]
  (let [class* (<class text-style variant color align)]
    (into
     [(variant mappings) {:class (if class
                                   (str/join " " [class* class])
                                   class*)}]
     (r/children (r/current-component)))))
