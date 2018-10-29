(ns herb.spec
  (:require [clojure.spec.alpha :as s]
            [clojure.test.check.generators]))

(s/def ::auto-prefix (s/coll-of (s/or :keyword keyword? :string string?) :kind set?))
(s/def ::vendors (s/coll-of (s/or :string string? :keyword keyword?) :kind vector?))
(s/def ::options (s/keys :opt-un [::vendors ::auto-prefix]))
(s/def ::classes (s/+ (s/or :s string? :n nil?)))
(s/def ::frame (s/or :frame (s/cat :keyframe keyword? :style map?)))
(s/def ::style (s/or :style (s/cat :identifier (s/+ (s/alt :kw keyword? :str string?)) :style map?)))

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
