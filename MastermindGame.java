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


	public MastermindGame(){
		System.out.println("Mastermind colors: w=white, r=red, y=yellow, g=green, o=orange");
		System.out.println("Enter 1 character for each color in the 4-digit code");
		start();
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
				System.out.print("Enter a 4-digit code for computer to guess");
				
			}
			else{
				System.out.println("Error. Please only enter \"player\" or \"computer\"");
				continue;	//repeat loop asking for input
			}

			System.out.print("Play again? (y/n)");
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


	public static void main(String[] args) {
		MastermindGame game = new MastermindGame();
	}

}