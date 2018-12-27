# Herb
[![Build Status](https://travis-ci.org/roosta/herb.svg?branch=master)](https://travis-ci.org/roosta/herb)

Herb is a CSS styling library for [Clojurescript](https://clojurescript.org/), built using [Garden](https://github.com/noprompt/garden), whose main focus is on component level styling using functions.

- API
- Tutorial

## Requirements
Herb requires at least Clojure 1.9.0 and ClojureScript 1.10.x due to use of [clojure.spec.alpha](https://cljs.github.io/api/cljs.spec.alpha/) to validate macro input.

## Usage
Add TODO

```clojure
(ns user
  (:require [herb.core :refer-macros [<class]]))

(defn style []
  {:background "red"})

(defn component
  [:div {:class (<class style)}])
```

Herb has two main macros, `<class` and `<id`, these macros takes a function that returns a [Garden](https://github.com/noprompt/garden) style map, and returns a classname/id based on the functions fully qualified name: In this case `user/style`.

[Garden](https://github.com/noprompt/garden) is used to translate the style map to CSS, which enables most of Gardens functionality, so familiarizing yourself with its features is a good idea.

Refer to the tutorial for a full overview and examples of Herbs features.

## Development
Development is done using the demo/tutorial page as a testbed. To start the development server [Leiningen](https://leiningen.org/) is required:
```shell
lein with-profile +demo figwheel
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
