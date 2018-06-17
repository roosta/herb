# Change Log
## [Unreleased]

## [v0.5.0] - 2018-06-17
### Added
- Clojure support
- `<style` macro that returns realized styles instead of a classname
- `defgroup` macro to wrap common pattern

### Changed
- Bussiness logic now in `cljc` to simplify testing, using lein test instead of a javascript runner
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

[Unreleased]: https://github.com/roosta/herb/compare/v0.5.0...HEAD
[v0.5.0]: https://github.com/roosta/herb/compare/v0.4.0...v0.5.0
[v0.4.0]: https://github.com/roosta/herb/compare/v0.3.5...v0.4.0
[v0.3.5]: https://github.com/roosta/herb/compare/v0.3.4...v0.3.5
[v0.3.4]: https://github.com/roosta/herb/compare/v0.3.3...v0.3.4
[v0.3.3]: https://github.com/roosta/herb/compare/v0.3.2...v0.3.3
[v0.3.2]: https://github.com/roosta/herb/compare/v0.3.1...v0.3.2
[v0.3.1]: https://github.com/roosta/herb/compare/v0.3.0...v0.3.1
[v0.3.0]: https://github.com/roosta/herb/compare/v0.2.0...v0.3.0
