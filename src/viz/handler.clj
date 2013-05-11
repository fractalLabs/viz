(ns viz.handler
  (:use [viz core highcharts pivot])
  (:use compojure.core)
  (:use [hiccup core page element])
  (:require [compojure.handler :as handler]
            [compojure.route :as route]))


;; Data de prueba para gen-chart
(def data-test [{:a 0 :b 3 :c 6 :d 7 :e 2} {:a 1 :b 2 :c 3 :d 4 :e 2} {:a 5 :b 6 :c 7 :d 8 :e 4} {:a 9 :b 10 :c 11 :d 12 :e 4}])

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
  (GET "/bars" [] (html charts [:p (str data-test)] (chart (gen-chart data-test
                                                         "bar" :title "una prueba"
                                                         :xtitle "a b y d" :xvals [:a :b :d] :ytitle "todas las c"
                                                         :name :e))
                         (chart (gen-chart data-test
                                                    "bubble" :title "a"
                                                    :xtitle "a b c" :xvals [:a :b :d] 
                                                    :name  :e))))
  (GET "/pivot" [] (pivot-with-definitions demo-definitions demo-data))
  (GET "/pvt" [] (pivot demo-data))
  (GET "/bubble" [] (html charts (chart (rand-id) bubbles-demo)))
  (route/resources "/")
  (route/not-found "Not Found"))

(def app
  (handler/site app-routes))
