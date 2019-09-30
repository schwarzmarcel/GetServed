package entities;

public class Gridposition {
    private  int row;
    private  int col;
    private  String type;
    public Gridposition(int row, int col, String type) {
        this.row = row;
        this.col = col;
        this.type = type;
    }
    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public String getType(){
        return type;
    }

    @Override
    public String toString() {
        return "Gridposition{" +
                "row=" + row +
                ", col=" + col +
                ", type='" + type + '\'' +
                '}';
    }

    public Gridposition() {
    }
}
