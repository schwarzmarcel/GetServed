package handlers;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Timer;

import entities.*;

import java.util.ArrayList;
import java.util.List;

import static com.mygdx.game.MyGdxGame.*;

public class LevelHandler {
    private int numberOfDishes;
    private float time;
    private int money;
    private ArrayList<Guest> guests;
    private Counter counter;
    private Dish[] dishes;
    private World world;
    private Waiter waiter;
    private boolean checkIfInstanceAlreadyCreated;
    private Spawnarea spawnarea;

    public LevelHandler(int numberOfDishes) {
        guests = new ArrayList<Guest>();
        this.numberOfDishes = numberOfDishes;
        world = new World(new Vector2(0, 0), true);
        Walls walls = new Walls(world);
        money = 60;
    }

    public void initializeLevel() {
        dishes = new Dish[numberOfDishes];
        drawField();
        intializeWaiter();
        intializeCounters();
        startTimer();
        intializeGuests();
    }

    private void intializeCounters() {
        float positionX = WORLD_WIDTH / 7;
        float positionY = WORLD_HEIGHT / 2;
        counter = new Counter(world, positionX, positionY);
    }

    private void intializeWaiter() {
        waiter = new Waiter(world, WORLD_WIDTH / 2, WORLD_HEIGHT / 2);
    }

    private void intializeGuests() {
        for (int i = 0; i < spawnarea.getGrid().length; i++) {
            for (int k = 0; k < spawnarea.getGrid()[i].length; k++) {
                if(spawnarea.getGrid()[i][k] != null && !spawnarea.getGrid()[i][k].isOccupied()){
                    Guest tempGuest = new Guest(spawnarea.getGrid()[i][k].getPosition()[0], spawnarea.getGrid()[i][k].getPosition()[1], time);
                    tempGuest.setTable(spawnarea.getGrid()[i][k]);
                    spawnarea.getGrid()[i][k].setGuest(tempGuest);
                    guests.add(tempGuest);
                }
            }
        }

    }

    public void drawField(){
        spawnarea = new Spawnarea();
        spawnarea.printGridDimensions();
        Gridposition pos1 = new Gridposition(3,5);
        Gridposition pos2 = new Gridposition(6,10);
        Gridposition pos3 = new Gridposition(9,20);
        Gridposition pos4 = new Gridposition(12,5);
        Gridposition pos5 = new Gridposition(15,5);
        Gridposition[] gridpositions = new Gridposition[5];
        gridpositions[0] = pos1;
        gridpositions[1] = pos2;
        gridpositions[2] = pos3;
        gridpositions[3] = pos4;
        gridpositions[4] = pos5;
        spawnarea.initializeTables(gridpositions,world);
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
    public Counter getCounter() {
        return counter;
    }

    public World getWorld() {
        return world;
    }

    public List<Guest> getGuests() {
        return guests;
    }

    public Dish[] getDishes() {
        return dishes;
    }

    public void updateLevel() {
        if ((time % 10) == 0) {
            if(!checkIfInstanceAlreadyCreated) {
                checkIfInstanceAlreadyCreated = true;
                dishes[0] = new Dish();
            }
            dishes[0].setPosition(counter.getPosition());
            counter.setDish(dishes[0]);
        }
        if (waiter.getDish() != null) {
            waiter.getDish().getSprite().setPosition(
                    (waiter.getBody().getPosition().x * PIXELS_TO_METERS) - waiter.getSprite().getWidth() / 2,
                    (waiter.getBody().getPosition().y * PIXELS_TO_METERS) - waiter.getSprite().getHeight() / 2
            );
        }
        for (Guest g : guests
        ) {
            g.update(time);
        }
    }

	public int getMoney() {
		return money;
	}

	public void setMoney(int money) {
		this.money = money;
	}

    public Spawnarea getSpawnarea() {
        return spawnarea;
    }
}
