import java.util.ArrayList;


public class Minimax implements IAI {
	int cols;
	int rows;
	int playerID;
	int INFMAX = 10000;
	int INFMIN = -10000;
	int WIN = 100;
	int LOSS = -100;
	int DRAW = 0;
	int POS_WIN_SLOT = 3;
	int[][] POS_VALUES;
	int depth = 0;
	private int INC_LIMIT = 500000;
	private boolean hasRunOnce = false;;
	
	public Minimax(int cols, int rows, int playerID) {
		this.cols = cols;
		this.rows = rows;
		this.playerID = playerID;
		this.POS_VALUES = new int[][] {
				{0 ,0 ,0 ,1 ,0 ,0 ,0 },
				{0 ,0 ,1 ,2 ,1 ,0 ,0 },
				{0 ,1 ,2 ,3 ,2 ,1 ,1 },
				{0 ,3 ,4 ,5 ,4 ,2 ,0 },
				{0 ,4 ,6 ,8 ,6 ,4 ,0 },
				{0 ,6 ,8 ,10,8 ,6 ,0 }
				};
	}

	@Override
	public int decideNextMove(IGameBoard gameBoard) {
		long time = System.currentTimeMillis();
		adjustDepth(gameBoard);
		hasRunOnce = true;
		Statistics.NODES = 0;
		System.out.println("Depth: " + depth);
		/* TEST */
		//((GameBoard) gameBoard).testWin();
		
		int action = miniMax(gameBoard, depth);
		
		// Nodes in search tree
		System.out.println("Minimax");
		System.out.println("Nodes: " + Statistics.NODES);
		// Action
		System.out.println("Time used: " + ((System.currentTimeMillis() - time)/1000) + " sek.");
		return action;
	}
	
	private void adjustDepth(IGameBoard gameBoard) {
		depth = (int) ((14-cols) * (1 + gameBoard.ratioFull()));
		if (depth < 5){
			depth = 5;
		}
	}

	private int miniMax(IGameBoard state, int depth) {
		int v = INFMIN;
		int bestAction = 0;
		int newV;
		ArrayList<Integer> actions = actions(state);
		for(int i = 0; i < actions.size(); i++){
			newV = Math.max(v, minValue(result(state, actions.get(i)), depth-1));
			//System.out.println("Action" + i + " value: " + newV + ", best action is [Action" + bestAction + "]");
			if (newV > v){
				v = newV;
				bestAction = actions.get(i);
			}
		}
		//System.out.println("BestAction: " + bestAction + ", value: " + v);
		return bestAction;
	}
	
	private int minValue(IGameBoard state, int depth){
		if (state.isFilled() || state.isWinner(1, false) || state.isWinner(2, false)){
			return eval(state);
		}
		int v = INFMAX;
		if (depth > 0){
			for(int a : actions(state)){
				v = Math.min(v, maxValue(result(state, a), depth-1));
			}
		} else {
			return eval(state);
		}
		return v;
	}
	
	private int maxValue(IGameBoard state, int depth){
		if (state.isFilled() || state.isWinner(1, false) || state.isWinner(2, false)){
			return eval(state);
		}
		int v = INFMIN;
		if (depth > 0){
			for(int a : actions(state)){
				v = Math.max(v, minValue(result(state, a), depth-1));
			}
		} else {
			return eval(state);
		}
		return v;
	}

	private IGameBoard result(IGameBoard state, int action) {
		IGameBoard newState = new GameBoard(state);
		newState.insertCoin(action, state.getTurn());
		newState.switchTurn();
		return newState;
	}

	private int eval(IGameBoard state) {
		// Terminal states
		if (state.isWinner(playerID, false)){
			return WIN;
		} 
		if (state.isLooser(playerID)){
			return LOSS;
		}
		if (state.isFilled()){
			return DRAW;
		}
		
		// Non-terminal states
		int v = 0;
		
		// Positional values
		for (int c = 0; c < state.getCols(); c++){
			for (int r = 0; r < state.getRows(); r++){
				if (state.getSlot(c, r, -1).isPlayer(playerID)){
					v += POS_VALUES[r][c];
				} else if (state.getSlot(c, r, -1).isFilled()){
					v -= POS_VALUES[r][c];
				}
			}
		}
		return v;
	}

	private ArrayList<Integer> actions(IGameBoard state) {
		ArrayList<Integer> actions = new ArrayList<Integer>();
		for(int c = 0; c < cols; c++){
			if (!state.isColumnFilled(c)){
				actions.add(c);
			}
		}
		return actions;
	}
}
