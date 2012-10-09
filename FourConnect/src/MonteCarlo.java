import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;


public class MonteCarlo {
	private static IAI player1;
	private static IAI player2;
	private static int cols;
	private static int rows;
	private static String results = "";
	
	public static void main(String[] args){
		cols = 7;
		rows = 6;
	    player1 = new AlphaBeta(cols,rows, 1);
	    player2 = new AlphaBeta(cols,rows, 2);
	    testStates(3);
	}

	private static void testStates(int steps) {
		ArrayList<GameBoard> states = possibleStates(steps, new GameBoard(cols,rows, 1));
		System.out.println(states);
		for(GameBoard gb : states){
			testState(gb);
		}
		printResults();
	}

	private static ArrayList<GameBoard> possibleStates(int steps, GameBoard state) {
		ArrayList<GameBoard> states = new ArrayList<GameBoard>();
		for (int c = 0; c < cols; c++){
			GameBoard gb = new GameBoard(state);
			if (!gb.isColumnFilled(c)){
				gb.insertCoin(c, gb.getTurn());
				states.add(gb);
				gb.switchTurn();
				if (steps>1){
					states.addAll(possibleStates(steps - 1, new GameBoard(gb)));
				}
			}
		}
		return states;
	}

	private static void testState(GameBoard gb) {
		GameBoard startingState = new GameBoard(gb);
		while(!gb.isFilled() && !gb.isWinner(1, false) && !gb.isWinner(2, false)){
			if (gb.getTurn() == 1){
				gb.insertCoin(player1.decideNextMove(gb),1);
				gb.switchTurn();
			} else {
				gb.insertCoin(player2.decideNextMove(gb),2);
				gb.switchTurn();
			}
		}
		saveResults(startingState, gb);
	}

	private static void saveResults(GameBoard startingState, GameBoard gb) {
		if (gb.isFilled()){
			results += "DRAW:" + "\n";
			results += startingState + "\n";
			results += "-" + "\n";
			results += gb + "\n";
			results += "---------------------" + "\n";
		} else if (gb.isWinner(1, false)){
			results += "Winner (1):" + "\n";
			results += startingState + "\n";
			results += "-" + "\n";
			results += gb + "\n";
			results += "---------------------" + "\n";
		} else if (gb.isWinner(2, false)){
			results += "Winner (2):" + "\n";
			results += startingState + "\n";
			results += "-" + "\n";
			results += gb + "\n";
			results += "---------------------" + "\n";
		}
		System.out.println(results);
	}

	private static void printResults() {
		try{
			  // Create file 
			  FileWriter fstream = new FileWriter("monteCarlo" + System.currentTimeMillis() + ".txt");
			  BufferedWriter out = new BufferedWriter(fstream);
			  out.write(results);
			  //Close the output stream
			  out.close();
		}catch (Exception e){//Catch exception if any
			  System.err.println("Error: " + e.getMessage());
		}
	}
}
