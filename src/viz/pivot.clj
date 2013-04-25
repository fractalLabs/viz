(ns viz.pivot
  (:use [viz core])
  (:use cheshire.core)
  (:use [hiccup core page element]))


;en highcharts esto se llama charts, hay que llegar a convension.
(def pivot-libs (include-js jquery "http://static.fractalmedia.mx/pivot.js" "http://static.fractalmedia.mx/jquery_pivot.js"))

(def test-json (generate-string [["last_name","first_name","zip_code","billed_amount","last_billed_date"]
		  		 ["Jackson", "Robert", 34471, 100.00, "Tue, 24 Jan 2012 00:00:00 +0000"]
				 ["Smith", "Jon", 34471, 173.20, "Mon, 13 Feb 2012 00:00:00 +0000"]]))

(def test-field-definitions (js-format [{"name" "last_name",   "type" "string",   "filterable" true},
        {"name" "first_name",        "type" "string",   "filterable" true},
        {"name" "zip_code",          "type" "integer",  "filterable" true},
        {"name" "pseudo_zip",        "type" "integer",  "filterable" true },
        {"name" "billed_amount",     "type" "float",    "rowLabelable" false},
        {"name" "last_billed_date",  "type" "date",     "filterable" true}]))

(defn pivot [json fields] (javascript-tag (str "pivot.init({json: " json ", fields: " fields "});")))

