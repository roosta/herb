(ns herb.runtime
  (:require #?@(:cljs [[goog.dom :as dom]
                       [goog.object :as gobj]])
            [garden.core :refer [css]]
            [garden.selectors :as s]
            [clojure.set :as set]
            [clojure.string :as str]))

#?(:cljs (def dev? ^boolean js/goog.DEBUG)
   :clj (def dev? true))

(defonce
  ^{:private true
    :doc "Atom containing all styles added to DOM. Takes the form of a map with
  classnames as keys. The map entry contains a `:data` which is Herb's
  representation of a style unit, `:data-string` which is what is used as the
  style data attribute in DOM, and `:css` which contains the rendered CSS
  string."}
  injected-styles (atom {}))

(defonce
  ^{:private true
    :doc "Atom containing all keyframe CSS added to DOM. Takes the form of a map
  with a namespace as a key. A map entry contains the keys `:data` which is herb's
  representation of a keyframe unit and `:css` which is the rendered CSS
  string"}
  injected-keyframes (atom {}))

(defonce
  ^{:private true
    :doc "Atom containing all global style added to DOM. Takes the form of a map
  with namespace as keys. A map entry contains `:data` which is a collection of
  global styles for a given via defglobal call and `:css` that contains the
  rendered CSS"}
  injected-global (atom {}))

(defonce
  ^{:doc "Atom containing a map with options passed from `herb.core/init!`.
  Entry includes `:vendors` and `:auto-prefix`"}
  options (atom {}))

(defn- update-style!
  "Create CSS string and update DOM"
  [identifier #?(:cljs element) new]
  (let [style (mapcat (fn [[classname {:keys [style pseudo media supports prefix vendors selectors]}]]
                        [[classname (with-meta style {:prefix prefix :vendors vendors})
                          pseudo media supports]
                         [(map (fn [[[combinator & elements] style]]
                                  (case combinator
                                    :> [(apply s/> classname elements) style]
                                    :+ [(apply s/+ classname elements) style]
                                    :- [(apply s/- classname elements) style]
                                    :descendant [(apply s/descendant classname elements) style]))
                           selectors)]])
                      (:data new))
        css-str (css {:vendors (seq (:vendors @options))
                      :auto-prefix (seq (:auto-prefix @options))}
                     style)]
    #?(:cljs (set! (.-innerHTML element) css-str))
    (swap! injected-styles assoc identifier (assoc new :css css-str))))

#?(:cljs
   (defn- create-element!
     "Create an element in the DOM with an optional data-herb attribute"
     [attr]
     (let [head (.-head js/document)]
       (assert (some? head) "An head element is required in the dom to inject the style.")
       (let [element (.createElement js/document "style")]
         (.setAttribute element "type" "text/css")
         (when attr
           (.setAttribute element "data-herb" attr))
         (.appendChild head element)
         element))))

(defn- create-style
  "Create a style element in head if identifier is not already present Attach a
  data attr with namespace and call update-style with new element"
  [identifier new data-str]
  (let [data (conj {} new)]
    #?(:cljs
       (let [element (create-element! data-str)]
         (update-style! identifier element (cond-> {:data data :element element}
                                             data-str (assoc :data-string data-str))))
       :clj (update-style! identifier {:data data :data-string data-str}))))

(defn inject-style!
  "Main interface to runtime. Takes an identifier, new garden style data structure
  and a fully qualified name. Check if identifier exist in DOM already, and if it
  does, compare `new` with `current` to make sure garden is not called to create
  the same style string again"
  [identifier new data-str]
  (if-let [injected (get @injected-styles identifier)]
    (let [data (:data injected)
          target (get data (first new))]
      (if (not= target (last new))
        (let [data (assoc injected :data (conj data new))]
          #?(:cljs (update-style! identifier (:element injected) data)
             :clj  (update-style! identifier data)))
        @injected-styles))
    (create-style identifier new data-str)))

(defn inject-obj!
  "Inject collection of style objects in a common element, used by passing a
  dispatch in the form of :keyframes or :global"
  [sym dispatch & obj]
  (let [state (case dispatch
                :global injected-global
                :keyframes injected-keyframes)]
    (when-not (= (:data (get @state sym)) obj)
      (let [css-str (css #_{:pretty-print? dev?} obj)]
        #?(:cljs
           (let [element (or (.querySelector js/document (str "style[data-herb=\"" (name dispatch) "\"]"))
                             (create-element! (name dispatch)))
                 inner-html (.-innerHTML element)]
             (set! (.-innerHTML element) (str inner-html (when dev? "\n") css-str))))
        (swap! state assoc sym {:data obj :css css-str})))))
