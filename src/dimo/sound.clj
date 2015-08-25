(ns dimo.sound)

(comment 
  "Working on making Alda usable as a library.

  Usage will be something like this:"

  (require '[alda.lisp  :refer :all]
           '[alda.sound :as sound]
           '[alda.now   :refer (play!)])

  (score*)
  (part* "guitar")
  (sound/set-up! :midi (score-map)) 

  (play! (chord (note (pitch :c))
                (note (pitch :e))
                (note (pitch :g))))

  "The above already works. One caveat is that all instruments need to be
   initialized before (sound/set-up! :midi (score-map)) happens.

   Subsequent calls to (part* 'foo') (i.e. to change the currently playing 
   instruments) will work, provided you are calling an instrument that has 
   already been initialized and loaded into the MIDI synth.

   Otherwise, if new instruments are added, (sound/set-up! :midi (score-map)
   must be called again in order to load the instruments into the MIDI synth. 
   
   TODO:
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
