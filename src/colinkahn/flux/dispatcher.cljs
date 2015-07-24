(ns colinkahn.flux.dispatcher
  (:require [cljsjs.flux])
  (:require-macros [colinkahn.flux.dispatcher :refer [defhandler]]))

(def *state* nil)
(def tokens (atom {}))

(defprotocol IDispatch
  (-wait-for [this ids])
  (-dispatch [this payload])
  (-register [this callback]))

(extend-type js/Flux.Dispatcher
  IDispatch
  (-wait-for [this ids]
    (.waitFor this (clj->js ids)))
  (-dispatch [this payload] (.dispatch this payload))
  (-register [this callback] (.register this callback)))

(def dispatcher (js/Flux.Dispatcher.))

(defn dispatch [payload]
  (-dispatch dispatcher payload))

(defn wait-for [& nms]
  (-wait-for dispatcher (map @tokens nms)))

(defn register [callback]
  (-register dispatcher callback))

(defn store-token! [nm token]
  (swap! tokens (fn [p] (assoc p nm token))))

(defn set-state! [state]
  (set! *state* state))

(defn update-state! [nm m]
  (swap! *state* (fn [p n] (merge-with merge p (hash-map nm n))) m))
