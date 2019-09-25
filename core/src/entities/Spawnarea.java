package entities;

import com.badlogic.gdx.physics.box2d.World;

import static com.mygdx.game.MyGdxGame.WORLD_HEIGHT;
import static com.mygdx.game.MyGdxGame.WORLD_WIDTH;

public class Spawnarea {

    private Table[][] grid;

    public Spawnarea(){
        initializeGrid();
    }
    private void initializeGrid(){
        int matrixHeight = (int) WORLD_HEIGHT / 5;
        int matrixWidth = (int) WORLD_WIDTH/ 5;
        grid = new Table[matrixHeight][matrixWidth];
    }
    public void initializeTables(Gridposition[] gridpositions, World world){
        for (Gridposition g: gridpositions
             ) {
            float tablePositionY = (float) ((g.getRow() * 5) - 2.5);
            float tablePositionX = (float) ((g.getCol() * 5) - 2.5 + WORLD_WIDTH/2);
            System.out.println("X:" + tablePositionX + " Y:" + tablePositionY);
            grid[g.getRow()][g.getCol()] = new Table(world,tablePositionX,tablePositionY);
        }
    }
    public void printGridDimensions(){
        System.out.println("Number of Rows:" + (int) WORLD_HEIGHT / 5);
        System.out.println("Number of Columns:" + (int) WORLD_WIDTH / 5);
    }
    /*
    Spawnarea test = new Spawnarea();
        test.printGridDimensions();
        Gridposition pos = new Gridposition(9,16);
        Gridposition[] blabla = new Gridposition[1];
        blabla[0] = pos;
        test.initializeTables(blabla,world);
     */
}
