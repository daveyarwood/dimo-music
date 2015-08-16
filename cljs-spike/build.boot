(set-env!
  :source-paths   #{"src"}
  :resource-paths #{"assets"}
  :dependencies   '[[org.clojure/clojure       "1.7.0"]
                    [org.clojure/clojurescript "0.0-3308"]
                    [org.clojure/core.async    "0.1.346.0-17112a-alpha"]
                    [servant                   "0.1.3"]
                    [adzerk/boot-cljs-repl     "0.1.9"]
                    [adzerk/boot-reload        "0.3.1"]
                    [pandeiro/boot-http        "0.6.2"]
                    [adzerk/boot-cljs          "0.0-3308-0"]
                    [tailrecursion/boot-hoplon "0.1.0"]
                    [tailrecursion/hoplon      "6.0.0-SNAPSHOT"]
                    [mantra                    "0.3.1"]])

(require
  '[clojure.java.io :as io]
  '[adzerk.boot-cljs :refer [cljs]]
  '[adzerk.boot-cljs-repl :refer [cljs-repl start-repl]]
  '[adzerk.boot-reload :refer [reload]]
  '[pandeiro.boot-http :refer [serve]]
  '[tailrecursion.boot-hoplon :refer [haml hoplon prerender html2cljs]])

(deftask dev
  "Build for local development."
  []
  (merge-env! :source-paths #{"src"})
  (comp (watch)
        (speak)
        (hoplon :pretty-print true)
        (reload)
        (cljs :source-map true :optimizations :none)
        (serve)))
