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
	private final int NUMBER_OF_TURNS = 10;
	private String[][] guesses = new String[NUMBER_OF_TURNS][];
	private int correctLocationAndColorCount = 0;
	private int correctColorWrongPlaceCount = 0;
	private int gameCount = 0;
	private double guessesSum = 0;

	private ArrayList<String> availableComputerGuesses = new ArrayList<String>();		//6! factorial combinations
	private String lastComputerMove = "";


	public MastermindGame(){
		System.out.println("Mastermind colors: w=white, r=red, y=yellow, g=green, o=orange");
		System.out.println("Enter 1 character for each color in the 4-digit code");

		availableComputerGuesses = generateAllPossibleGuesses();

		start();
	}

	private ArrayList<String> generateAllPossibleGuesses(){
		ArrayList<String> possibleGuesses = new ArrayList<String>();
		for (int digit1 = 0; digit1 < validColors.length; digit1++){
			for (int digit2 = 0; digit2 < validColors.length; digit2++){
				for (int digit3 = 0; digit3 < validColors.length; digit3++){
					for (int digit4 = 0; digit4 < validColors.length; digit4++) {
						if (digit1 != digit2 && digit1 != digit3 && digit1 != digit4
							   && digit2 != digit3 && digit2 != digit4
								&& digit3 != digit4)
						{
							possibleGuesses.add(validColors[digit1] + validColors[digit2] + validColors[digit3] + validColors[digit4]);
						}
					}
				}
			}
		}
		return possibleGuesses;
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

		boolean playAgain = true;
		while(playAgain){
			System.out.print("Choose game mode (player/computer): ");
			gameMode = keyboard.next().replaceAll(" ", "");
			if(gameMode.equals("player")){
				generateHiddenCode();

				System.out.print("Enter a 4-character guess for the code's colors");

				keyboard.nextLine();	//clear buffer & move to next line

				boolean correct = false;
				for(int i=0; i<NUMBER_OF_TURNS && !correct; ){
					System.out.print("\nGuess: ");
					String guess = keyboard.nextLine();
					guessesSum++;
					if(isValidGuessInput(guess)){
						guesses[i] = convertGuessStringToArray(guess);
						if(isCorrectCode(guesses[i], gameMode)){
							correct = true;
						}
						i++;	//only increment turn counter if is valid
					}
					else{
						System.out.println("Invalid Input, try again");
					}
				}

				if(correct){
					System.out.println("You win");
				}
				else{
					System.out.println("\nSorry, you ran out of turns\n" + "The correct code was " + Arrays.toString(hiddenCode));
				}
			}
			else if(gameMode.equals("computer")){
				// System.out.print("Enter a 4-digit code for computer to guess");
				generateHiddenCode();

				String repeat = "y";
				while(repeat.equals("y")){
					lastComputerMove = availableComputerGuesses.get((int) (Math.random()*availableComputerGuesses.size()) );
					System.out.println("Computer guess="+lastComputerMove);
					if(isCorrectCode(lastComputerMove.split(""), "computer")){
						System.out.println("WON");
						break;
					}
					else{
						availableComputerGuesses.remove(lastComputerMove);
						System.out.println("Computer guessed wrong, removed before loop: "+lastComputerMove);
					}
					int good=correctLocationAndColorCount;
					int close=correctColorWrongPlaceCount;
					System.out.println("Computer guess \tCorrect location & Color = "+correctLocationAndColorCount + "\tCorrect Color, wrong location = "+correctColorWrongPlaceCount);

					for (int i = 0; i < availableComputerGuesses.size(); i++) {
						good=correctLocationAndColorCount;
						close=correctColorWrongPlaceCount;
						String token = availableComputerGuesses.get(i);
						if(isCorrectCode(token.split(""), "computer")){
							System.out.println("\nGuessed it!");
						}
						System.out.println(i+": \ttoken="+token+"\tprevGood="+good+"\tright="+correctLocationAndColorCount+"\tprevClose="+close+"\tclose="+correctColorWrongPlaceCount);
						if(correctLocationAndColorCount!=4 && (correctLocationAndColorCount!=good || correctColorWrongPlaceCount!=close) ){
							System.out.println("\tRemoved i="+i+": "+token);
							availableComputerGuesses.remove(i--);
						}
					}
					System.out.println("available guesses left="+availableComputerGuesses.size());

					System.out.println("Repeat? ");
					repeat = keyboard.next();
					keyboard.nextLine();
				}

			}
			else{
				System.out.println("Error. Please only enter \"player\" or \"computer\"");
				continue;	//repeat loop asking for input
			}

			gameCount++;
			System.out.println("Average guesses per game = "+getAverage());
			System.out.print("Play again? (y/n) ");
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

	private boolean isCorrectCode(String[] guess, String mode){
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

		if(mode.equals("player")){
			System.out.println("Correct location & Color = "+correctLocationAndColorCount + "\tCorrect Color, wrong location = "+correctColorWrongPlaceCount);
		}

		return correctLocationAndColorCount == hiddenCode.length;
	}

	private double getAverage(){
		return Math.round(guessesSum/gameCount * 100) / 100;
	}


	public static void main(String[] args) {
		MastermindGame game = new MastermindGame();
	}

}