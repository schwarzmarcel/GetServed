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

    public Level(int numberOfTables, int numberOfGuests, double difficulty) {
        this.numberOfTables = numberOfTables;
        this.numberOfGuests = numberOfGuests;
        this.difficulty = difficulty;
        world = new World(new Vector2(0, 0), true);
    }

    public void initializeLevel(){
        tables = new Table[numberOfTables];
        guests = new Guest[numberOfGuests];
        intializeGuests();
        intializeTables();
    }
    private void intializeGuests(){

    }
    private void intializeTables(){
        for(int i = 0; i < numberOfTables; i++){
            tables[0] = new Table(world,WORLD_WIDTH / 4, WORLD_HEIGHT / 2);
        }
    }
}
