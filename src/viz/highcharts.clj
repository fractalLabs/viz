(ns viz.highcharts
  (:use [viz core])
  (:use [hiccup core page element]))


(def highcharts (include-js "http://ajax.googleapis.com/ajax/libs/jquery/1.8.2/jquery.min.js" "http://code.highcharts.com/highcharts.js" "http://code.highcharts.com/modules/exporting.js" "http://code.highcharts.com/highcharts-more.js"))

(defn bar-js-demo [id] (str  
"$(function () { 
    $('#container" id"').highcharts({
        chart: {
            type: 'bar'
        },
	credits: {
		enabled: false
	},
        title: {
            text: 'Fruit Consumption'
        },
        xAxis: {
            categories: ['Apples', 'Bananas', 'Oranges']
        },
        yAxis: {
            title: {
                text: 'Fruit eaten'
            }
        },
        series: [{
            name: 'Jane',
            data: [1, 0, 4]
        }, {
            name: 'John',
            data: [5, 7, 3]
        }]
    });
});"))

(defn bar-demo [id] (html (str "<div id=\"container" id "\" style=\"width:100%; height:400px;\"></div>") (javascript-tag (bar-js-demo id))))


(defn bubble-js-demo [id] (str 
"$(function () {
  $('#container" id"').highcharts({
    chart: {
      type: 'bubble',
      zoomType: 'xy'
    },
    title: {
      text: 'Highcharts Bubbles'
    },
    series: [{
      name: 'a', data: [[97,36,79],[94,74,60],[68,76,58],[64,87,56],[68,27,73],[74,99,42],[7,93,87],[51,69,40],[38,23,33],[57,86,31]]}, {
      data: [[25,10,87],[2,75,59],[11,54,8],[86,55,93],[5,3,58],[90,63,44],[91,33,17],[97,3,56],[15,67,48],[54,25,81]] }, {
      data: [[47,47,21],[20,12,4],[6,76,91],[38,30,60],[57,98,64],[61,17,80],[83,60,13],[67,78,75],[64,12,10],[30,77,82]]
    }]
  });
});"))

(defn js-boiler [id s]
  (str "$(function () {
	  $('#container" id "').highcharts("
	  (js-format s)
	  ");});"
	  ))

(defn bubble-demo [id] (html (str "<div id=\"container" id"\" style=\"height: 400px; width: 600px; margin: 0 auto\"></div>") (javascript-tag (bubble-js-demo id))))

(def bbl {"chart" {"type" "bubble" "zoomType" "xy"}
	   "title" {"text" "Highcharts bubbles"}
	   "series"[{"data" [[1 2 3] [4 5 6] [7 8 9]]} {"data" [[10 11 12] [13 14 15] [16 17 18]]}]})

(defn bubble-demo [id] (html (str "<div id=\"container" id "\" style=\"height: 400px; width: 600px; margin: 0 auto\"></div>") (javascript-tag (js-boiler id bbl))))
