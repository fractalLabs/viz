(ns viz.handler
  (:use viz.core)
;  (:use compojure.core)
  (:use compojure.core)
  (:use [hiccup core page element])
  (:require [compojure.handler :as handler]
            [compojure.route :as route]))


(defn smop []
   (str "
<html>
	<head>
		<title>SMOP Dashboard</title>
	</head>
	<body bgcolor=\"aaaaaa\">"graphael"
		<h1>
			SMOP</h1>
		<table align=\"center\" border=\"1\" cellpadding=\"1\" cellspacing=\"1\" style=\"width: 90%;\">
			<tbody>
				<tr>
					<td>"(line [1 430 23 5 4 200])"</td>
					<td>"(line [1 43 3 65 4 200])"</td>
					<td>"(line [1 43 23 65 4 20])"</td>
				</tr>
				<tr>
					<td></td>
					<td></td>
					<td></td>
				</tr>
			</tbody>
		</table></body>
</html>
"))

(defn smop2 []
  (html
   [:html
   [:head
    [:title "SMOP Dashboard"]]
   [:body {:bgcolor "aaaaaa"} graphael
    [:h1 "SMOP"]
    [:table {:align "center" :border 1 :cellpadding 1 :cellspacing 1 :style "width 90%"}
     [:tbody
      [:tr
       [:td (line [1 430 23 5 4 200])]
       [:td (line [1 43 3 65 4 200])]
       [:td (line [1 43 23 65 4 20])]]
      [:tr
       [:td]
       [:td]
       [:td]]]]]]))

(defroutes app-routes
  (GET "/" [] (smop))
  ;(POST "/" request (api (:body request)))
  (GET "/g" [] (html graphael (pie [1 2 3 4 5 6 7 8 9] ["a" "b" "c" "d" "e" "F" "gffff" "H" "i"]) (pie [1 2 3 4 5 6 7 8 9] ["a" "b" "c" "d" "e" "F" "gffff" "H" "i"])))
  (GET "/l" [] (line [1 43 23 65 4 200]))
  (GET "/b" [] (html graphael (bar [1 2 3 4] [5 6 7 8])))
  (route/resources "/")
  (route/not-found "Not Found"))

(def app
  (handler/site app-routes))
