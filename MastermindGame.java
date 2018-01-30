import java.util.*;

public class MastermindGame {
	private static final String[] validColors = {"r","o","y","g","b"};
	private Map<String, Integer> stringToIntegerColor = new HashMap<String, Integer>();
	
	private int[] hiddenCode = new int[4];


	public MastermindGame(){
		for(int i=0; i<validColors.length; i++){
			stringToIntegerColor.put(validColors[i], i);
		}

		generateHiddenCode();
	}

	//Random number to create code, allows repeated colors
	private void generateHiddenCode(){
		for(int i=0; i<hiddenCode.length; i++){
			hiddenCode[i] = (int)(Math.random() * validColors.length) ;
		}
		System.out.println(Arrays.toString(hiddenCode));
	}

	
	public static void main(String[] args) {
		MastermindGame game = new MastermindGame();
	}

}