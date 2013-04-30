(ns viz.pivot
  (:use [viz core])
  (:use cheshire.core)
  (:use [hiccup core page element]))

(def pivot-css (include-css "http://static.fractalmedia.mx/bootstrap.min.css" "http://static.fractalmedia.mx/subnav.css" "http://static.fractalmedia.mx/pivot.css"))
;en highcharts esto se llama charts, hay que llegar a convension.
(def pivot-libs (include-js jquery "http://static.fractalmedia.mx/subnav.js" "http://static.fractalmedia.mx/jquery.dataTables.min.js" "http://static.fractalmedia.mx/dataTables.bootstrap.js" "http://static.fractalmedia.mx/pivot.js" "http://static.fractalmedia.mx/jquery_pivot.js"))

(def test-json (generate-string [["last_name","first_name","zip_code","billed_amount","last_billed_date"]
		  		 ["Jackson", "Robert", 34471, 100.00, "Tue, 24 Jan 2012 00:00:00 +0000"]
				 ["Smith", "Jon", 34471, 173.20, "Mon, 13 Feb 2012 00:00:00 +0000"]]))

(def test-field-definitions (js-format [{"name" "last_name",   "type" "string",   "filterable" true},
        {"name" "first_name",        "type" "string",   "filterable" true},
        {"name" "zip_code",          "type" "integer",  "filterable" true, "summarizable" "count"},
        {"name" "pseudo_zip",        "type" "integer",  "filterable" true },
        {"name" "billed_amount",     "type" "float",    "rowLabelable" false},
        {"name" "last_billed_date",  "type" "date",     "filterable" true}]))

(defn pivot-init [json fields] (javascript-tag (str "pivot.init({json: " json ", fields: " fields "});")))

(def results-div "<div id=\"results\"></div>")
(def menu-div "<div id=\"pivot-menu-container\"></div>")

(defn pivot-display [json fields] (javascript-tag (str "$(document).ready(function() {
  $('#pivot-menu-container').pivot_display('setup', {json:" json" ,fields:" fields"})
})"
)))

(defn pivot-display-data-tables [input] (javascript-tag (str "$(document).ready(function() { setupPivot(" (js-format input) ");});")))

;hickory the fuck out of this
(def nav "<div class=\"subnav\">\n      <ul class=\"nav nav-pills\">\n        <li class=\"dropdown\">\n          <a class=\"dropdown-toggle\" data-toggle=\"dropdown\" href=\"#\">\n            Filter Fields\n            <b class=\"caret\"></b>\n          </a>\n          <ul class=\"dropdown-menu stop-propagation\" style=\"overflow:auto;max-height:450px;padding:10px;\">\n            <div id=\"filter-list\"></div>\n          </ul>\n        </li>\n        <li class=\"dropdown\">\n          <a class=\"dropdown-toggle\" data-toggle=\"dropdown\" href=\"#\">\n            Row Label Fields\n            <b class=\"caret\"></b>\n          </a>\n          <ul class=\"dropdown-menu stop-propagation\" style=\"overflow:auto;max-height:450px;padding:10px;\">\n            <div id=\"row-label-fields\"></div>\n          </ul>\n        </li>\n        <li class=\"dropdown\">\n          <a class=\"dropdown-toggle\" data-toggle=\"dropdown\" href=\"#\">\n            Column Label Fields\n            <b class=\"caret\"></b>\n          </a>\n          <ul class=\"dropdown-menu stop-propagation\" style=\"overflow:auto;max-height:450px;padding:10px;\">\n            <div id=\"column-label-fields\"></div>\n          </ul>\n        </li>\n        <li class=\"dropdown\">\n          <a class=\"dropdown-toggle\" data-toggle=\"dropdown\" href=\"#\">\n            Summary Fields\n            <b class=\"caret\"></b>\n          </a>\n          <ul class=\"dropdown-menu stop-propagation\" style=\"overflow:auto;max-height:450px;padding:10px;\">\n            <div id=\"summary-fields\"></div>\n          </ul>\n        </li>\n        <li class=\"dropdown pull-right\">\n          <a class=\"dropdown-toggle\" data-toggle=\"dropdown\" href=\"#\">\n            Canned Reports\n            <b class=\"caret\"></b>\n          </a>\n          <ul class=\"dropdown-menu\">\n           <li><a id=\"ar-aged-balance\" href=\"#\">AR Aged Balance</a></li>\n           <li><a id=\"acme-detail-report\" href=\"#\">Acme Corp Detail</a></li>\n           <li><a id=\"miami-invoice-detail\" href=\"#\">Miami Invoice Detail</a></li>\n          </ul>\n        </li>\n      </ul>\n    </div>\n")

(def setupPivot (javascript-tag "function setupPivot(input){\n    input.callbacks = {afterUpdateResults: function(){\n      $('#results > table').dataTable({\n        \"sDom\": \"<'row'<'span6'l><'span6'f>>t<'row'<'span6'i><'span6'p>>\",\n        \"iDisplayLength\": 50,\n        \"aLengthMenu\": [[25, 50, 100, -1], [25, 50, 100, \"All\"]],\n        \"oLanguage\": {\n          \"sLengthMenu\": \"_MENU_ records per page\"\n        }\n      });\n    }};\n    $('#pivot-demo').pivot_display('setup', input);\n  };\n"))

(def formato-chaca (str "{json: " test-json ", fields:" test-field-definitions"}"))
;parece que (pivot-init json fields) no se necesita alla abajo
(defn pivot [json fields] (html pivot-css "NTRDMS" nav results-div setupPivot (pivot-display-data-tables formato-chaca)))
