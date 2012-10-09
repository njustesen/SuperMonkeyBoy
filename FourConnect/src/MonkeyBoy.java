import java.util.ArrayList;


public class MonkeyBoy implements IGameLogic {
    private IGameBoard gameBoard;
    private IAI ai;
    private boolean monteCarlo;
    private int moves = 0;
    private ArrayList<Integer> actions = new ArrayList<Integer>();
    
    public MonkeyBoy() {
        
    }
	
    public void initializeGame(int cols, int rows, int playerID) {
        gameBoard = new GameBoard(cols,rows, playerID);
        ai = new Minimax(cols,rows, playerID);
    }
	
    public Winner gameFinished() {
    	if (gameBoard.isWinner(1, true)){
    		return Winner.PLAYER1;  
    	}
    	if (gameBoard.isWinner(2, true)){
    		return Winner.PLAYER2;    	
    	}
    	if (gameBoard.isFilled()){
    		return Winner.TIE;    
    	}
        return Winner.NOT_FINISHED;
    }

    public void insertCoin(int col, int playerID) {
        gameBoard.insertCoin(col, playerID);
    }

    public int decideNextMove() {
    	int m = 0;
    	if (monteCarlo && moves < actions.size()){
    		m = actions.get(moves);
    		moves++;
    	} else {
    		m = ai.decideNextMove(gameBoard);
    	}
        return m;
    }
}
