import java.util.*;

public class MastermindGame {
	private static final String[] validColors = {"r","o","y","g","b"};
	private Map<String, Integer> stringToIntegerColor = new HashMap<String, Integer>();
	
	private int[] hiddenCode = new int[4];
	private final int NUMBER_OF_TURNS = 10;
	private String[][] guesses = new String[NUMBER_OF_TURNS][];


	public MastermindGame(){
		for(int i=0; i<validColors.length; i++){
			stringToIntegerColor.put(validColors[i], i);
		}

		start();
	}

	//Random number to create code, allows repeated colors
	private void generateHiddenCode(){
		for(int i=0; i<hiddenCode.length; i++){
			hiddenCode[i] = (int)(Math.random() * validColors.length) ;
		}
		System.out.println("Hidden code: "+Arrays.toString(hiddenCode));
	}

	private void start(){
		generateHiddenCode();

		System.out.print("Enter a 4-character guess for the code's colors");

		Scanner keyboard = new Scanner(System.in);

		boolean correct = false;
		for(int i=0; i<NUMBER_OF_TURNS && !correct; ){
			System.out.print("Guess: ");
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
			// System.out.println();
		}
	}

	private boolean isValidGuessInput(String guess){
		guess = guess.trim().replaceAll(" ", "");
		return guess.length() == 4;		//maybe also check if it only contains valid colors
	}

	private String[] convertGuessStringToArray(String guess){
		return guess.trim().replaceAll(" ", "").split("");
	}

	private boolean isCorrectCode(String[] guess){
		return false;
	}

	
	public static void main(String[] args) {
		MastermindGame game = new MastermindGame();
	}

}