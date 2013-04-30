(ns viz.core
  (:use [hiccup core page element]))


(defn rand-id [] (int (rand 100000)))


(defn ify [p f] #(if (p %) (f %) %))
(def stringify (ify keyword? name))
(def quotify (ify string? #(str "'" % "'")))
(defn dirty? [o] (and (string? o) (not= (first o) \[) (not= (first o) \{) (not= "false" o) (not= "true" o) (not (re-find #"function" o))))
(def dirty-quotify (ify dirty?  #(str "'" % "'")))

(defn vec-js-format [v] (str "[" (reduce str (interpose "," (map dirty-quotify v))) "]"))
(defn map-js-format [m] (str "{" (apply str (interpose "," (map #(str (stringify (first %)) ":" (dirty-quotify (second %))) m))) "}"))

(defn js-format [o]
  (if (vector? o) (vec-js-format (vec (map js-format o)))
    (if (map? o) (map-js-format (apply merge (map #(hash-map (first %) (js-format (second %))) o))) o)))

; De clojure contrib
(defn deep-merge-with
  "Like merge-with, but merges maps recursively, applying the given fn
  only when there's a non-map at a particular level.

  (deepmerge + {:a {:b {:c 1 :d {:x 1 :y 2}} :e 3} :f 4}
               {:a {:b {:c 2 :d {:z 9} :z 3} :e 100}})
  -> {:a {:b {:z 3, :c 3, :d {:z 9, :x 1, :y 2}}, :e 103}, :f 4}"
  [f & maps]
  (apply
    (fn m [& maps]
      (if (every? map? maps)
        (apply merge-with m maps)
        (apply f maps)))
    maps))

(defn last-arg [& args] (last args))

(defn merge-recur [last-arg & args] (apply deep-merge-with last-arg args))
