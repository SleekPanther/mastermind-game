import java.util.*;

public class MastermindGame {					//d=gold	b=blue		o=orange		k=blacK
	private static final String[] validColors = {"d","y","w","r", "g","b","o","k"};//"r","o","y","g","b", "w"};
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
//		for(int i=0; i<hiddenCode.length; i++){
//			hiddenCode[i] = (int)(Math.random() * validColors.length) ;
//		}
		//Hack it to a specific color setting
		hiddenCode = new int[]{0,1,2,3};
		System.out.println("Hidden code: "+Arrays.toString(hiddenCode) + " = " + printCodeAsString(hiddenCode));
	}

	private String printCodeAsString(int[] code){
		String codeString = "[";
		for(int digit : code){
			codeString += (validColors[digit] + "  ");
		}
		codeString += "]";
		return codeString;
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
		}
	}

	private boolean isValidGuessInput(String guess){
		guess = guess.trim().replaceAll(" ", "");
		return guess.length() == 4;		//maybe also check if it only contains valid colors (&& guess.matches("^[stuff in valid characters]"))
	}

	private String[] convertGuessStringToArray(String guess){
		return guess.trim().replaceAll(" ", "").split("");
	}

	private boolean isCorrectCode(String[] guess){
		int correctLocationAndColorCount = 0;
		int correctColorCount = 0;
		ArrayList<Integer> finalizedIndices = new ArrayList<Integer>();
		for(int i=0; i<guess.length; i++){
			if(stringToIntegerColor.get(guess[i]) == hiddenCode[i]){
				correctLocationAndColorCount++;
				finalizedIndices.add(i);
			}
			else{
				System.out.println("Finalized="+finalizedIndices);
				for(int j=0; j<hiddenCode.length; j++){		//
					if(!finalizedIndices.contains(j) && stringToIntegerColor.get(guess[i]) == hiddenCode[j]){		//only check indeces that aren't already correct
						correctColorCount++;
						finalizedIndices.add(j);
					}
				}
			}
		}

		System.out.println("Correct location & Color = "+correctLocationAndColorCount + "\tCorrect Color, wrong location = "+correctColorCount);

		return correctLocationAndColorCount == hiddenCode.length;
	}

	
	public static void main(String[] args) {
		MastermindGame game = new MastermindGame();
	}

}