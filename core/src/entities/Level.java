package entities;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Timer;

import static com.mygdx.game.MyGdxGame.*;

public class Level {
    private int numberOfTables;
    private int numberOfGuests;
    private double difficulty;
    private float time;
    private int money;
    private Table[] tables;
    private Guest[] guests;
    private World world;
    private Walls walls;
    private Waiter waiter;

    public Level(int numberOfTables, int numberOfGuests, double difficulty) {
        this.numberOfTables = numberOfTables;
        this.numberOfGuests = numberOfGuests;
        this.difficulty = difficulty;
        world = new World(new Vector2(0, 0), true);
        walls = new Walls(world);
        money = 60;
        
    }

    public void initializeLevel(){
        tables = new Table[numberOfTables];
        guests = new Guest[numberOfGuests];
        intializeTables();
        intializeGuests();
        intializeWaiter();
        startTimer();
    }

    private void intializeWaiter(){
        waiter = new Waiter(world, WORLD_WIDTH / 2, WORLD_HEIGHT / 2);
    }

    private void intializeGuests(){
        float[] positions = new float[2];
        int currentTable = 0;
        for(int i = 0; i < numberOfGuests; i++){
            if(currentTable < tables.length){
                positions = tables[currentTable].getPosition();
                positions[0] = positions[0] + (WORLD_WIDTH/30);
            }else{
                currentTable = 0;
            }
            guests[i] = new Guest(world, positions[0], positions[1], time);
            guests[i].setTable(tables[currentTable]);
            currentTable++;
        }
    }
    private void intializeTables(){
        float positionX = WORLD_WIDTH/2 + (WORLD_WIDTH/4);
        float positionY = 0;
        for(int i = 0; i < numberOfTables; i++){
            positionY = (float) (positionY + (0.2*WORLD_HEIGHT));
            positionX = (float) (positionX - (10));
            tables[i] = new Table(world, positionX, positionY);
        }
    }

    private void startTimer() {
        time = 0;
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                time++;
                if(money > 0)
                money--;
            }
        }, 0, 1);
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
    public Guest[] getGuests(){
        return guests;
    }
    public float getTime(){
        return time;
    }

	public int getMoney() {
		return money;
	}
	
	public void setMoney(int money) {
		this.money = money;
	}
}
