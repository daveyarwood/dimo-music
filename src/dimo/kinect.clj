(ns dimo.kinect
  (:require [http.async.client :as client]
            [nio.core          :as nio]))

(defn on-open [ws]
  (println "Connected to WebSocket."))

(defn on-close [ws code reason]
  (println "Connection to WebSocket closed.\n"
           (format "[%s] %s" code reason)))

(defn on-error [ws e]
  (println "ERROR:" e))

(defn close-on-exit 
  "Closes a websocket if an interrupt signal (e.g. Ctrl-C) is received."
  [ws]
  (.addShutdownHook (Runtime/getRuntime)
    (Thread. (fn [] (client/close ws)))))

(defn ->ints 
  "Java is dumb, so this function converts a byte array into numbers we can 
   actually use."
  [ba]
  (mapv #(bit-and % 0xfff) (nio/buffer-seq (nio/short-buffer ba))))

(defn depth-stream
  "Streams data from the Kinect WebSocket at `url`, using `client`.
   
   Creates and returns an atom, which is updated on each message from the 
   WebSocket with a new byte-array of depth values from the Kinect."
  [client url]
  (println "Connecting...")
  (let [depths (atom nil)
        ws (client/websocket client 
                             url
                             :open  on-open
                             :close on-close
                             :error on-error
                             :byte  (fn [ws msg] 
                                      (reset! depths (->ints msg))))]
    (close-on-exit ws)
    depths))
