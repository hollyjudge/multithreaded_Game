# multiThreadedGame
A multithreaded game of "falling words". 

This game should operate as follows:
• When the start button is pressed, a specified number of words (a command line
parameter) start falling at the same time from the top of the panel, but fall at
different speeds – some faster, some slower.
• Words disappear when they reach the red zone, whereupon the Missed
counter is incremented by one and a new word starts falling (with a different
speed).
• The user attempts to type all the words before they hit the red zone, pressing
enter after each one.
• If a user types a word correctly, that word disappears, the Caught counter is
incremented by one, and the Score is incremented by the length of the word. A
new word then starts falling (with a different speed).
• If a user types a word incorrectly, it is ignored.
• The game continues until the specified maximum number of words (a
command-line parameter) is reached or the user presses the End button (however the end button does not work at this time). The user can then play again, beginning a new game by pressing the Start button.


WordApp is the class to run to play the game from.

