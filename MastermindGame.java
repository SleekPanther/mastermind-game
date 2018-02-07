import java.util.*;

public class MastermindGame {
	private static final String[] validColors = {
		"w",	//White
		"b",	//Blue
		"r",	//Red
		"y",	//Yellow
		"g",	//Green
		"o",	//Orange
	};	
	private String[] hiddenCode = new String[4];
	private final int NUMBER_OF_TURNS = 12;
	private String[][] guesses = new String[NUMBER_OF_TURNS][];
	private int correctLocationAndColorCount = 0;
	private int correctColorWrongPlaceCount = 0;
	private int gameCount = 0;
	private double guessesTotal = 0;

	private ArrayList<String[]> availableComputerGuesses = new ArrayList<String[]>();		//6! factorial combinations
	private String[] lastComputerGuess;


	public MastermindGame(){
		System.out.println("Mastermind colors: w=white, r=red, y=yellow, g=green, o=orange");
		System.out.println("Enter 1 character for each color in the 4-digit code");

		availableComputerGuesses = generateAllPossibleGuesses();

		start();
	}

	private ArrayList<String[]> generateAllPossibleGuesses(){
		ArrayList<String[]> allPossibleGuesses = new ArrayList<String[]>();
		for (int digit1 = 0; digit1 < validColors.length; digit1++){
			for (int digit2 = 0; digit2 < validColors.length; digit2++){
				for (int digit3 = 0; digit3 < validColors.length; digit3++){
					for (int digit4 = 0; digit4 < validColors.length; digit4++) {
						if (digit1 != digit2 && digit1 != digit3 && digit1 != digit4
							   && digit2 != digit3 && digit2 != digit4
								&& digit3 != digit4)
						{
							allPossibleGuesses.add(new String[]{validColors[digit1], validColors[digit2], validColors[digit3], validColors[digit4]});
						}
					}
				}
			}
		}
		return allPossibleGuesses;
	}

	//Random number to create code, allows repeated colors
	private void generateHiddenCode(){
		ArrayList<String> usedColors = new ArrayList<String>();
		for(int i=0; i<hiddenCode.length; ){
			int randomIndexForColor = (int)(Math.random() * validColors.length);
			String randomColor = validColors[randomIndexForColor];
			if(!usedColors.contains(randomColor)){
				hiddenCode[i] = randomColor;
				usedColors.add(randomColor);
				i++;
			}			
		}
		//Hack it to a specific color setting
		//hiddenCode = new String[]{"k", "d", "r", "b"};
		System.out.println("Hidden code: "+Arrays.toString(hiddenCode));
	}
	
	private void start(){
		Scanner keyboard = new Scanner(System.in);

		String gameMode = "";
		guesses = new String[NUMBER_OF_TURNS][];

		boolean playAgain = true;
		while(playAgain){
			System.out.print("Choose game mode (player/computer): ");
			gameMode = keyboard.next().replaceAll(" ", "");
			// gameMode = "computer";
			if(gameMode.equals("player")){
				generateHiddenCode();

				System.out.print("Enter a 4-character guess for the code's colors");

				boolean won = false;
				for(int i=0; i<NUMBER_OF_TURNS && !won; ){
					System.out.print("\nGuess: ");
					String guess = keyboard.nextLine();
					guessesTotal++;
					if(isValidGuessInput(guess)){
						guesses[i] = convertGuessStringToArray(guess);
						if(isCorrectCode(guesses[i])){
							won = true;
						}
						i++;	//only increment turn counter if it's a valid guess
						System.out.println("Correct location & Color = "+correctLocationAndColorCount + "\tCorrect Color, wrong location = "+correctColorWrongPlaceCount);
					}
					else{
						System.out.println("Invalid Input, try again");
					}
				}

				if(won){
					System.out.println("You win");
				}
				else{
					System.out.println("\nSorry, you ran out of turns\n" + "The correct code was " + Arrays.toString(hiddenCode));
				}
			}
			else if(gameMode.equals("computer")){
				// generateHiddenCode();
				System.out.print("Enter a 4-digit code for computer to guess");
				String code = keyboard.nextLine().replaceAll(" ", "").toLowerCase();
				while(!isValidGuessInput(code)){
					System.out.print("Invalid! Please enter a 4-digit code using valid colors: ");
					code = keyboard.nextLine().replaceAll(" ", "").toLowerCase();
				}
				hiddenCode = code.split("");

				boolean won = false;
				for(int turn=0; turn<NUMBER_OF_TURNS && !won; turn++){
					guessesTotal++;
					lastComputerGuess = availableComputerGuesses.get((int) (Math.random()*availableComputerGuesses.size()) );	//pick random from remaining possible guesses
					guesses[turn] = lastComputerGuess;
					System.out.print("Computer guess="+Arrays.toString(lastComputerGuess));
					if(isCorrectCode(lastComputerGuess)){
						won = true;
						break;
					}
					else{		//Remove current guess from pool if it's not correct
						availableComputerGuesses.remove(lastComputerGuess);
					}

					//Save result from current guess to compare with all other remaining guesses
					int previousCorrectCount=correctLocationAndColorCount;
					int previousCloseCount=correctColorWrongPlaceCount;
					System.out.println("\tCorrect location & Color = "+correctLocationAndColorCount + "\tCorrect Color, wrong location = "+correctColorWrongPlaceCount);

					for (int i = 0; i < availableComputerGuesses.size(); i++) {
						previousCorrectCount=correctLocationAndColorCount;
						previousCloseCount=correctColorWrongPlaceCount;
						String[] possibleGuess = availableComputerGuesses.get(i);
						//Remove all combinations that give a different response than the current guess (also skip the correct code & DO NOT remove it from remaining guesses)
						if(!isCorrectCode(possibleGuess) && (correctLocationAndColorCount!=previousCorrectCount || correctColorWrongPlaceCount!=previousCloseCount) ){
							availableComputerGuesses.remove(i--);	//remove guess & decrement I since removing from list shifts all items 1 index back
						}
					}
				}

				System.out.println();
				if(won){
					System.out.println("Computer wins");
				}
				else{
					System.out.println("Sorry, computer ran out of turns\n" + "The correct code was " + Arrays.toString(hiddenCode));
				}
			}
			else{
				System.out.println("Error. Please only enter \"player\" or \"computer\"");
				continue;	//repeat loop asking for input
			}

			gameCount++;
			System.out.println("Average guesses per game = "+getAverage());
			System.out.print("\nPlay again? (y/n) ");
			playAgain=false;
			if(keyboard.next().toLowerCase().equals("y")){
				playAgain=true;
			}
		}
		System.out.println("Goodbye");

		keyboard.close();
	}

	private boolean isValidGuessInput(String guess){
		guess = guess.trim().replaceAll(" ", "");
		return guess.length() == 4;		//maybe also check if it only contains valid colors (&& guess.matches("^[stuff in valid characters]"))
	}

	private String[] convertGuessStringToArray(String guess){
		return guess.trim().replaceAll(" ", "").toLowerCase().split("");
	}

	private boolean isCorrectCode(String[] guess){
		correctLocationAndColorCount = 0;
		correctColorWrongPlaceCount = 0;

		ArrayList<Integer> finalizedIndices = new ArrayList<Integer>();
		ArrayList<String> colorsAlreadySearched = new ArrayList<String>();

		//Search for correct location & correct color
		for(int i=0; i<guess.length; i++){
			if(guess[i].equals(hiddenCode[i])){
				correctLocationAndColorCount++;
				finalizedIndices.add(i);
			}
		}

		//Search remaining spaces for correct colors in incorrect locations
		for(int i=0; i<guess.length; i++){
			for(int j=0; j<hiddenCode.length; j++){
				if(!finalizedIndices.contains(j) && !colorsAlreadySearched.contains(guess[i]) && guess[i].equals(hiddenCode[j])){		//only check indices that aren't already correct & colors that haven't been searched
					correctColorWrongPlaceCount++;
					finalizedIndices.add(j);
					colorsAlreadySearched.add(guess[i]);
				}
			}
		}

		return correctLocationAndColorCount == hiddenCode.length;
	}

	private double getAverage(){
		return Math.round(guessesTotal/gameCount * 100) / 100;
	}


	public static void main(String[] args) {
		MastermindGame game = new MastermindGame();
	}

}