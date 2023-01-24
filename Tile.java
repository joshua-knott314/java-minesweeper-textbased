
public class Tile {
	int x;
	int y;
	int numBombNeighbors = 0;
	boolean isCovered = true;
	boolean isBomb;
	boolean hasFlag = false;
	
	
	public Tile(int y, int x, boolean bomb_bool) {
		this.y = y;
		this.x = x;
		this.isBomb = bomb_bool;
	}
	
	public String getAppearance() {		
		if (this.hasFlag) {
			return "F";
		}else if (this.isCovered) {
			return "-";
		} else if (this.isBomb) {
			return "*";
		} else if (this.numBombNeighbors > 0){
			return Integer.toString(this.numBombNeighbors);
		} else {
			return ".";
		}
	}
}