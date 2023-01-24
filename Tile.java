public class Tile {
    int x;
    int y;
    int value;
    boolean bomb;
    boolean covered;


    public Tile(int x, int y, int value, boolean bomb) {
        this.x = x;
        this.y = y;
        this.value = value;
        this.bomb = bomb;
        this.covered = true;
    }

    public char show() {
        if (this.covered) {
            return '#';
        }
        return (char)(this.value + 49);
    }

}