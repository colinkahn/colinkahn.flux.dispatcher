(ns colinkahn.flux.dispatcher)

(defn expand-clauses [clauses]
  (loop [v []
         cs (partition 2 clauses)]
    (if-let [[t r] (first cs)]
      (recur (concat v (if (vector? t)
                         (reduce (fn [m x] (conj m x r)) [] t)
                         [t r]))
             (rest cs))
      v)))

(defn clauses-bindings [clauses]
  (let [bmap (reduce (fn [m [t r]] (assoc m r (gensym 'x))) {}
                         (filter (comp vector? first) (partition 2 clauses)))
        bvec (reduce (fn [l [t s]] (conj l s (list 'fn ['action] t))) [] bmap)
        clauses (map (fn [x] (if (contains?  bmap x) (list (get bmap x) 'action) x)) clauses)]
    [bvec (conj (into [] (expand-clauses clauses)) nil)]))

(defmacro defhandler
  [nm [action :as params] & clauses]
  (let [m (gensym 'm)
        [bvec clauses] (clauses-bindings clauses)]
    `(let ~bvec
       (store-token! ~nm
         (register
          (fn ~params
            (when-let [~m (case (:type ~action) ~@clauses)]
              (update-state! ~nm ~m))))))))
