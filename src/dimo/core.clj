(ns dimo.core
  (:require [http.async.client :as client]
            [dimo.kinect       :as kinect]
            [dimo.space        :as space]
            [dimo.sound        :as sound]))

(def url "ws://localhost:1337")

(defn- debug 
  [in-range track-hand avg-depth bass-note]
  (printf "in-range: %s, track-hand: %s, avg-depth: %s, bass note: %s\n"
          in-range track-hand avg-depth bass-note))

(defn debug-loop
  [depths]
  (loop [] 
    (when-let [depths @depths] 
      (let [in-range        (space/in-range depths 1200 1400)
            [hand-x hand-y] (space/track-hand depths 1200 1400)
            avg-depth       (space/average-depth depths)
            bass-note       (sound/bass-note hand-x avg-depth)]
        (debug in-range [hand-x hand-y] avg-depth bass-note)))
    (Thread/sleep 30) 
    (recur)))

(defn bass-loop
  [depths]
  (loop []
    (when-let [depths @depths]
      (let [[hand-x hand-y] (space/track-hand depths 1200 1400)
            avg-depth       (space/average-depth depths)
            bass-note       (sound/bass-note hand-x avg-depth)]
        (sound/play-bass-note! bass-note)))
    (Thread/sleep 500)
    (recur)))

(defn -main []
  (sound/set-up!)
  (with-open [client (client/create-client)]
    (let [depths (kinect/depth-stream client url)]
      (bass-loop depths))))
