(ns dimo.sound
  (:require [alda.lisp  :refer :all]
            [alda.now   :as alda]))

(score*)
(part* "upright-bass")

(def notes
  "an E minor / G major pentatonic scale for bass"
  [[:e 1] [:g 1] [:a 1] [:b 1] [:d 2]
   [:e 2] [:g 2] [:a 2] [:b 2] [:d 3]
   [:e 3] [:g 3] [:a 3] [:b 3] [:d 4]
   [:e 4]])

(defn bass-note
  "Calculates a note to play and a volume at which to play it."
  [hand-x avg-depth]
  (when (and hand-x avg-depth) 
    (let [note-index (-> hand-x (/ 640) (* 15) double Math/round)
          volume (max 0 (double (/ (- 1850 avg-depth) 1850)))]
      [(notes note-index) (* 100 volume)])))

(defn play-bass-note!
  [[[p o] v :as bass-note]]
  (when bass-note
    (prn bass-note)
    (prn (score-map))
    (alda/play! (octave o) 
                (set-attribute :volume v)
                (note (pitch p)))))

