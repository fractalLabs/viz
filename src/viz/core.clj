(ns viz.core
  (:use [hiccup core page element]))


(def graphael (html (include-js "raphael.js" "g.raphael-min.js" "g.pie-min.js" "g.bar-min.js" "g.line-min.js")
   (javascript-tag 
"function addLoadEvent(func) {
  var oldonload = window.onload;
  if (typeof window.onload != 'function') {
    window.onload = func;
  } else {
    window.onload = function() {
      if (oldonload) {
        oldonload();
      }
      func();
    }
  }
}")))

(defn vec-js-format [v] (str "[" (apply str (interpose "," v)) "]"))
(defn vec-js-format-str [v] (str "[\"" (apply str (interpose "\",\"" v)) "\"]"))


(defn pie-js [v strs] (str "window.onload = function () {var r = Raphael(0, 0, 640, 480); r.piechart(320, 240, 100, " (vec-js-format v) ", {legend: " (vec-js-format-str strs) "}  );}"))

(defn pie-js-inter [v strs] 
   (str "addLoadEvent( function () {var r = Raphael(0, 0, 640, 480); pie = r.piechart(320, 240, 100, " (vec-js-format v) ", {legend: " (vec-js-format-str strs) "}  );pie.hover(function () {
                    this.sector.stop();
                    this.sector.scale(1.1, 1.1, this.cx, this.cy);

                    if (this.label) {
                        this.label[0].stop();
                        this.label[0].attr({ r: 7.5 });
                        this.label[1].attr({ \"font-weight\": 800 });
                    }
                }, function () {
                    this.sector.animate({ transform: 's1 1 ' + this.cx + ' ' + this.cy }, 500, \"bounce\");

                    if (this.label) {
                        this.label[0].animate({ r: 5 }, 500, \"bounce\");
                        this.label[1].attr({ \"font-weight\": 400 });
                    }
                });});"))


(defn pie "Dibuja una grafica de pie, recibe un vector de numeros y opcionalmente otro con las etiquetas"
   ([v strs] (html (include-js "raphael.js" "g.raphael-min.js" "g.pie-min.js")
      [:body (javascript-tag (pie-js-inter v strs))]))
   ([v] (pie v [])))


(defn vs-js-format [vs] (vec-js-format (map vec-js-format vs)))
;Bar
(defn bar "Dibuja una grafica de barras, recibe uno o mas vectores con numeros" [& vs] (html (include-js "raphael.js" "g.raphael-min.js" "g.bar-min.js")
	(javascript-tag (str "
            addLoadEvent( function () {
                var r = Raphael(0,0,640,480),
                    fin = function () {
                        this.flag = r.popup(this.bar.x, this.bar.y, this.bar.value || \"0\").insertBefore(this);
                    },
                    fout = function () {
                        this.flag.animate({opacity: 0}, 300, function () {this.remove();});
                    };
                r.barchart(10, 250, 300, 220, " (vs-js-format vs) ", {type: \"soft\"}).hover(fin, fout);
            });
"))))


(defn line-format [vs] 
   (if-not (vector? (first (first vs)))
      (str "[" (vec-js-format (range (count (first vs)))) "], [" (vec-js-format (first vs)) "]")))

(defn line-js [rand-id vs]
(str "
addLoadEvent( function () {
                //var r = Raphael(0,0,640,480),
		  var r"rand-id" = Raphael(\"holder"rand-id"\",'100%','100%'),
                    txtattr = { font: \"12px sans-serif\" };
                
                var lines = r"rand-id".linechart(0, 0, 640, 480, " (line-format vs) ", { nostroke: false, axis: \"0 0 1 1\", symbol: \"circle\", smooth: true }).hoverColumn(function () {
                    this.tags = r"rand-id".set();

                    for (var i = 0, ii = this.y.length; i < ii; i++) {
                        this.tags.push(r"rand-id".tag(this.x, this.y[i], this.values[i], 160, 10).insertBefore(this).attr([{ fill: \"#fff\" }, { fill: this.symbols[i].attr(\"fill\") }]));
                    }
                }, function () {
                    this.tags && this.tags.remove();
                });

                lines.symbols.attr({ r: 6 });
            });
"))

(defn line [& vs] (let [rand-id (int (rand 10000))]
(html
       (javascript-tag (line-js rand-id vs))
      "<div id=\"holder"rand-id"\" style=\"width: 640px; height: 480px;\"></div>")))
