(ns viz.handler
  (:use [viz core highcharts pivot])
  (:use compojure.core)
  (:use [hiccup core page element])
  (:require [compojure.handler :as handler]
            [compojure.route :as route]))


;; Data de prueba para charts
(def data-test [{:a 0 :b 3 :c 6 :d 7 :e 2} {:a 1 :b 2 :c 3 :d 4 :e 2} {:a 5 :b 6 :c 7 :d 8 :e 4} {:a 9 :b 10 :c 11 :d 12 :e 4}])

(def barras-demo
  {"chart" {"type" "bar"}
   "title" {"text" "OLA K ASE"}
   "xAxis" {"categories" ["Manzanas" "Platanos" "Naranjas"]}
   "yAxis" {"title" {"text" "Frutas"}}
   "series" [{"name" "Sammy" "data" [1 5 3]} {"name" "Miguel Luis" "data" [10 6 1]}]})

(def bubbles-demo {
                   "xAxis" {"title" {"text" "la equis"}}
"chart" {"type" "bubble" "zoomType" "xy"}
"series"[{"name" "cjto 1" "data" [[4 2 3] [4 5 6] [7 8 9]]} {"name" "cjto 2" "data" [[10 11 12] [13 14 15] [16 17 18]]}]})

(def pie-demo [["a" 12] ["b" 24] ["c" 8]])

;; Data de prueba para pivot
(def demo-definitions [{"name" "last_name",   "type" "string",   "filterable" true},
        {"name" "first_name",        "type" "string",   "filterable" true},
        {"name" "zip_code",          "type" "integer",  "filterable" true, "summarizable" "count"},
        {"name" "pseudo_zip",        "type" "integer",  "filterable" true },
        {"name" "billed_amount",     "type" "string",    "rowLabelable" false},
        {"name" "last_billed_date",  "type" "date",     "filterable" true}])

(def demo-data [["last_name","first_name","zip_code","billed_amount","last_billed_date"]
                                 ["Jackson", "Robert", 34471, false, "Tue, 24 Jan 2012 00:00:00 +0000"]
                                 ["Smith", nil, nil, true, "Mon, 13 Feb 2012 00:00:00 +0000"]])


(defroutes app-routes
  (GET "/" [] (html charts (chart barras-demo) (chart bubbles-demo) (dirty-pie pie-demo)))
  (GET "/bars" [] (html charts [:p (str data-test)]
                        (chart (gen-chart data-test
                                          "bar" :title "una prueba"
                                          :xtitle "a b y d" :xvals [:a :b :d] :ytitle "todas las c"
                                          :name :e))
                         (chart (gen-chart data-test
                                                    "bubble" :title "a"
                                                    :xtitle "a b c" :xvals [:a :b :d] 
                                                    :name  :e))
                         (gen-chart data-test "pie" :title "p" :xvals :e :name :a)))
  (GET "/pivot" [] (html pivot-css pivot-js [:body {} (pivot demo-data)]))
  (GET "/bubble" [] (html charts (chart (rand-id) bubbles-demo)))
  (route/resources "/")
  (route/not-found "Not Found"))

(def app
  (handler/site app-routes))
