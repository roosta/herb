# Change Log
All notable changes to this project will be documented in this file. This change
log follows the conventions of [keepachangelog.com](http://keepachangelog.com/).

## [Unreleased]

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

[Unreleased]: https://github.com/roosta/herb/compare/v0.3.1...HEAD
[v0.3.1]: https://github.com/roosta/herb/compare/v0.3.0...v0.3.1
[v0.3.0]: https://github.com/roosta/herb/compare/v0.2.0...v0.3.0
