(ns viz.highcharts
  (:use [hiccup core page element]))


(def highcharts (include-js "http://ajax.googleapis.com/ajax/libs/jquery/1.8.2/jquery.min.js" "http://code.highcharts.com/highcharts.js"))

(defn b-js-demo [id] (str  
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

(defn b-demo [] (let [id (int (rand 10000))] (html (str "<div id=\"container" id "\" style=\"width:100%; height:400px;\"></div>") (javascript-tag (b-js-demo id))))) 
