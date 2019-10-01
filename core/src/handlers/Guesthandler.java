package handlers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.Queue;
import entities.Guest;
import entities.Spawnarea;
import entities.Table;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

public class Guesthandler {
    private Queue<Guest> guests;
    private ArrayList<Guest> activeGuests;
    private ArrayList<Guest> guestsToRemove;
    private Spawnarea spawnarea;
    private ArrayList<Sprite> currentOrders;

    public Guesthandler() {
        guests = new Queue<>();
        activeGuests = new ArrayList<>();
        guestsToRemove = new ArrayList<>();
        currentOrders = new ArrayList<>();
    }

    public void handleGuests(int time, Dishhandler dishhandler) {
        if (!guests.isEmpty()) {
            if (guests.first().getSpawnTime() == time) {
                spawnGuest(guests.first(), dishhandler);
            }
        }
        for (Guest g : activeGuests) {
            updateGuest(g, time);
        }
        if (!guestsToRemove.isEmpty()) {
            for (Guest g : guestsToRemove) {
                activeGuests.remove(g);
            }
            guestsToRemove.clear();
        }
    }

    public void updateGuest(Guest guest, int time) {
        long timeElapsed = time - guest.getSpawnTime();
        guest.setTimeElapsed(timeElapsed);
        if (timeElapsed >= guest.getPatience()) {
            guestsToRemove.add(guest);
        } else if (timeElapsed >= (guest.getPatience() / 1.5)) {
            guest.setHappiness(1);
            guest.setColor(Color.RED);
        } else if (timeElapsed >= (guest.getPatience() / 3)) {
            guest.setHappiness(2);
            guest.setColor(Color.YELLOW);
        }
        if (time > guest.getSpawnTime() + 2 && !(time > guest.getSpawnTime()+4)){
            if(!currentOrders.contains(guest.getDish().getSprite())) currentOrders.add(guest.getDish().getSprite());
        }
        if (time > guest.getSpawnTime() + 4){
            currentOrders.remove(guest.getDish().getSprite());
        }
    }

    private void spawnGuest(Guest guest, Dishhandler dishhandler) {
    	if(spawnarea.getFreeTables().size() != 0) {
    		Random rndm = new Random();
    		int tableID = rndm.nextInt(spawnarea.getFreeTables().size());
    		Table table = spawnarea.getFreeTables().get(tableID);
    		spawnarea.removeFreeTable(table);
            guest.setPosition(table.getPosition()[0], table.getPosition()[1]);
            guest.setTable(table);
            table.setGuest(guest);
            dishhandler.addToDishQueue(guest.getOrder());
            Gdx.app.log("INFO: ", "Added " + guest.getOrder() + "to dishqueue");
            activeGuests.add(guest);
            Gdx.app.log("INFO: ", "Spawned " + guest);
            guests.removeFirst();
            Gdx.app.log("INFO: ", "Removed Guest from guestlist ");
    	}
    	
    }

    public void intializeGuests(List<Guest> guests) {
        for (Guest g : guests
        ) {
            this.guests.addLast(g);
        }
    }

    public void removeActiveGuest(Guest guest) {
    	spawnarea.addFreeTable(guest.getTable());
        currentOrders.remove(guest.getDish().getSprite());
        activeGuests.remove(guest);
        guest.getTable().removeGuest();
        Gdx.app.log("INFO: ", "Guest " + guest + " removed.");
        if(guests.isEmpty()) {
        	Gdx.app.log("INFO: ", "You won! Good job!");
        }
    }

    public void setSpawnarea(Spawnarea spawnarea) {
		this.spawnarea = spawnarea;
	}

    public ArrayList<Guest> getActiveGuests() {
        return activeGuests;
    }

    public ArrayList<Sprite> getCurrentOrders() {
        return currentOrders;
    }
}

