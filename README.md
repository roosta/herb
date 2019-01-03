# Herb
[![Build Status](https://travis-ci.org/roosta/herb.svg?branch=master)](https://travis-ci.org/roosta/herb)
[![Clojars Project](https://img.shields.io/clojars/v/herb.svg)](https://clojars.org/herb)

Herb is a CSS styling library for [Clojurescript](https://clojurescript.org/), built using [Garden](https://github.com/noprompt/garden), whose main focus is on component level styling using functions.

- [API](https://roosta.github.io/herb/)
- [Tutorial](http://herb.roosta.sh/)
- [Changelog](https://github.com/roosta/herb/blob/master/CHANGELOG.md)

## Requirements
Herb requires at least Clojure 1.9.0 and ClojureScript 1.9.542 due to use of [clojure.spec.alpha](https://cljs.github.io/api/cljs.spec.alpha/) to validate macro input.

## Quick start
[![Clojars Project](http://clojars.org/herb/latest-version.svg)](http://clojars.org/herb)

```clojure
(ns user
  (:require [herb.core :refer-macros [<class]]))

(defn style []
  {:background "red"})

(defn component
  [:div {:class (<class style)}])
```

Herb has two main macros, `<class` and `<id`, these macros takes a function that returns a [Garden](https://github.com/noprompt/garden) style map, and returns a classname/id based on the functions fully qualified name, in this case `user/style`.

The style is injected into the DOM when any one of Herb's macros are called.


Pass arguments:

```clojure
(ns user
  (:require [herb.core :include-macros true :as herb]))

(defn style
  [color]
  {:color color})

(defn component []
  [:div {:class (herb/<class style "red")}])
```

Extend existing functions:

```clojure
(ns user
  (:require [herb.core :include-macros true :refer [<class]]))

(defn button-style [text-color]
  {:display "inline-block"
   :color text-color
   :text-transform "uppercase"
   :cursor "pointer"
   :padding (px 12)})

(defn red-button-style []
  ^{:extend [button "white"]}
  {:background-color "red"})

(defn button []
  [:button {:class (<class red-button-style)}])
```


[Garden](https://github.com/noprompt/garden) is used to translate the style map to CSS, which enables most of Gardens functionality, so familiarizing yourself with its features is a good idea.

Refer to the [tutorial](http://herb.roosta.sh/) for a full overview and examples of Herbs features.

## Advanced optimizations
During a production build add this to the `:compiler` config  `:closure-defines {"goog.DEBUG" false}`. This flag tells Herb to minify styles, remove unneeded style element attributes, and ensure anonymous functions gets the correct minified name.

NOTE: Currently minifying/pretty-printing is temporarily disabled due to a [bug](https://github.com/noprompt/garden/issues/168) affecting certain media queries.

## Development
To start the development server [Leiningen](https://leiningen.org/) is required:
```shell
lein figwheel
```

To use in Emacs using [Cider](https://github.com/clojure-emacs/cider) `m-x cider-jack-in-clojurescript` and pick figwheel to start a ClojureScript repl environment and a server. See [.dir-locals.el](https://github.com/roosta/herb/blob/master/.dir-locals.el).

Point your browser to `localhost:3449`

## Testing
To run tests:
```shell
lein test
```

## License

Copyright © 2018 Daniel Berg

Distributed under the Eclipse Public License, the same as Clojure.
