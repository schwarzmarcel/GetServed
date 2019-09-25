package entities;

public class Gridposition {
    private  int row;
    private  int col;
    public Gridposition(int row, int col) {
        this.row = row;
        this.col = col;
    }
    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }
}
