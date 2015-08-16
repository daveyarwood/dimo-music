(ns dimo.core
  (:require [http.async.client :as client]
            [dimo.kinect       :as kinect]
            [dimo.space        :as space]))

(def url "ws://localhost:1337")

(defn -main []
  (with-open [client (client/create-client)]
    (let [depths (kinect/depth-stream client url)]
      (loop [] 
        (when-let [depths @depths] 
          (printf "in-range: %s, track-hand: %s, avg-depth: %s\n"
                  (space/in-range depths 600 800)
                  (space/track-hand depths 600 800)
                  (space/average-depth depths)))
        (Thread/sleep 30) 
        (recur)))))
