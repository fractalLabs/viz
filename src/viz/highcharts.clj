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

(defn map-defaults [m] (merge {"credits" {"enabled" "false"}, "title" {"text" ""}} m))

(defn chart [m] 
  (let [id (rand-id)] (html (str "<div id=\"container" id "\" style=\"height: 300px; width: 450px; margin: 0 auto\"></div>") (javascript-tag (js-boiler id (map-defaults m))))))

(defn bubble [m] (chart (merge {"chart" {"type" "bubble" "zoomType" "xy"}} m)))
(defn bar [m] (chart (merge {"chart" {"type" "bar"}} m)))
(defn makechart [m] (fn [mm] (chart (merge m mm))))
(def bar (makechart {"chart" {"type" "bar"}}))

;iba a ser con merge recur pero tiene que ser otra fn aware de [] y {} anidados
(defn pie [m] (chart (merge 
  {"plotOptions" {"pie" {"allowPointSelect" "true", 
			"cursor" "pointer", 
			"dataLabels" {"enabled" "true", 
				      "formatter" "function() { return '<b>' + this.point.name + '</b>: ' + Math.round(this.point.y);} "}}}
  "series" {"type" "pie"}}
    m)))


; Maps con descripciones demo
(def barras-demo {
"chart" {"type" "bar"}
"title" {"text" "OLA K ASE"}
"xAxis" {"categories" ["Manzanas" "Platanos" "Naranjas"]}
"yAxis" {"title" {"text" "Frutas"}}
"series" [{"name" "Sammy" "data" [1 5 3]} {"name" "Miguel Luis" "data" [10 6 1]}]})

(def bubbles-demo {
"chart" {"type" "bubble" "zoomType" "xy"}
"series"[{"data" [[4 2 3] [4 5 6] [7 8 9]]} {"data" [[10 11 12] [13 14 15] [16 17 18]]}]})

;en construccion
(def pie-demo [["a" 12] ["b" 24] ["c" 8]])

(defn dirty-pie [v] (pie {"series" [{"type" "pie" "data" v}]}))
