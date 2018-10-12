(ns herb.runtime
  (:require #?@(:clj [[debux.core :refer [dbg]]]
                :cljs [[goog.dom :as dom]
                       [debux.cs.core :as d :refer-macros [clog clogn dbg dbgn break]]
                       [goog.object :as gobj]])
            [garden.core :refer [css]]
            [clojure.string :as str]))

#?(:cljs (def dev? ^boolean js/goog.DEBUG)
   :clj (def dev? true))

(defonce injected-styles (atom {}))
(defonce injected-keyframes (atom {}))
(defonce injected-global (atom {}))

(defn update-style!
  "Create css string and update DOM"
  [identifier #?(:cljs element) new]
  (let [vendors (-> new :data last val :vendors)
        auto-prefix (-> new :data last val :auto-prefix)
        css-str (css {:vendors vendors
                      :auto-prefix auto-prefix
                      :pretty-print? dev?}
                     (map (fn [[class {:keys [style pseudo media supports]}]]
                            [class style pseudo media supports])
                          (:data new)))]
    #?(:cljs (set! (.-innerHTML element) css-str))
    (swap! injected-styles assoc identifier (assoc new :css css-str))))

#?(:cljs
   (defn create-element!
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

(defn create-style
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
      (let [css-str (css {:pretty-print? dev?} obj)]
        #?(:cljs
           (let [element (or (.querySelector js/document (str "style[data-herb=\"" (name dispatch) "\"]"))
                             (create-element! (name dispatch)))
                 inner-html (.-innerHTML element)]
             (set! (.-innerHTML element) (str inner-html (when dev? "\n") css-str))))
        (swap! state assoc sym {:data obj :css css-str})))))
