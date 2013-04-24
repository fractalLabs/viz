(ns viz.highcharts
  (:use [viz core])
  (:use [hiccup core page element]))


(def charts (include-js "http://ajax.googleapis.com/ajax/libs/jquery/1.8.2/jquery.min.js" "http://code.highcharts.com/highcharts.js" "http://code.highcharts.com/modules/exporting.js" "http://code.highcharts.com/highcharts-more.js"))

(defn js-boiler [id s]
  (str "$(function () {
	  $('#container" id "').highcharts("
	  (js-format s)
	  ");});"
	  ))

(defn map-defaults [m] (assoc m "credits" {"enabled" "false"}))

(defn chart [m] (let [id (rand-id)]
  (html (str "<div id=\"container" id "\" style=\"height: 300px; width: 450px; margin: 0 auto\"></div>") (javascript-tag (js-boiler id (map-defaults m))))))


; Maps con descripciones demo
(def barras-demo {
"chart" {"type" "bar"}
"title" {"text" "OLA K ASE"}
"xAxis" {"categories" ["Manzanas" "Platanos" "Naranjas"]}
"yAxis" {"title" {"text" "Frutas"}}
"series" [{"name" "Sammy" "data" [1 5 3]} {"name" "Miguel Luis" "data" [10 6 1]}]})

(def bubbles-demo {
"chart" {"type" "bubble" "zoomType" "xy"}
"title" {"text" "Highcharts bubbles"}
"series"[{"data" [[4 2 3] [4 5 6] [7 8 9]]} {"data" [[10 11 12] [13 14 15] [16 17 18]]}]})

