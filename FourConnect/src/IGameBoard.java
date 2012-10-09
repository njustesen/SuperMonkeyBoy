
public interface IGameBoard {

	boolean isWinner(int playerNum, boolean printWhich);

	boolean isLooser(int playerID);

	boolean isFilled();

	void insertCoin(int column, int playerID);

	boolean isColumnFilled(int column);

	int getTurn();

	void switchTurn();

	int getCols();
	
	int getRows();

	Slot getSlot(int col, int row, int index);

	double ratioFull();

	int GetBoardValue(int playerNum);
	
}
