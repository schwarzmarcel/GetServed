package handlers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Queue;
import entities.Guest;
import entities.Spawnarea;
import entities.Table;

import java.util.ArrayList;

public class Guesthandler {
    private Queue<Guest> guests;
    private ArrayList<Guest> activeGuests;
    private ArrayList<Guest> guestsToRemove;

    public Guesthandler() {
        guests = new Queue<Guest>();
        activeGuests = new ArrayList<Guest>();
        guestsToRemove = new ArrayList<Guest>();
    }

    public void handleGuests(int time, Spawnarea spawnarea, Dishhandler dishhandler) {
        if (!guests.isEmpty()) {
            if (guests.first().getSpawnTime() == time) {
                spawnGuest(guests.first(), spawnarea, dishhandler);
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

    private void spawnGuest(Guest guest, Spawnarea spawnarea, Dishhandler dishhandler) {

        for (Table t : spawnarea.getTables()
        ) {
            if (t.getGuest() == null) {
                guest.setPosition(t.getPosition()[0], t.getPosition()[1]);
                guest.setTable(t);
                t.setGuest(guest);
                dishhandler.addToDishQueue(guest.getOrder());
                Gdx.app.log("INFO: ", "Added " + guest.getOrder() + "to dishqueue");
                activeGuests.add(guest);
                Gdx.app.log("INFO: ", "Spawned " + guest);
                guests.removeFirst();
                Gdx.app.log("INFO: ", "Removed Guest from guestlist ");
                return;
            }
        }
    }

    public void intializeGuests() {
        // temporary guest list for testing
        guests.addLast(new Guest(2));
        guests.addLast(new Guest(6));
        guests.addLast(new Guest(11));
        guests.addLast(new Guest(16));
        guests.addLast(new Guest(21));
        Gdx.app.log("INFO: ", "Guests created");
    }

    public void removeActiveGuest(Guest guest) {
        activeGuests.remove(guest);
        guest.getTable().removeGuest();
        Gdx.app.log("INFO: ", "Guest " + guest + " removed.");
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

