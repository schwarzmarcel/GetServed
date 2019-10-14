package handlers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Queue;
import entities.Guest;
import entities.Spawnarea;
import entities.Table;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GuestManager {
	private Queue<Guest> guests;
	private ArrayList<Guest> activeGuests;
	private ArrayList<Guest> guestsToRemove;
	private Spawnarea spawnarea;

	public GuestManager() {
		guests = new Queue<>();
		activeGuests = new ArrayList<>();
		guestsToRemove = new ArrayList<>();
	}

	public void manageGuests(int time, DishManager dishManager) {
		if (!guests.isEmpty()) {
			if (guests.first().getSpawnTime() == time) {
				spawnGuest(guests.first(), dishManager);
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
		guest.calculatePatience(timeElapsed);
		if (guest.getPatience() == 0) {
			guestsToRemove.add(guest);
		} else if (guest.getPatience() == 50) {
				guest.setOrderTime(time);
		} else if (guest.getPatience() == 20) {
				guest.setOrderTime(time);
		}
	}

	private void spawnGuest(Guest guest, DishManager dishManager) {
		if (spawnarea.getFreeTables().size() != 0) {
			Random rndm = new Random();
			int tableID = rndm.nextInt(spawnarea.getFreeTables().size());
			Table table = spawnarea.getFreeTables().get(tableID);
			spawnarea.removeFreeTable(table);
			guest.setPosition(table.getChairPosition()[0], table.getChairPosition()[1]);
			guest.setTable(table);
			table.setGuest(guest);
			dishManager.addToDishQueue(guest.getOrder());
			Gdx.app.log("INFO: ", "Added " + guest.getOrder() + " to dishqueue");
			activeGuests.add(guest);
			Gdx.app.log("INFO: ", "Spawned " + guest);
			guests.removeFirst();
			Gdx.app.log("INFO: ", "Removed Guest from guestlist ");
		}

	}

	public void intializeGuests(List<Guest> guests) {
		for (Guest g : guests) {
			this.guests.addLast(g);
		}
	}

	public void removeActiveGuest(Guest guest) {
		spawnarea.addFreeTable(guest.getTable());
		activeGuests.remove(guest);
		guest.getTable().removeGuest();
		Gdx.app.log("INFO: ", "Guest " + guest + " removed.");
	}

	public void setSpawnarea(Spawnarea spawnarea) {
		this.spawnarea = spawnarea;
	}

	public ArrayList<Guest> getActiveGuests() {
		return activeGuests;
	}

	public Queue<Guest> getGuests() {
		return guests;
	}


}
