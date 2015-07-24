(ns colinkahn.flux.dispatcher)

(defmacro defhandler
  [nm [action :as params] & clauses]
  (let [m (gensym 'm)
        clauses (conj clauses nil)]
    `(store-token! ~nm
       (register
        (fn ~params
          (when-let [~m (case (:type ~action) ~@clauses)]
            (update-state! ~nm ~m)))))))
