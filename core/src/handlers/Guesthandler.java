package handlers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Queue;
import entities.Guest;
import entities.Spawnarea;
import entities.Table;

import java.util.ArrayList;
import java.util.Random;

public class Guesthandler {
    private Queue<Guest> guests;
    private ArrayList<Guest> activeGuests;
    private ArrayList<Guest> guestsToRemove;
    private Spawnarea spawnarea;

    public Guesthandler() {
        guests = new Queue<Guest>();
        activeGuests = new ArrayList<Guest>();
        guestsToRemove = new ArrayList<Guest>();
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
        int timeElapsed = time - guest.getSpawnTime();
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
    }

    private void spawnGuest(Guest guest, Dishhandler dishhandler) {

    	System.out.println(spawnarea.getFreeTables().size());
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

    public void intializeGuests() {
        // temporary guest list for testing
        guests.addLast(new Guest(2));
        guests.addLast(new Guest(4));
        guests.addLast(new Guest(6));
        guests.addLast(new Guest(12));
        guests.addLast(new Guest(14));
        guests.addLast(new Guest(17));
        guests.addLast(new Guest(23));
        guests.addLast(new Guest(25));
        Gdx.app.log("INFO: ", "Guests created");
    }

    public void removeActiveGuest(Guest guest) {
    	spawnarea.addFreeTable(guest.getTable());
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

	public Queue<Guest> getGuests() {
        return guests;
    }

    public ArrayList<Guest> getActiveGuests() {
        return activeGuests;
    }

    public ArrayList<Guest> getGuestsToRemove() {
        return guestsToRemove;
    }
}

