
public class GameBoard implements IGameBoard {

	private static final int FOUR_IN_ROW = 10;
	private static final int THREE_IN_ROW = 4;
	private static final int TWO_IN_ROW = 2;
	private Slot[] slots;
	private int cols;
	private int rows;
	private boolean player1Turn;
	
	public GameBoard(int cols, int rows, int turn) {
		this.cols = cols;
		this.rows = rows;
		if (turn == 1){
			this.player1Turn = true;
		} else {
			this.player1Turn = false;
		}
		setupBoard(cols,rows);
		Statistics.NODES++;
	}

	public GameBoard(IGameBoard state) {
		this.cols = state.getCols();
		this.rows = state.getRows();
		if (state.getTurn() == 1){
			this.player1Turn = true;
		} else {
			this.player1Turn = false;
		}
		cloneBoard(state);
		Statistics.NODES++;
	}
	
	public GameBoard(int cols, int rows, int turn, int[] slots) {
		this.cols = cols;
		this.rows = rows;
		if (turn == 1){
			this.player1Turn = true;
		} else {
			this.player1Turn = false;
		}
		this.slots = new Slot[cols*rows];
		for(int i = 0; i < slots.length; i++){
			this.slots[i] = new Slot(slots[i]);
		}
		Statistics.NODES++;
	}

	private void cloneBoard(IGameBoard state) {
		slots = new Slot[cols*rows];
		for(int i = 0; i < slots.length; i++){
			slots[i] = new Slot(state.getSlot(0,0,i));
		}
	}

	private void setupBoard(int cols, int rows) {
		slots = new Slot[cols*rows];
		for(int i = 0; i < slots.length; i++){
			slots[i] = new Slot();
		}
	}

	@Override
	public boolean isWinner(int playerNum, boolean print) {
		if (isWinnerVertical(playerNum)){
			if (print){
				System.out.println("VerticalWin.");
			}
			return true;
		}
		if (isWinnerHorizontal(playerNum)){
			if (print){
				System.out.println("HorizontalWin.");
			}
			return true;
		}
		if (isWinnerDiagonal(playerNum)){
			if (print){
				System.out.println("DiagonalWin.");
			}
			return true;
		}
		return false;
	}

	private boolean isWinnerDiagonal(int playerNum) {
		for(int i = 0; i < slots.length; i++){
			if (slots[i].isPlayer(playerNum)){
				int highestDia = Math.max(highestDiagonalLeft(i, playerNum), highestDiagonalRight(i, playerNum));
				if (highestDia >= 4){
					return true;
				}
			}
		}
		return false;
	}

	private int highestDiagonalLeft(int slot, int playerNum) {
		int x = 0;
		int left = 0;
		if (slots[slot].isPlayer(playerNum)){
			x = 1;
			if (slot + cols - 1 < slots.length && slot%cols != 0){
				left = highestDiagonalLeft(slot + cols - 1, playerNum);
			}
		}
		return x + left;
	}
	
	private int highestDiagonalRight(int slot, int playerNum) {
		int x = 0;
		int right = 0;
		if (slots[slot].isPlayer(playerNum)){
			x = 1;
			if (slot + cols + 1 < slots.length && slot%cols != cols - 1){
				right = highestDiagonalRight(slot + cols + 1, playerNum);
			}
		}
		return x + right;
	}

	private boolean isWinnerHorizontal(int playerNum) {
		int inRow = 0;
		for(int s = 0; s < slots.length; s++){
			if (s%cols == 0){
				inRow = 0;
			}
			if (slots[s].isFilled()){
				if (slots[s].isPlayer(playerNum)){
					inRow++;
				} else {
					inRow = 0;
				}
			} else {
				inRow = 0;
			}
			if (inRow == 4){
				return true;
			}
		}
		return false;
	}

	private boolean isWinnerVertical(int playerNum) {
		int inRow = 0;
		for(int c = 0; c < cols; c++){
			for(int r = 0; r < rows; r++){
				if (slots[c + cols*r].isPlayer(playerNum)){
					inRow = 1;
					int rr = r;
					rr++;
					while(rr < rows){
						if (slots[c + cols*rr].isPlayer(playerNum)){
							inRow++;
							rr++;
						} else {
							break;
						}
					}
					if (inRow == 4){
						return true;
					}
				}
				
			}
		}
		return false;
	}

	@Override
	public boolean isFilled() {
		for(int c = 0; c < cols; c++){
			if (!isColumnFilled(c)){
				return false;
			}
		}
		return true;
	}
	
	@Override
	public boolean isColumnFilled(int c) {
		if (slots[c].isFilled()){
			return true;
		}
		return false;
	}

	@Override
	public void insertCoin(int column, int playerID) {
		Slot slot = slots[column];
		// Room for coin?
		if (slot.isFilled()){
			// Slot is filled - should throw exception
		} else {
			// Find coin slot
			int row = 1;
			while(row < rows){
				if (slots[column + cols*row].isFilled()){
					slot = slots[column + cols*(row - 1)];
					break;
				}
				row++;
				if (!(row < rows)){
					slot = slots[column + cols*(row - 1)];
				}
			}
			// Set coin
			slot.set(playerID, true);
		}
	}

	@Override
	public String toString(){
		String s = "";
		for(int r = 0; r < rows; r++){
			for(int c = 0; c < cols; c++){
				s += "[" + slots[r*cols+c] + "]";
			}
			s += "\n";
		}
		return s;
	}

	@Override
	public int getTurn() {
		if (player1Turn){
			return 1;
		}
		return 2;
	}

	@Override
	public void switchTurn() {
		if (player1Turn){
			player1Turn = false;
		} else {
			player1Turn = true;
		}
	}

	@Override
	public boolean isLooser(int playerID) {
		if (playerID == 1){
			return isWinner(2, false);
		}
		if (playerID == 2){
			return isWinner(1, false);
		}
		return false;
	}

	@Override
	public int getCols() {
		return cols;
	}

	@Override
	public int getRows() {
		return rows;
	}

	@Override
	public Slot getSlot(int col, int row, int index) {
		if (index >= 0){
			return slots[index];
		} else {
			return slots[col + row*cols];
		}
	}

	@Override
	public double ratioFull() {
		int n = 0;
		for(int s = 0; s < slots.length; s++){
			if (slots[s].isFilled()){
				n++;
			}
		}
		if (n == 0){
			return 0;
		}
		return (double)(n)/slots.length;
	}
	
	/* TEST WINNING ALGORITHMS */
	public void testWin(){
		int[] slots1 = new int[] {
				2,1,1,1,2,0,1,
				0,2,2,1,1,2,2,
				1,1,1,2,1,2,2,
				2,1,2,1,1,2,1,
				2,1,1,2,2,0,1,
				1,2,2,0,1,1,2 
				};
		GameBoard test1 = new GameBoard(7,6,1,slots1);
		test1.isWinner(1, true);
		test1.isWinner(2, true);
	}
	
	@Override
	public int GetBoardValue(int playerNum) {
		int val = 0;
		val += boardValueDiagonal(playerNum);
		val += boardValueVertical(playerNum);
		val += boardValueHorizontal(playerNum);
		return val;
	}

	private int boardValueDiagonal(int playerNum) {
		int val = 0;
		for(int i = 0; i < slots.length; i++){
			if (slots[i].isPlayer(playerNum) || !slots[i].isFilled()){
				int highestDia = 0;
				if (!slots[i].isFilled()){
					highestDia = Math.max(highestDiagonalLeftValue(i, playerNum, false), highestDiagonalRightValue(i, playerNum, true));
				} else {
					highestDia = Math.max(highestDiagonalLeftValue(i, playerNum, false), highestDiagonalRightValue(i, playerNum, false));
				}
				if (highestDia >= 4){
					val += FOUR_IN_ROW;
				}
				if (highestDia >= 3){
					val += THREE_IN_ROW;
				}
				if (highestDia >= 2){
					val += TWO_IN_ROW;
				}
			}
		}
		return val;
	}
	
	private int highestDiagonalLeftValue(int slot, int playerNum, boolean emptyFound) {
		int x = 0;
		int left = 0;
		if (slots[slot].isPlayer(playerNum)){
			x = 1;
			if (slot + cols - 1 < slots.length && slot%cols != 0){
				left = highestDiagonalLeftValue(slot + cols - 1, playerNum, emptyFound);
			}
		} else if (!slots[slot].isFilled() && !emptyFound){
			x = 1;
			if (slot + cols - 1 < slots.length && slot%cols != 0){
				left = highestDiagonalLeftValue(slot + cols - 1, playerNum, true);
			}
		}
		return x + left;
	}
	
	private int highestDiagonalRightValue(int slot, int playerNum, boolean emptyFound) {
		int x = 0;
		int right = 0;
		if (slots[slot].isPlayer(playerNum) || !slots[slot].isFilled()){
			x = 1;
			if (slot + cols - 1 < slots.length && slot%cols != 0){
				right = highestDiagonalRightValue(slot + cols - 1, playerNum, emptyFound);
			}
		} else if (!slots[slot].isFilled() && !emptyFound){
			x = 1;
			if (slot + cols - 1 < slots.length && slot%cols != 0){
				right = highestDiagonalRightValue(slot + cols - 1, playerNum, true);
			}
		}
		return x + right;
	}

	private int boardValueHorizontal(int playerNum) {
		int val = 0;
		int inRow = 0;
		boolean emptyFound = false;
		for(int s = 0; s < slots.length; s++){
			if (s%cols == 0){
				inRow = 0;
			}
			if (slots[s].isFilled()){
				if (slots[s].isPlayer(playerNum)){
					inRow++;
				} else {
					inRow = 0;
				}
			} else {
				if (emptyFound){
					if (inRow == 4){
						val += FOUR_IN_ROW;
					} else if (inRow == 3){
						val += THREE_IN_ROW;
					} else if (inRow == 2){
						val += TWO_IN_ROW;
					}
					inRow = 0;
				} else {
					inRow++;
					emptyFound = true;
				}
			}
			
		}
		return val;
	}

	private int boardValueVertical(int playerNum) {
		int val = 0;
		int inRow = 0;
		int[][] checked = new int[cols][rows];
		boolean emptyFound = false;
		for(int c = 0; c < cols; c++){
			for(int r = 0; r < rows; r++){
				if ((slots[c + cols*r].isPlayer(playerNum) || !slots[c + cols*r].isFilled()) && checked[c][r] == 0){
					inRow = 1;
					int rr = r;
					rr++;
					while(rr < rows){
						checked[c][rr] = 1;
						if (slots[c + cols*rr].isPlayer(playerNum) || !emptyFound){
							inRow++;
							rr++;
							emptyFound = true;
						} else {
							break;
						}
					}
					if (inRow == 4){
						val += FOUR_IN_ROW;
					} else if (inRow == 3){
						val += THREE_IN_ROW;
					} else if (inRow == 2){
						val += TWO_IN_ROW;
					} 
				}
			}
		}
		return val;
	}
}
