(ns site.snippets.inline-prefixes)

;; By supplying an outer map with the selected prefix as a key, the inner map is
;; gets prefixed with this prefix
(defn style []
  {:-moz {:border-radius "3px"
          :box-sizing "border-box"}
   :background-color "black"
   :color "white"})
