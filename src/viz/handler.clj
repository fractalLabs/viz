(ns viz.handler
  (:use [viz core highcharts])
  (:use compojure.core)
  (:use [hiccup core page element])
  (:require [compojure.handler :as handler]
            [compojure.route :as route]))



(defroutes app-routes
  (GET "/" [] (html charts (chart barras-demo) (chart bubbles-demo) (dirty-pie pie-demo)))
  (GET "/bubble" [] (html charts (chart (rand-id) bubbles-demo)))
  (route/resources "/")
  (route/not-found "Not Found"))

(def app
  (handler/site app-routes))
