package entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.World;
import exceptions.InputNotValidException;

import java.util.ArrayList;
import java.util.List;

import static com.mygdx.game.MyGdxGame.WORLD_HEIGHT;
import static com.mygdx.game.MyGdxGame.WORLD_WIDTH;

public class Spawnarea {

    private final float CELLSIZE = 5;
    private final int rowNumber = (int) (WORLD_HEIGHT / CELLSIZE);
    private final int colNumber = (int) (WORLD_WIDTH / CELLSIZE);
    private List<Table> tables = new ArrayList<Table>();
    private List<Counter> counters = new ArrayList<>();
    private List<Table> freeTables = new ArrayList<Table>();

    public Spawnarea(){
    }

    public void initializeTables(List<Gridposition> gridpositions, World world) throws InputNotValidException {
        for (Gridposition g: gridpositions
             ) {
            if (g != null) {
                if ((g.getCol() > colNumber) || (g.getRow() > rowNumber) || (g.getRow() < 0) || (g.getCol() < 0)) {
                    throw (new InputNotValidException("Column or Row for " + g.getType() + " too large or too small"));
                }
                float positionY = (g.getRow() * CELLSIZE) - CELLSIZE / 2;
                float positionX = (g.getCol() * CELLSIZE) - CELLSIZE / 2;
                if (g.getType().equals("counter")) {
                    counters.add(new Counter(world, positionX, positionY, g.getRotation()));
                    Gdx.app.log("INFO: ", "Counter created");
                } else if (g.getType().equals("table")) {
                	Table table = new Table(world, positionX, positionY);
                    tables.add(table);
                    freeTables.add(table);
                    Gdx.app.log("INFO: ", "Table created");

                }
            }
        }
    }
    public void printGridDimensions(){
        System.out.println("Number of Rows:" + (int) WORLD_HEIGHT / CELLSIZE);
        System.out.println("Number of Columns:" + (int) WORLD_WIDTH / CELLSIZE);
    }

    public List<Table> getTables() {
        return tables;
    }
    
    public List<Table> getFreeTables(){
    	return freeTables;
    }
    
    public void addFreeTable(Table table) {
    	freeTables.add(table);
    }
    
    public void removeFreeTable(Table table) {
    	freeTables.remove(table);
    }
    
    public List<Counter> getCounters(){
        return counters;
    }

    @Override
    public String toString() {
        return "Spawnarea{" +
                "CELLSIZE=" + CELLSIZE +
                ", rowNumber=" + rowNumber +
                ", colNumber=" + colNumber +
                ", tables=" + tables +
                ", counters=" + counters +
                '}';
    }
}
