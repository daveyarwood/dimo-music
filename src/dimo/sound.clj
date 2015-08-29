(ns dimo.sound
  (:require [alda.lisp  :refer :all]
            [alda.sound :as sound]
            [alda.now   :refer (play!)]))

(defn set-up! 
  []
  (score*)
  (part* "upright-bass")
  (note (pitch :c)) ; HACK
  (sound/set-up! :midi (score-map)))

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
    (play! (octave o) 
           (set-attribute :volume v)
           (note (pitch p)))))

(comment 
  "The above already works. One caveat is that all instruments need to be
   initialized before (sound/set-up! :midi (score-map)) happens.

   Subsequent calls to (part* 'foo') (i.e. to change the currently playing 
   instruments) will work, provided you are calling an instrument that has 
   already been initialized and loaded into the MIDI synth.

   Otherwise, if new instruments are added, (sound/set-up! :midi (score-map)
   must be called again in order to load the instruments into the MIDI synth. 
   
   TODO:
   - possible bug with sound/set-up! it only sets up instruments that have 
     notes in the score already. maybe it should set up all declared instruments?
     - although maybe this won't matter if I do the below
   - consider making the sound/set-up! step automatic. Maybe provide a 
     function to allow users to set which types they will be using, 
     e.g. :midi, and the function will add these types to a dynamic var.
     Then now/play! can call sound/set-up! on the list of types each time it
     is called.
     - better yet, consider whether this setup can just be done automatically 
     by Alda when alda.sound/play! is called. This will involve implementing
     `determine-audio-types`. It should still be possible to manually 
     call sound/set-up! for specific audio types, but it would be cool if you
     could leave it out and let Alda figure it out.
       - once this is done, can probably simplify alda.repl a bit -- right
         now there is some code duplication between alda.repl and alda.now."
)
