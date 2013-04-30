(ns viz.handler
  (:use [viz core highcharts])
  (:use compojure.core)
  (:use [hiccup core page element])
  (:require [compojure.handler :as handler]
            [compojure.route :as route]))


(def data-test [{:a 0 :b 3 :c 6 :d 7 :e 2} {:a 1 :b 2 :c 3 :d 4 :e 2} {:a 5 :b 6 :c 7 :d 8 :e 4} {:a 9 :b 10 :c 11 :d 12 :e 4}])

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
  (GET "/bubble" [] (html charts (chart (rand-id) bubbles-demo)))
  (route/resources "/")
  (route/not-found "Not Found"))

(def app
  (handler/site app-routes))
