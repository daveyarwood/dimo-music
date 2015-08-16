(ns dimo.space)

(defn average-depth [depths]
  (int (/ (apply + depths) (* 640 480))))

(defn in-range 
  "Returns the number of depth values that are between `min-z` and `max-z`."
  [depths min-z max-z]
  (count (filter #(<= min-z % max-z) depths)))

(defn with-coords
  "Takes the 1D vector of depths from the Kinect and assigns an [x y] 
   coordinate in the 640 x 480 field to each depth value.
   
   The result is still essentially a 1D list, but each item is a 3-item vector
   [x y z], where x and y are the coordinates in the 640 x 480 space and z
   is the depth value."
  [depths]
  (map-indexed (fn [i z] [(rem i 640) (quot i 640) z]) depths))

(defn track-hand
  "Gets averages of the x and y values of all pixels with depth values
   between `min-z` and `max-z`.
   
   Returns the average x and y values."
  [depths min-z max-z]
  (let [in-range (filter (fn [[x y z]] (<= min-z z max-z)) 
                         (with-coords depths))]
    (when-not (empty? in-range)
      (->> (reduce (fn [[sum-x sum-y] [x y z]] 
                     [(+ sum-x x) (+ sum-y y)])
                   [0 0]
                   in-range)
           (mapv #(quot % (count in-range)))))))
