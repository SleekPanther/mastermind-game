import java.util.*;

public class MastermindGame {
	private static final String[] validColors = {
		"w",	//White
		"d",	//golD
		"k",	//blacK
		"b",	//Blue
		"r",	//Red
		"y",	//Yellow
		"g",	//Green
		"o",	//Orange
	};
	
	private String[] hiddenCode = new String[4];
	private final int NUMBER_OF_TURNS = 10;
	private String[][] guesses = new String[NUMBER_OF_TURNS][];


	public MastermindGame(){
		start();
	}

	//Random number to create code, allows repeated colors
	private void generateHiddenCode(){
		for(int i=0; i<hiddenCode.length; i++){
			int randomIndexForColor = (int)(Math.random() * validColors.length);
			hiddenCode[i] = validColors[randomIndexForColor];
		}
		//Hack it to a specific color setting
		hiddenCode = new String[]{"w", "d", "k", "b"};
		System.out.println("Hidden code: "+Arrays.toString(hiddenCode));
	}

	private void start(){
		generateHiddenCode();

		System.out.print("Enter a 4-character guess for the code's colors");

		Scanner keyboard = new Scanner(System.in);

		boolean correct = false;
		for(int i=0; i<NUMBER_OF_TURNS && !correct; ){
			System.out.print("\nGuess: ");
			String guess = keyboard.nextLine();
			if(isValidGuessInput(guess)){
				guesses[i] = convertGuessStringToArray(guess);
				if(isCorrectCode(guesses[i])){
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

	private boolean isValidGuessInput(String guess){
		guess = guess.trim().replaceAll(" ", "");
		return guess.length() == 4;		//maybe also check if it only contains valid colors (&& guess.matches("^[stuff in valid characters]"))
	}

	private String[] convertGuessStringToArray(String guess){
		return guess.trim().replaceAll(" ", "").toLowerCase().split("");
	}

	private boolean isCorrectCode(String[] guess){
		int correctLocationAndColorCount = 0;
		int correctColorWrongPlaceCount = 0;

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

		System.out.println("Correct location & Color = "+correctLocationAndColorCount + "\tCorrect Color, wrong location = "+correctColorWrongPlaceCount);

		return correctLocationAndColorCount == hiddenCode.length;
	}


	public static void main(String[] args) {
		MastermindGame game = new MastermindGame();
	}

}