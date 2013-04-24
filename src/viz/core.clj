(ns viz.core
  (:use [hiccup core page element]))


(defn rand-id [] (int (rand 100000)))


(defn ify [p f] #(if (p %) (f %) %))
(def stringify (ify keyword? name))
(def quotify (ify string? #(str "'" % "'")))
(defn dirty? [o] (and (string? o) (not= (first o) \[) (not= (first o) \{) (not= "false" o) (not= "true" o)))
(def dirty-quotify (ify dirty?  #(str "'" % "'")))

(defn vec-js-format [v] (str "[" (apply str (interpose "," (map dirty-quotify v))) "]"))
(defn map-js-format [m] (str "{" (apply str (interpose "," (map #(str (stringify (first %)) ":" (dirty-quotify (second %))) m))) "}"))

(defn js-format [o]
  (if (vector? o) (vec-js-format (vec (map js-format o)))
    (if (map? o) (map-js-format (apply merge (map #(hash-map (first %) (js-format (second %))) o))) o)))
