#!/usr/bin/env bash
# https://github.com/borkdude/clj-kondo/blob/master/script/cljdoc-preview

rm -rf /tmp/cljdoc
mkdir -p /tmp/cljdoc
version=$(grep "defproject" < project.clj | cut -d'"' -f2)

echo "---- cljdoc preview: installing jar in local repo"
lein install

echo "---- cljdoc preview: ingesting herb"
docker run --rm -v "$PWD:/herb" \
       -v "$HOME/.m2:/root/.m2" -v /tmp/cljdoc:/app/data --entrypoint "clojure" \
       cljdoc/cljdoc -A:cli ingest -p herb/herb -v "$version" \
       --git /herb

echo "---- cljdoc preview: starting server on port 8000"
docker run --rm -p 8000:8000 -v /tmp/cljdoc:/app/data -v "$HOME/.m2:/root/.m2" cljdoc/cljdoc

# go directly to http://localhost:8000/d/herb/herb/VERSION, not via the search
