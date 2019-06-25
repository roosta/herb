# Herb Tutorial Site

This is the [Herb](https://github.com/roosta/herb) tutorial site project.

## Development mode

To start the Figwheel compiler, navigate to the project folder and run
the following command in the terminal:

```
lein figwheel
```

Figwheel will automatically push cljs changes to the browser. The
server will be available at
[http://localhost:3449](http://localhost:3449) once Figwheel starts
up.

Figwheel also starts `nREPL` using the value of the `:nrepl-port` in
the `:figwheel` config found in `project.clj`. By default the port is
set to `7002`.

The figwheel server can have unexpected behaviors in some situations
such as when using websockets. In this case it's recommended to run a
standalone instance of a web server as follows:

```
lein do clean, run
```

The application will now be available at [http://localhost:3000](http://localhost:3000).


### Optional development tools

Start the browser REPL:

```
$ lein repl
```
The Jetty server can be started by running:

```clojure
(start-server)
```
and stopped by running:
```clojure
(stop-server)
```


## Building for release

```
lein do clean, uberjar
```

## Deploying to dokku

Make sure you have [Git](http://git-scm.com/downloads) and [Heroku
toolbelt](https://toolbelt.heroku.com/) installed, then simply follow
the steps below.

Optionally, test that your application runs locally with running.

```
heroku local
```

create your app on dokku. This step has to be run on the server, or
you could use [dokku toolbelt](https://github.com/digitalsadhu/dokku-toolbelt) 
but that is currently unmaintained.

```
dokku create
```

Make sure to navigate to project root and run:

```
git subtree push --prefix site dokku master
```

Alternatively there is a script in `./script/deploy.sh` that does the
same as the example above.

Your application should now be deployed to Dokku!  For further
instructions see the [official
documentation](http://dokku.viewdocs.io/dokku/).
