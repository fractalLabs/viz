(ns viz.core
  (:use [hiccup core page element]))


(defn rand-id [] (int (rand 100000)))

(defn with-rand-id [body]
  (let [id (rand-id)] (body)))
