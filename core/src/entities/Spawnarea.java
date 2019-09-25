package entities;

import com.badlogic.gdx.physics.box2d.World;

import static com.mygdx.game.MyGdxGame.WORLD_HEIGHT;
import static com.mygdx.game.MyGdxGame.WORLD_WIDTH;

public class Spawnarea {

    private Table[][] grid;
    private final float CELLSIZE = 5;

    public Spawnarea(){
        initializeGrid();
    }
    private void initializeGrid(){
        int matrixHeight = (int) (WORLD_HEIGHT / CELLSIZE);
        int matrixWidth = (int) (WORLD_WIDTH/ CELLSIZE);
        grid = new Table[matrixHeight][matrixWidth];
    }
    public void initializeTables(Gridposition[] gridpositions, World world){
        for (Gridposition g: gridpositions
             ) {
            float tablePositionY = (float) ((g.getRow() * CELLSIZE) - CELLSIZE/2);
            float tablePositionX = (float) ((g.getCol() * CELLSIZE) - CELLSIZE/2);
            System.out.println("X:" + tablePositionX + " Y:" + tablePositionY);
            grid[g.getRow()][g.getCol()] = new Table(world,tablePositionX,tablePositionY);
        }
    }
    public void printGridDimensions(){
        System.out.println("Number of Rows:" + (int) WORLD_HEIGHT / CELLSIZE);
        System.out.println("Number of Columns:" + (int) WORLD_WIDTH / CELLSIZE);
    }

    public Table[][] getGrid() {
        return grid;
    }
}
