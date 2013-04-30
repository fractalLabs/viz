(ns viz.pivot
  (:use [viz core])
  (:use cheshire.core)
  (:use [hiccup core page element]))

(def pivot-csslol (include-css "http://static.fractalmedia.mx/bootstrap.min.css" "http://static.fractalmedia.mx/subnav.css" "http://static.fractalmedia.mx/pivot.css"))
;en highcharts esto se llama charts, hay que llegar a convension.
(def pivot-js (include-js jquery "http://static.fractalmedia.mx/subnav.js" "http://rjackson.github.io/pivot.js/lib/javascripts/accounting.min.js" "http://static.fractalmedia.mx/jquery.dataTables.min.js" "http://static.fractalmedia.mx/dataTables.bootstrap.js" "http://static.fractalmedia.mx/pivot.js" "http://static.fractalmedia.mx/jquery_pivot.js"))

(defn js-code [mapa-str] (str  "function ageBucket(row, field){
  var age = Math.abs(((new Date().getTime()) - row[field.dataSource])/1000/60/60/24);
  switch (true){
    case (age < 31):
      return '000 - 030'
    case (age < 61):
      return '031 - 060'
    case (age < 91):
      return '061 - 090'
    case (age < 121):
      return '091 - 120'
    default:
      return '121+'}};

// {url:'http://rjackson.github.io/pivot.js/./lib/csv/demo.csv', fields: fields, filters: {employer: 'Acme Corp'}, rowLabels:[\"city\"], summaries:[\"billed_amount\", \"payment_amount\"]}

  function setupPivot(input){
    input.callbacks = {afterUpdateResults: function(){
      $('#results > table').dataTable({
        \"sDom\": \"<'row'<'span6'l><'span6'f>>t<'row'<'span6'i><'span6'p>>\",
        \"iDisplayLength\": 50,
        \"aLengthMenu\": [[25, 50, 100, -1], [25, 50, 100, \"All\"]],
        \"sPaginationType\": \"bootstrap\",
        \"oLanguage\": {
          \"sLengthMenu\": \"_MENU_ records per page\"
        }});}};
    $('#pivot-demo').pivot_display('setup', input);};
  $(document).ready(function() {
    setupPivot(" mapa-str ")
    // prevent dropdown from closing after selection
    $('.stop-propagation').click(function(event){
      event.stopPropagation();});
    // **Sexy** In your console type pivot.config() to view your current internal structure (the full initialize object).  Pass it to setup and you have a canned report.
    $('#ar-aged-balance').click(function(event){
      $('#pivot-demo').pivot_display('reprocess_display', {rowLabels:[\"employer\"], columnLabels:[\"age_bucket\"], summaries:[\"balance\"]})});
    $('#acme-detail-report').click(function(event){
      $('#pivot-demo').pivot_display('reprocess_display', {filters:{\"employer\":\"Acme Corp\"},rowLabels:[\"city\",\"last_name\",\"first_name\",\"state\",\"invoice_date\"]})});
    $('#miami-invoice-detail').click(function(event){
      $('#pivot-demo').pivot_display('reprocess_display', {\"filters\":{\"city\":\"Miami\"},\"rowLabels\":[\"last_name\",\"first_name\",\"employer\",\"invoice_date\"],\"summaries\":[\"payment_amount\"]})
    });});"))

(defn html-hickoried [mapa-str]
   [:html {:xmlns "http://www.w3.org/1999/xhtml", :xml:lang "en", :lang "en-us"}
    [:body {}
     [:div {:class "container"}
      [:div {:class "subnav"}
       [:ul {:class "nav nav-pills"}
        [:li {:class "dropdown"}
         [:a {:class "dropdown-toggle", :data-toggle "dropdown", :href "#"} "Filter Fields" [:b {:class "caret"}]]
         [:ul {:class "dropdown-menu stop-propagation", :style "overflow:auto;max-height:450px;padding:10px;"}
          [:div {:id "filter-list"}]]]
        [:li {:class "dropdown"}
         [:a {:class "dropdown-toggle", :data-toggle "dropdown", :href "#"}
          "Row Label Fields"
          [:b {:class "caret"}]]
         [:ul {:class "dropdown-menu stop-propagation", :style "overflow:auto;max-height:450px;padding:10px;"}
          [:div {:id "row-label-fields"}]]]
        [:li {:class "dropdown"}
         [:a {:class "dropdown-toggle", :data-toggle "dropdown", :href "#"}
          "Column Label Fields"
          [:b {:class "caret"}]]
         [:ul {:class "dropdown-menu stop-propagation", :style "overflow:auto;max-height:450px;padding:10px;"}
          [:div {:id "column-label-fields"}]]]
        [:li {:class "dropdown"}
         [:a {:class "dropdown-toggle", :data-toggle "dropdown", :href "#"}
          "Summary Fields"
          [:b {:class "caret"}]]
         [:ul {:class "dropdown-menu stop-propagation", :style "overflow:auto;max-height:450px;padding:10px;"}
          [:div {:id "summary-fields"}]]]
        [:li {:class "dropdown pull-right"}
         [:a {:class "dropdown-toggle", :data-toggle "dropdown", :href "#"}
          "Canned Reports"
          [:b {:class "caret"}]]
         [:ul {:class "dropdown-menu"}
          [:li {} [:a {:id "ar-aged-balance", :href "#"} "AR Aged Balance"]]
          [:li {} [:a {:id "miami-invoice-detail", :href "#"} "Miami Invoice Detail"]]]]]]
      [:hr {}]
      [:h1 {} "Results"]
      [:span {:id "pivot-detail"}]
      [:hr {}]
      [:div {:id "results"}]]
     [:script {:type "text/javascript"}
      (js-code mapa-str)]]])


(defn pivot-generator [mapa-str] (html pivot-csslol pivot-js (html-hickoried mapa-str)))

;fields es un vector de mapas con llaves "name" "type" "filtrable"
;data llega como un vector de vectores donde el primer vector son los
;nombres de las columnas.
(defn crea-mapa-str [fields data] (str "{json: " (generate-string data) ", fields:" (js-format fields) "}"))

(defn pivot [fields data] (pivot-generator (crea-mapa-str fields data)))

;TODO fn(s) que generen la data (la primera fila de la data sean los
;nombres correspondientes en la tabla. type distinga entre numeros y strings.

(def demo-definitions [{"name" "last_name",   "type" "string",   "filterable" true},
        {"name" "first_name",        "type" "string",   "filterable" true},
        {"name" "zip_code",          "type" "integer",  "filterable" true, "summarizable" "count"},
        {"name" "pseudo_zip",        "type" "integer",  "filterable" true },
        {"name" "billed_amount",     "type" "float",    "rowLabelable" false},
        {"name" "last_billed_date",  "type" "date",     "filterable" true}])

(def demo-data [["last_name","first_name","zip_code","billed_amount","last_billed_date"]
                                 ["Jackson", "Robert", 34471, 100.00, "Tue, 24 Jan 2012 00:00:00 +0000"]
                                 ["Smith", "Jon", 34471, 173.20, "Mon, 13 Feb 2012 00:00:00 +0000"]])
