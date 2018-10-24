(ns herb.spec
  (:require [clojure.spec.alpha :as s]))

(s/def ::auto-prefix (s/coll-of (s/or :keyword keyword? :string string?) :kind set?))
(s/def ::vendors (s/coll-of (s/or :string string? :keyword keyword?) :kind vector?))
(s/def ::options (s/keys :opt-un [::vendors ::auto-prefix]))
(s/def ::style-fn (fn [[f a]]
                    (and (fn? f)
                         (map? (apply f a)))))
(s/def ::classes (s/+ (s/or :s string? :n nil?)))
(s/def ::frame (s/or :frame (s/cat :keyframe keyword? :style map?)))
(s/def ::style (s/or :frame (s/cat :classname keyword? :style map?)))
(s/def ::frames (s/+ ::frame))
(s/def ::styles (s/+ ::style))

(s/fdef herb.core/defkeyframes
  :args (s/cat :name symbol? :frames ::frames)
  :ret any?)

(s/fdef herb.core/defglobal
  :args (s/cat :name symbol? :styles ::styles)
  :ret any?)

(s/fdef herb.core/<keyframes
  :args (s/cat :keyframes symbol?)
  :ret any?)
