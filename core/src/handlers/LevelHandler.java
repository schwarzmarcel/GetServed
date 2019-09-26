package handlers;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Queue;
import com.badlogic.gdx.utils.Timer;

import entities.*;

import java.util.ArrayList;
import java.util.Iterator;
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
    private int time;
    private int money;
    private Queue<Guest> guests;
    private ArrayList<Guest> activeGuests;
    private ArrayList<Guest> guestsToRemove;
    private Queue<Foodtype> dishQueue;
    private ArrayList<Dish> dishes; 
    private World world;
    private Waiter waiter;
    private boolean checkIfInstanceAlreadyCreated;
    private Spawnarea spawnarea;
    private int dishTimer;

    public LevelHandler(int numberOfDishes) {
        this.numberOfDishes = numberOfDishes;
        world = new World(new Vector2(0, 0), true);
        Walls walls = new Walls(world);
        money = 60;
        guests = new Queue<Guest>();
    	activeGuests = new ArrayList<Guest>();
    	guestsToRemove = new ArrayList<Guest>();
    	dishQueue = new Queue<Foodtype>();
        dishes = new ArrayList<Dish>();
    }

    public void initializeLevel() {
    	
        drawField();
        intializeWaiter();
        startTimer();
        intializeGuests();
    }

    private void intializeWaiter() {
        waiter = new Waiter(world, WORLD_WIDTH / 2, WORLD_HEIGHT / 2);
    }

    private void intializeGuests() {
    	// temporary guest list for testing 
    	guests.addLast(new Guest(1));
    	guests.addLast(new Guest(6));
    	guests.addLast(new Guest(11));
    	guests.addLast(new Guest(16));
    	guests.addLast(new Guest(21));
    }
    
    private void spawnGuest(Guest guest) {
    	
      for (Table t: spawnarea.getTables()
      ) {
     if(t.getGuest() == null){
         guest.setPosition(t.getPosition()[0],t.getPosition()[1]);
         guest.setTable(t);
         t.setGuest(guest);
         dishQueue.addLast(guest.getOrder());
         activeGuests.add(guest);
         guests.removeFirst();
         // replace with visual cue for order
         System.out.println("Guest orders " + guest.getOrder());
         return;
     }
 }
    }

    private void updateGuest(Guest guest) {
	    
	    float timeElapsed = time - guest.getSpawnTime();
	    if (timeElapsed >= guest.getPatience()) {
	    	guestsToRemove.add(guest);
	    } else if (timeElapsed >= (guest.getPatience() / 1.5)) {
	        guest.setHappiness(1);
	        guest.setColor(Color.RED);
	    } else if (timeElapsed >= (guest.getPatience() / 3)) {
	        guest.setHappiness(2);
	        guest.setColor(Color.YELLOW);
	    }
	}

	private void drawField(){
        spawnarea = new Spawnarea();
        spawnarea.printGridDimensions();
        Gridposition pos1 = new Gridposition(6,20,"table");
        Gridposition pos2 = new Gridposition(8,20, "table");
        Gridposition pos3 = new Gridposition(10,20, "table");
        Gridposition pos4 = new Gridposition(12,20, "table");
        Gridposition pos5 = new Gridposition(14,20, "table");
        Gridposition pos6 = new Gridposition(16,20, "table");
        Gridposition pos7 = new Gridposition(10,3, "counter");
        Gridposition pos8 = new Gridposition(5,3, "counter");
        Gridposition pos9 = new Gridposition(15,3, "counter");
        Gridposition[] gridpositions = new Gridposition[9];
        gridpositions[0] = pos1;
        gridpositions[1] = pos2;
        gridpositions[2] = pos3;
        gridpositions[3] = pos4;
        gridpositions[4] = pos5;
        gridpositions[5] = pos6;
        gridpositions[6] = pos7;
        gridpositions[7] = pos8;
        gridpositions[8] = pos9;
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

    public World getWorld() {
        return world;
    }

    public Queue<Guest> getGuests() {
        return guests;
    }
    
    public ArrayList<Guest> getActiveGuests() {
		return activeGuests;
	}

	public void removeActiveGuest(Guest guest) {
    	activeGuests.remove(guest);
    	guest.getTable().removeGuest();
    }

    public ArrayList<Dish> getDishes() {
        return dishes;
    }

    public void updateDishTimer(){
        this.dishTimer = time;
    }

    public void updateLevel() {
        if (waiter.getDish() != null) {
            waiter.getDish().getSprite().setPosition(
                    (waiter.getBody().getPosition().x * PIXELS_TO_METERS) - waiter.getSprite().getWidth() / 2,
                    (waiter.getBody().getPosition().y * PIXELS_TO_METERS) - waiter.getSprite().getHeight() / 2
            );
        }
        if(!guests.isEmpty()) {
        	if(guests.first().getSpawnTime() == time) {
        		spawnGuest(guests.first());
        	}
        }
        for (Guest g : activeGuests) {
            updateGuest(g);
        }
        if(!guestsToRemove.isEmpty()) {
        	for(Guest g : guestsToRemove) {
        		activeGuests.remove(g);
        	}
        	guestsToRemove.clear();
        }
        for (Counter c: spawnarea.getCounters()
        ) {
            if(dishTimer+1 < time){
                if(c.getDish() == null && !dishQueue.isEmpty()){
                    Dish tempDish = new Dish(dishQueue.removeFirst(),c.getPosition());
                    c.setDish(tempDish);
                    dishes.add(tempDish);
                }
                if(dishQueue.isEmpty() && c.getDish() == null){
                    Dish tempDish = new Dish(Foodtype.getRandomFoodType(),c.getPosition());
                    c.setDish(tempDish);
                    dishes.add(tempDish);
                }
            }
        }
        //TEMPORARY TRASH
        if(Gdx.input.isKeyJustPressed(Input.Keys.X)){
            if(waiter.getDish() != null){
                dishes.remove(waiter.getDish());
            }
            waiter.setDish(null);
        }
    }
    
    public int getMoney() {
		return money;
	}

	public void setMoney(int money) {
		this.money = money;
	}
}
