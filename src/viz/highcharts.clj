(ns viz.highcharts
  (:use [viz core])
  (:use [hiccup core page element]))


(def charts (include-js jquery "http://code.highcharts.com/highcharts.js" "http://code.highcharts.com/modules/exporting.js" "http://code.highcharts.com/highcharts-more.js"))

(defn js-boiler [id s]
  (str "$(function () {
	  $('#container" id "').highcharts("
	  (js-format s)
	  ");});"
	  ))

(defn map-defaults [m] (merge {"credits" {"enabled" "false"}, "title" {"text" ""}} m))

(defn chart [m] 
  (let [id (rand-id)] (html (str "<div id=\"container" id "\" style=\"height: 240px; width: 320px; margin: 0 auto\"></div>") (javascript-tag (js-boiler id (map-defaults m))))))

(defn bubble [m] (chart (merge {"chart" {"type" "bubble" "zoomType" "xy"}} m)))
(defn bar [m] (chart (merge {"chart" {"type" "bar"}} m)))
(defn makechart [m] (fn [mm] (chart (merge m mm))))
(def bar (makechart {"chart" {"type" "bar"}}))

;iba a ser con merge recur pero tiene que ser otra fn aware de [] y {} anidados
(defn dpie [m] (chart (merge 
  {"plotOptions" {"pie" {"allowPointSelect" "true", 
			"cursor" "pointer", 
			"dataLabels" {"enabled" "true", 
				      "formatter" "function() { return '<b>' + this.point.name + '</b>: ' + Math.round(this.point.y);} "}}}
  "series" {"type" "pie"}}
    m)))

(defn pie [v] (chart (merge 
  {"plotOptions" {"pie" {"allowPointSelect" "true", 
			"cursor" "pointer", 
			"dataLabels" {"enabled" "true", 
				      "formatter" "function() { return '<b>' + this.point.name + '</b>: ' + Math.round(this.point.y);} "}}}
  "series" {"type" "pie"}}
    {"series" [{"type" "pie" "data" v}]})))

(defn dirty-pie [v] (dpie {"series" [{"type" "pie" "data" v}]}))

(defn series
  "Genera el vector de vectores que el mapa series en la key data"
  [maps k categs chart]
  (if-not (= "bubble" chart)
    (vec
     (for [n maps]
       {"name" (str (n k)) "data" (vec (map #(n %) categs))}))
    (let [cjtos (group-by #(% k) maps)]
      (vec (for [i cjtos]
             {"name" (str (key i))
              "data" (vec (map (fn [elem]
                                 (vec (for [i categs] (elem i))))
                               (val i)))})))))

(defn gen-chart
  "maps - coleccion de mapas que usará para la gráfica
   chart - bar/pie/bubble etc
   :title - titulo de la gráfica
   :xtitle - la leyenda del eje x
   :xvals - los valores que tendrá el eje x (categories)
   :ytitle - la leyenda del eje y
   :name - la key que tomará c/valor para mostrar de maps"
  [maps chart & {:keys [title xtitle xvals ytitle name]}]
  (if (not= "pie" chart )
    {"chart" {"type" chart}
     "title" {"text" (or title "")}
     "xAxis" (merge {"title" {"text" (if xtitle xtitle "")}} (if-not (= "bubble" chart) {"categories" (vec (map str xvals))}))
     "yAxis" {"title" {"text" (or ytitle "")}}
     "series" (doall (series maps name xvals chart))}
    (dirty-pie
     (vec
      (map #(vec [(str (% name)) (% xvals)])  maps)))))
