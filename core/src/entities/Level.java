package entities;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

import static com.mygdx.game.MyGdxGame.WORLD_HEIGHT;
import static com.mygdx.game.MyGdxGame.WORLD_WIDTH;

public class Level {
    private int numberOfTables;
    private int numberOfGuests;
    private double difficulty;
    private Table[] tables;
    private Guest[] guests;
    private World world;
    private Walls walls;
    private Waiter waiter;

    public Level(int numberOfTables, int numberOfGuests, double difficulty) {
        this.numberOfTables = numberOfTables;
        this.numberOfGuests = numberOfGuests;
        this.difficulty = difficulty;
        world = new World(new Vector2(0, 0), true);	    walls = new Walls(world);
        walls = new Walls(world);
    }

    public void initializeLevel(){
        tables = new Table[numberOfTables];
        guests = new Guest[numberOfGuests];
        intializeGuests();
        intializeTables();
        intializeWaiter();
    }

    private void intializeWaiter(){
        waiter = new Waiter(world, WORLD_WIDTH / 2, WORLD_HEIGHT / 2);
    }

    private void intializeGuests(){

    }
    private void intializeTables(){
        for(int i = 0; i < numberOfTables; i++){
            tables[0] = new Table(world,WORLD_WIDTH / 4, WORLD_HEIGHT / 2);
        }
    }

    public Waiter getWaiter() {
        return waiter;
    }
    public Table[] getTables(){
        return tables;
    }

    public World getWorld() {
        return world;
    }
}
