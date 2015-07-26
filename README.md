# colinkahn.flux.dispatcher

![Clojars Project](http://clojars.org/colinkahn.flux.dispatcher/latest-version.svg)

A Clojurescript wrapper around Facebook's
[Flux](https://facebook.github.io/flux/) dispatcher.

## Usage

```cljs
(def state (set-state! (atom {})))

(defhandler :comedy [action]
  "was-funny"
  {:funny true :who (:who action)})

(defhandler :club [action]
  ["was-funny" "was-mildly-amusing"]
  (do
    (wait-for :comedy)
    {:audience :laughs}))

(dispatch {:type "was-funny" :who "Jerry"})

@state ; => {:comedy {:funny true :who "Jerry"} :club {:audience :laughs}}
```

## License

Copyright © 2015 colinkahn

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
