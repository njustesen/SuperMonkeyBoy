
public class Slot {
	boolean player1;
	boolean player2;
	
	public Slot(){
		player1 = false;
		player2 = false;
	}
	
	public Slot(int p){
		if (p == 0){
			player1 = false;
			player2 = false;
		} else if (p == 1){
			player1 = true;
			player2 = false;
		} else if (p == 2){
			player1 = false;
			player2 = true;
		}
	}
	
	public Slot(Slot s){
		player1 = s.player1;
		player2 = s.player2;
	}
	
	public boolean isFilled() {
		return (player1 || player2);
	}
	
	@Override
	public String toString(){
		if (isPlayer(1)){
			return "1";
		}
		if (isPlayer(2)){
			return "2";
		}
		return " ";
	}

	public boolean isPlayer(int playerNum) {
		if (playerNum == 1){
			return player1;
		} else if (playerNum == 2){
			return player2;
		} else {
			System.out.println("ERROR! Unknown player.");
		}
		return false;
	}

	public void set(int playerID, boolean b) {
		if (playerID == 1){
			player1 = true;
		} else if (playerID == 2){
			player2 = true;
		} else {
			System.out.println("ERROR! Unknown player.");
		}
	}
}
