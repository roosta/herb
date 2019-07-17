<p align="center">
  <img height="400" src="https://raw.githubusercontent.com/roosta/herb/master/assets/herb.svg?sanitize=true">
</p>

<p align="center">
  <a href="https://travis-ci.org/roosta/herb">
    <img src="https://travis-ci.org/roosta/herb.svg?branch=master">
  </a>
  <a href="https://clojars.org/herb">
    <img src="https://img.shields.io/clojars/v/herb.svg">
  </a>
  <a href="https://cljdoc.org/d/herb/herb/CURRENT">
    <img src="https://cljdoc.org/badge/herb/herb">
  </a>
</p>


Herb is a CSS styling library for
[Clojurescript](https://clojurescript.org/), built using
[Garden](https://github.com/noprompt/garden), whose main focus is on
component level styling using functions.

- [Documentation](https://cljdoc.org/d/herb/herb/CURRENT)
- [Tutorial](http://herb.roosta.sh/)
- [Changelog](https://github.com/roosta/herb/blob/master/CHANGELOG.md)

## Requirements
Herb requires at least Clojure 1.9.0 and ClojureScript 1.9.542 due to
use of
[clojure.spec.alpha](https://cljs.github.io/api/cljs.spec.alpha/) to
validate macro input.

## Quick start
[![Clojars Project](http://clojars.org/herb/latest-version.svg)](http://clojars.org/herb)

```clojure
(ns user
  (:require [herb.core :refer [<class]]))

(defn style []
  {:background "red"})

(defn component
  [:div {:class (<class style)}])
```

Herb has two main macros, `<class` and `<id`, these macros takes a
function that returns a [Garden](https://github.com/noprompt/garden)
style map, and returns a classname/id based on the functions fully
qualified name, in this case `user/style`.

The style is injected into the DOM when any one of Herb's macros are
called.


Pass arguments:

```clojure
(ns user
  (:require [herb.core :refer [<class]]))

(defn style
  [color]
  {:color color})

(defn component []
  [:div {:class (<class style "red")}])
```

Extend existing functions:

```clojure
(ns user
  (:require [herb.core :refer [<class]]))

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


[Garden](https://github.com/noprompt/garden) is used to translate the
style map to CSS, which enables most of Gardens functionality, so
familiarizing yourself with its features is a good idea.

Refer to the [tutorial](http://herb.roosta.sh/) for a full overview
and examples of Herbs features.

## Advanced optimizations
During a production build add this to the `:compiler` config
`:closure-defines {"goog.DEBUG" false}`. This flag tells Herb to
minify styles, remove unneeded style element attributes, and ensure
anonymous functions gets the correct minified name.

## Debugging advanced compilation

During advanced compilation Herb minify styles and removes the
`data-herb` attribute. If you need to debug production build it can be
helpful to see the function namespaces unmunged to get a clearer image
of what is happening.

To do this remove the `goog.DEBUG false` from production build and
enable `:pseudo-names`:

``` clojure
:cljsbuild {:builds {:min {:source-paths ["src" "env/prod"]
                           :compiler {:pseudo-names true
                                      :optimizations :advanced
                                      ...
                                      }}
```

That way you will see both full classnames and the namespace reflected
in the `data-herb` HTML attribute.


## Development
Start figwheel main with the development build

```shell
lein fig:build
```

Figwheel-main will automatically push cljs changes to the browser. Once Figwheel
starts up, you should be able to open <http://localhost:9500> for the
development server.


# Testing

Either run:

```shell
lein fig:test
```

For a headless test environment using chrome, make sure its
installed on your system.

You can also start the dev build and navigate to
[http://localhost:9500/figwheel-extra-main/auto-testing](http://localhost:9500/figwheel-extra-main/auto-testing)
to get a nice interface while coding that runs the tests on each save.

## License

Copyright © 2019 Daniel Berg

Distributed under the Eclipse Public License, the same as Clojure.
