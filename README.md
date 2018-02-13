# Mastermind
Command line implementation of the board game Mastermind for a Software Engineering class

## Algorithm
An adaptation of the [Knuth Algorithm](https://en.wikipedia.org/wiki/Mastermind_(board_game)#Algorithms)

1. The computer generates all possible 4-character code of unique colors
2. Picks a random code from these possibilities & calls an evaluation function feedback
3. It then loops over all remaining possibilities, calls the evaluation function & compares the new feedback to the feedback from the 1st guess eliminating all possibilities that give different feedback. (e.g. if the evaluation function returns "2 correct color & location" and "1 correct color, incorrect location" it removes all codes that don't give the feedback 2 & 1).
4. Go back to step 2, pick another code from remaining possibilities & evaluate all other possibilities eliminating the ones that score the same. This continues until it's the correct code or the game runs out of guesses.
