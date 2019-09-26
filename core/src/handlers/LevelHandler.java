package handlers;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Timer;

import entities.*;

import java.util.ArrayList;
import java.util.List;

import static com.mygdx.game.MyGdxGame.*;
/*
  TODO: eine Queue für alle Counter
        eine Queue für alle Gäste
        wenn mehr als ein Table free choose randomly
        Counter immer mit richtigen Dishes vollfüllen, wenn nicht möglich leeren Counter mit random Dish füllen
 */
public class LevelHandler {
    private int numberOfDishes;
    private float time;
    private int money;
    private ArrayList<Guest> guests;
    private ArrayList<Guest> activeGuests;
    private Counter[] counters;
    private ArrayList<Foodtype> dishQueue;
    private ArrayList<Dish> dishes; 
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
        dishes = new ArrayList<Dish>();
        counters = new Counter[1];
        drawField();
        intializeWaiter();
        intializeCounters();
        startTimer();
        intializeGuests();
    }

    private void intializeCounters() {
        float positionX = WORLD_WIDTH / 7;
        float positionY = WORLD_HEIGHT / 2;
        counters[0] = new Counter(world, positionX, positionY);
    }

    private void intializeWaiter() {
        waiter = new Waiter(world, WORLD_WIDTH / 2, WORLD_HEIGHT / 2);
    }

    private void intializeGuests() {
        for (Table t: spawnarea.getTables()
             ) {
            if(!t.isOccupied()){
                Guest tempGuest = new Guest(t.getPosition()[0],t.getPosition()[1], time);
                tempGuest.setTable(t);
                t.setGuest(tempGuest);
                guests.add(tempGuest);
            }
        }
    }

    private void drawField(){
        spawnarea = new Spawnarea();
        spawnarea.printGridDimensions();
        Gridposition pos1 = new Gridposition(3,5);
        Gridposition pos2 = new Gridposition(6,10);
        Gridposition pos3 = new Gridposition(9,20);
        Gridposition pos4 = new Gridposition(12,5);
        Gridposition pos5 = new Gridposition(15,5);
        Gridposition pos6 = new Gridposition(10,31);
        Gridposition[] gridpositions = new Gridposition[6];
        gridpositions[0] = pos1;
        gridpositions[1] = pos2;
        gridpositions[2] = pos3;
        gridpositions[3] = pos4;
        gridpositions[4] = pos5;
        gridpositions[5] = pos6;
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
        return counters[0];
    }

    public World getWorld() {
        return world;
    }

    public List<Guest> getGuests() {
        return guests;
    }

    public ArrayList<Dish> getDishes() {
        return dishes;
    }

    public void updateLevel() {
        if ((time % 10) == 0) {
            if(!checkIfInstanceAlreadyCreated) {
                checkIfInstanceAlreadyCreated = true;
                dishes.add(new Dish());
            }
            dishes.get(0).setPosition(counters[0].getPosition());
            counters[0].setDish(dishes.get(0));
        }
        if (waiter.getDish() != null) {
            waiter.getDish().getSprite().setPosition(
                    (waiter.getBody().getPosition().x * PIXELS_TO_METERS) - waiter.getSprite().getWidth() / 2,
                    (waiter.getBody().getPosition().y * PIXELS_TO_METERS) - waiter.getSprite().getHeight() / 2
            );
        }
        for (Guest g : guests
        ) {
            updateGuest(g);
        }
    }
    
    public void updateGuest(Guest guest) {
        
        float timeElapsed = time - guest.getSpawnTime();
        if (timeElapsed >= guest.getPatience()) {
            //TODO: despawn with guest handler
        } else if (timeElapsed >= (guest.getPatience() / 1.5)) {
            guest.setHappiness(1);
            guest.setColor(Color.RED);
        } else if (timeElapsed >= (guest.getPatience() / 3)) {
            guest.setHappiness(2);
            guest.setColor(Color.YELLOW);
        }
    }

	public int getMoney() {
		return money;
	}

	public void setMoney(int money) {
		this.money = money;
	}
}
