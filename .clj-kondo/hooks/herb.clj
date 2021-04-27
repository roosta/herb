(ns hooks.herb
  (:require [clj-kondo.hooks-api :as api]))

; https://github.com/clj-kondo/clj-kondo/blob/master/doc/hooks.md
; https://github.com/clj-kondo/config

(defn global-keyframes [[m1 m2]]
  (fn [{:keys [node]}]
    (let [[sym & body] (rest (:children node))
          sexpr (api/sexpr node)
          new-node (api/list-node
                     (list
                       (api/token-node 'def)
                       (api/token-node sym)
                       (api/list-node
                         (list* body))))]
      (when-not (symbol? (second sexpr))
        (throw (ex-info m1 {})))
      (when-not (every? vector? (nthrest sexpr 2))
        (throw (ex-info m2 {})))
      {:node new-node})))

(defn defgroup [{:keys [:node]}]
  (let [[sym smap] (rest (:children node))
        sexpr (api/sexpr node)
        body (api/list-node
               (list
                 (api/token-node 'let)
                 (api/vector-node
                   [(api/token-node 'style)
                    (api/list-node
                      (list
                        (api/token-node 'get)
                        smap
                        (api/token-node 'component)))])
                 (api/list-node
                   (list
                     (api/token-node 'identity)
                     (api/token-node 'args)))
                 (api/token-node 'style)))
        new-node (api/list-node
                   (list
                     (api/token-node 'defn)
                     sym
                     (api/vector-node [(api/token-node 'component) (api/token-node 'args)])
                     body))]
    (when-not (map? (nth sexpr 2))
      (throw (ex-info "Second argument to defgroup needs to be a map" {})))
    (when-not (symbol? (second sexpr))
      (throw (ex-info "defgroup needs a symbol as first argument" {})))
    {:node new-node}))

(def defglobal
  (global-keyframes
    ["You need to name the global group"
     "Every style group needs to be a vector"]))

(def defkeyframes
  (global-keyframes
    ["You need to name the keyframes group"
     "Every keyframe needs to be a vector"]))

