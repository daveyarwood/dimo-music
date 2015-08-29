(set-env!
  :source-paths   #{"src"}
  :dependencies   '[[org.clojure/clojure "1.7.0"]
                    [http.async.client   "0.6.1"]
                    [nio                 "1.0.4-SNAPSHOT"]
                    [alda                "0.1.1"]])

(require '[dimo.core :as dimo])

(deftask run
  "Run the application."
  []
  (dimo/-main))
