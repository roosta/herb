# Change Log
## [v0.10.1-SNAPSHOT]
## [v0.10.0]
### Changed
- The `:key` and `:group` meta data is now completely removed,
  deciding instead to "automatically key" all functions based on it's
  function and argument signature. This results in a bit different
  class/id where a hash is appended to the selector based on its
  function and arguments, this won't break anything but it does make
  the API much simpler. This also has the added benefit of improving
  grouping performance significantly.

### Added
- Add `:hint` meta data. Can be used to extend classname in case you
  need some more data to attached to classname. The hint is only used
  during development builds, by checking the `goog.DEBUG` flag. If
  you'd like to see the hint during advanced builds set `goog.DEBUG`
  to true in production compiler config.

  ```clojure
  (defn style []
    ^{:hint "my-hint"}
    {:color "red"})
  ```
  ```css

  .ns_function_my-hint_1234567 {
    color: red;
  }
  ```

## [v0.9.0]
### Breaking change
- This library from here on out only supports `cljs`
  environment. Previously most of the library was written in `cljc` to
  allow for easier testing, which made it possible to use Herb in
  regular Clojure environments. The functionality was severly limited
  and honestly I don't see much use for it. If you'd like to do some
  server side CSS rendering a much better option is simply using
  [Garden](https://github.com/noprompt/garden) directly.

### Changed
- By using the [macro sugar
  trick](https://blog.fikesfarm.com/posts/2016-03-01-clojurescript-macro-sugar.html)
  users no longer have to require macros via the usual methods like
  `:require-macros`, just require in the macros like any other
  function from `herb.core`
  ```clojure
  (ns my.namespace
    (:require [herb.core :refer [<class defgroup]]))
  ```

## [v0.8.2] - 2019-07-10
### Fixed
- Fixed issue in `<style` macro where `throw` would get the wrong number of arguments and error out.

### Changed
- Markedly improve performance when using large stylegroups
- Upgrade dependencies

## [v0.8.1] - 2019-05-13
### Changed
- Upgrade garden dependency to version 1.3.9
- Re-enable minification/pretty-printing

## [v0.8.0] - 2019-03-30
### Changed
- Updated dependencies
### Fixed
- Allow passing at-rules (at-media, at-supports ...) to defglobal
### Added
- New meta type `:combinators`, allows for targeting using garden selector
  combinators like `>`, `+`, `-`, `descendant` from `garden.selectors` namespace.

```clojure
(defn selector-test []
  ^{:combinators {[:> :div :span] {:margin-left "10px"
                                   :background "red"}
                  [:+ :p] {:background "purple"
                           :margin 0
                           :margin-left "20px"}
                  [:- :div] {:background "yellow"}
                  [:descendant :div] {:background "green"}}}
  {:background :blue
   :color :white})
```
Result:

```css

.herbdemo_examples_selector-test {
  background: blue;
  color: white;
}

.herbdemo_examples_selector-test > div > span {
  margin-left: 10px;
  background: red;
}

.herbdemo_examples_selector-test + p {
  background: purple;
  margin: 0;
  margin-left: 20px;
}

.herbdemo_examples_selector-test ~ div {
  background: yellow;
}

.herbdemo_examples_selector-test div {
  background: green;
}
```

The syntax is a map with a vector of variable length as a key, starting with whatever combinator function you want to run as a keyword. Some combinators takes multiple elements as arguments. After that put whatever style map you'd like to be applied.


## [v0.7.2] - 2019-01-01
### Changed
- Remove clojure tools.analyzer dependency
- Temporarily disable pretty printing due to a
  [bug](https://github.com/noprompt/garden/issues/168) affecting certain media
  queries.
- Remove debux-stubs dependency

## [v0.7.1] - 2018-12-28
### Changed
- Split out site as a separate project.
- Move examples to demo/examples

## [v0.7.0] - 2018-12-27
### Breaking change
- `:auto-prefix` in component meta is removed, instead use either global config
  via herb.core/init! or pass `:prefix true` and `:vendors` for a local override.
- `join-classes` is renamed to `join`

### Added
- New global `herb.core/init!` function defined and currently takes only
  `:vendors` and `:auto-prefix` as possible options
- Finish first draft of the tutorial page

### Changed
- Improve error handling, and use `clojure.spec` for various input validation
- `:auto-prefix` now accepts strings as well as keywords
- Add passing multiple elements in `defglobal` for a single rule:
  ```clojure
  (defglobal [:body :html {:margin 0}])
  ```

### Fixed
- Change how the `data-herb` attr is parsed, fixing issue with namespace slashes not matching fully qualified name

## [v0.6.0] - 2018-10-13
### Breaking change
- `global-style!` runtime function has been replaced by `defglobal` macro

### Changed
- Upgrade dependencies
- The herb data string is no longer used in production builds
- Data-string now more resemble the fully qualified name of input function
- Change project structure from multiple separate projects to a single project
- Disable CSS pretty printing on production builds

### Added
- @supports queries via `:supports` metadata
- @keyframes support via `defkeyframes` macro and accompanying `<keyframes` macro
- vendor prefixes via `:vendors` and `:auto-prefix` metadata
- defglobal macro that when used appends garden style vectors to head as CSS

## [v0.5.0] - 2018-06-17
### Added
- Clojure support
- `<style` macro that returns realized styles instead of a classname
- `defgroup` macro to wrap common pattern

### Changed
- Business logic now in `cljc` to simplify testing, using lein test instead of a javascript runner
- Rename `set-global-style!` to `global-style!`
- Rename `:mode` to `:pseudo`

## [v0.4.0] - 2018-03-31
### Fixed
- Issue with passing a keyword as a style key causing a crash

### Added
- New meta data `:id` that returns an id instead of a classname
- Add `set-global-style!` helper fn that assists setting styles to global elements like <a>, <body> and so forth
- Add `join-classes` helper fn that takes multiple class names and joins them.
- Setup `:advanced` build, using munged function names in-place of fully qualified names

### Changed
- Introduce two new macros `<class` and `<id` and deprecate `with-style`
- Changed identifier and data string to use input functions `.-name` field. This
  allows for nested forms.
- Include NS in anonymous function identifiers and data string to make them
  easier to debug
- Args are no longer used as a data string identifier, key is used instead since
  it would more likley conform to a readable identifier.

## [v0.3.5] - 2018-02-23
### Changed
A change in the :extend metadata syntax:
```clojure
;; Passing single function
^{:extend some-style-fn}

;; Passing fn with args
^{:extend [some-style-fn some-arg]}

;; passing multiple functions
^{:extend [[style-fn1 "green" 42] [style-fn]]}
```

### Fixed
- Fixed issue where multiple levels of extended style fns would not work.

## [v0.3.4] - 2018-02-19
### Fixed
- Fixed issue where an extra dash got added to the returned classname

## [v0.3.3] - 2018-02-17
### Added
- Unit tests

### Changed
- Improve anonymous function classname hashing
- Passed keys now replace all non legal characters with underscore, making it
  more lenient.

## [v0.3.2] - 2018-02-15
### Changed
- Refactor large portions of the codebase

### Fixed
- Issue with :extend meta-data and multiple single functions with args

## [v0.3.1] - 2018-02-11
### Added
- Anonymous functions, with-style can now take named or unnamed functions.
  Anonymous functions gets the name `name.space/anonymous-(hash)`. Hash is
  calculated from the combined string of returned style map and meta.

## [v0.3.0] - 2018-02-11
### Breaking change
Macro NS has changed so, to require macro all requires of herb need to change
from `herb.macro` to `herb.core`

### Added
- Support media queries
### Changed
- Move macro ns to core.clj, move rest into core.cljs

### Fixed
- Fixed issues with inheritance precedence

[v0.10.1-SNAPSHOT]: https://github.com/roosta/herb/compare/v0.10.0...HEAD
[v0.10.0]: https://github.com/roosta/herb/compare/v0.9.0...v0.10.0
[v0.9.0]: https://github.com/roosta/herb/compare/v0.8.2...v0.9.0
[v0.8.2]: https://github.com/roosta/herb/compare/v0.8.1...v0.8.2
[v0.8.1]: https://github.com/roosta/herb/compare/v0.8.0...v0.8.1
[v0.8.0]: https://github.com/roosta/herb/compare/v0.7.2...v0.8.0
[v0.7.2]: https://github.com/roosta/herb/compare/v0.7.1...v0.7.2
[v0.7.1]: https://github.com/roosta/herb/compare/v0.7.0...v0.7.1
[v0.7.0]: https://github.com/roosta/herb/compare/v0.6.0...v0.7.0
[v0.6.0]: https://github.com/roosta/herb/compare/v0.5.0...v0.6.0
[v0.5.0]: https://github.com/roosta/herb/compare/v0.4.0...v0.5.0
[v0.4.0]: https://github.com/roosta/herb/compare/v0.3.5...v0.4.0
[v0.3.5]: https://github.com/roosta/herb/compare/v0.3.4...v0.3.5
[v0.3.4]: https://github.com/roosta/herb/compare/v0.3.3...v0.3.4
[v0.3.3]: https://github.com/roosta/herb/compare/v0.3.2...v0.3.3
[v0.3.2]: https://github.com/roosta/herb/compare/v0.3.1...v0.3.2
[v0.3.1]: https://github.com/roosta/herb/compare/v0.3.0...v0.3.1
[v0.3.0]: https://github.com/roosta/herb/compare/v0.2.0...v0.3.0
