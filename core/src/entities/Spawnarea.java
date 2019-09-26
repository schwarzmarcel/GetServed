package entities;

import com.badlogic.gdx.physics.box2d.World;

import java.util.ArrayList;
import java.util.List;

import static com.mygdx.game.MyGdxGame.WORLD_HEIGHT;
import static com.mygdx.game.MyGdxGame.WORLD_WIDTH;

public class Spawnarea {

    private final float CELLSIZE = 5;
    private List<Table> tables = new ArrayList<Table>();

    public Spawnarea(){
        initializeGrid();
    }
    private void initializeGrid(){
        int matrixHeight = (int) (WORLD_HEIGHT / CELLSIZE);
        int matrixWidth = (int) (WORLD_WIDTH/ CELLSIZE);
    }
    public void initializeTables(Gridposition[] gridpositions, World world){
        for (Gridposition g: gridpositions
             ) {
            float tablePositionY = (g.getRow() * CELLSIZE) - CELLSIZE/2;
            float tablePositionX = (g.getCol() * CELLSIZE) - CELLSIZE/2;
            System.out.println("X:" + tablePositionX + " Y:" + tablePositionY);
            tables.add(new Table(world,tablePositionX,tablePositionY));
        }
    }
    public void printGridDimensions(){
        System.out.println("Number of Rows:" + (int) WORLD_HEIGHT / CELLSIZE);
        System.out.println("Number of Columns:" + (int) WORLD_WIDTH / CELLSIZE);
    }

    public List<Table> getTables() {
        return tables;
    }
}
