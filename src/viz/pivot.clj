(ns viz.pivot
  (:use [viz core])
  (:use cheshire.core)
  (:use [hiccup core page element]))


(def pivot-css (include-css "http://static.fractalmedia.mx/bootstrap.min.css"
                            "http://static.fractalmedia.mx/subnav.css"
                            "http://static.fractalmedia.mx/pivot.css"))

(def pivot-js (include-js jquery
                          "http://static.fractalmedia.mx/subnav.js"
                          "http://rjackson.github.io/pivot.js/lib/javascripts/accounting.min.js"
                          "http://static.fractalmedia.mx/jquery.dataTables.min.js"
                          "http://static.fractalmedia.mx/dataTables.bootstrap.js"
                          "http://static.fractalmedia.mx/pivot.js"
                          "http://static.fractalmedia.mx/jquery_pivot.js"))

(def pivots (html pivot-css pivot-js))

(defn js-code [mapa-str] (str
 "function setupPivot(input){
    input.callbacks = {afterUpdateResults: function(){
      $('#results > table').dataTable({
        \"sDom\": \"<'row'<'span6'l><'span6'f>>t<'row'<'span6'i><'span6'p>>\",
        \"iDisplayLength\": 50,
        \"aLengthMenu\": [[25, 50, 100, -1], [25, 50, 100, \"All\"]],
        \"sPaginationType\": \"bootstrap\",
        \"oLanguage\": {
          \"sLengthMenu\": \"_MENU_ por página\"
        }});}};
    $('#pivot-demo').pivot_display('setup', input);};
  $(document).ready(function() {
    setupPivot(" mapa-str ")
    // prevent dropdown from closing after selection
    $('.stop-propagation').click(function(event){
      event.stopPropagation();});});"))

(def subnav
   [:div {:class "subnav"}
    [:ul {:class "nav nav-pills"}
     [:li {:class "dropdown"}
      [:a {:class "dropdown-toggle", :data-toggle "dropdown", :href "#"} "Filtrar" [:b {:class "caret"}]]
      [:ul {:class "dropdown-menu stop-propagation", :style "overflow:auto;max-height:450px;padding:10px;"}
       [:div {:id "filter-list"}]]]
     [:li {:class "dropdown"}
      [:a {:class "dropdown-toggle", :data-toggle "dropdown", :href "#"}
       "Campos Seleccionados"
       [:b {:class "caret"}]]
      [:ul {:class "dropdown-menu stop-propagation", :style "overflow:auto;max-height:450px;padding:10px;"}
       [:div {:id "row-label-fields"}]]]
     [:li {:class "dropdown"}
      [:a {:class "dropdown-toggle", :data-toggle "dropdown", :href "#"}
       "Columnas"
       [:b {:class "caret"}]]
      [:ul {:class "dropdown-menu stop-propagation", :style "overflow:auto;max-height:450px;padding:10px;"}
       [:div {:id "column-label-fields"}]]]
     [:li {:class "dropdown"}
      [:a {:class "dropdown-toggle", :data-toggle "dropdown", :href "#"}
       "Sumarios"
       [:b {:class "caret"}]]
      [:ul {:class "dropdown-menu stop-propagation", :style "overflow:auto;max-height:450px;padding:10px;"}
       [:div {:id "summary-fields"}]]]
     [:li {:class "dropdown pull-right"}
      [:a {:class "dropdown-toggle", :data-toggle "dropdown", :href "#"}
       "Reportes"
       [:b {:class "caret"}]]
      [:ul {:class "dropdown-menu"}
       [:li {} [:a {:id "miami-invoice-detail", :href "#"} "En construccion"]]]]]])

(defn html-hickoried [mapa-str]
  [:div {:class "container"}
   subnav
   [:hr {}]
   [:span {:id "pivot-detail"}]
   [:hr {}]
   [:div {:id "results"}]
  [:script {:type "text/javascript"}
   (js-code mapa-str)]])

(defn pivot-generator [mapa-str] (html (html-hickoried mapa-str)))

;fields es un vector de mapas con llaves "name" "type" "filtrable"
;data llega como un vector de vectores donde el primer vector son los
;nombres de las columnas.
(defn crea-mapa-str [fields data]
  (str "{json: " (generate-string data)
       ", fields:" (js-format fields)
       ", rowLabels: " (js-format fields)"}"))

(defn pivot-with-definitions [fields data] (pivot-generator (crea-mapa-str fields data)))

(defn data-type [o]
  (if (integer? o) "integer"
      (if (float? o) "float"
          "string")))

(defn fn-definitions-from-data [n v]
  (hash-map "name" n
            "type" (data-type v)
            "filterable" true
            "summarizable" "count"))

(defn definitions-from-data [colls]
  (let [names (first colls)
        vars (second colls)]
    (vec (map fn-definitions-from-data names vars))))

(defn stringize-nils "Convierte todos los nils a strings vacias en un vector de vectores" [vecs]
  (vec (map (fn [v] (vec (map #(if (nil? %) "" %) v))) vecs)))

(defn pivot "Genera el hiccup de una pivot table, recibe como argumento un vector de vectores, donde el primero contiene los nombres de las columnas y el resto la data"
  [colls] (pivot-with-definitions (definitions-from-data colls)
                                  (stringize-nils colls)))
