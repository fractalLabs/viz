# viz
The clojure js Chart & Pivot Table Library

## Uso

Agrega la dependencia:
[viz "0.1.0-SNAPSHOT"]

Para ver los ejemplos:
git clone git@github.com:fractalLabs/viz
cd viz
lein ring server
http://localhost:3000, localhost:3000/pivot y /bars

Agrega al namespace:
(:use [viz core highcharts])

# Charts
Meter en un (html ) de hiccup:
(html charts) ;importa al html los js de highcharts

Teniendo un mapa descriptivo:
(def chart-map {
"chart" {"type" "bubble" "zoomType" "xy"}
"title" {"text" "Highcharts bubbles"}
"series"[{"data" [[4 2 3] [4 5 6] [7 8 9]]} {"data" [[10 11 12] [13 14 15] [16 17 18]]}]})

Genera la grafica con:
(chart chart-map)

# Pivot Tables
(use 'viz.pivot)
(pivot demo-data) ;vec de vecs en formato "csv"

## License

Copyright © 2013 Fractal Labs
