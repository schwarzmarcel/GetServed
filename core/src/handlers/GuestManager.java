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
	/**
	 * guestQueue: this queue contains all the guests that will be spawned during the game in the correct order
	 * activeGuests: this list contains all the guests at any time that are currently on the field
	 * guestsToRemove: this list contains all the guests at any time that need to be removed from the field
	 */
	private Queue<Guest> guestQueue;
	private ArrayList<Guest> activeGuests;
	private ArrayList<Guest> guestsToRemove;
	private Spawnarea spawnarea;

	GuestManager() {
		guestQueue = new Queue<>();
		activeGuests = new ArrayList<>();
		guestsToRemove = new ArrayList<>();
	}

	/**
	 * This method will directly manages the guests during the game
	 *
	 * @param time        the current time since the level started
	 * @param dishManager the object that manages the dishes; used to create the dishes when guests spawn
	 */
	void manageGuests(int time, DishManager dishManager) {
		if (!guestQueue.isEmpty()) {
			if (guestQueue.first().getSpawnTime() == time) {
				spawnGuest(guestQueue.first(), dishManager);
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

	/**
	 * this method updates a guests state
	 *
	 * @param guest the guest to be updated
	 * @param time  the current time since the level started
	 */
	private void updateGuest(Guest guest, int time) {
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

	/**
	 * this method spawns the guest on the on the field
	 * @param guest the guest to be spawned; all guest objects are already created prior to spawning them
	 * @param dishManager the dishManager to add the order of the new guest to the queue
	 */
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
			guestQueue.removeFirst();
			Gdx.app.log("INFO: ", "Removed Guest from guestlist ");
		}

	}

	/**
	 * this method initializes the guests for the game by putting them into the queue
	 * @param guests the list of guests
	 */
	void initializeGuests(List<Guest> guests) {
		for (Guest g : guests) {
			this.guestQueue.addLast(g);
		}
	}

	/**
	 * this method removes an active guest from the field
	 * @param guest the guest to be removed
	 */
	public void removeActiveGuest(Guest guest) {
		spawnarea.addFreeTable(guest.getTable());
		activeGuests.remove(guest);
		guest.getTable().removeGuest();
		Gdx.app.log("INFO: ", "Guest " + guest + " removed.");
	}

	/**
	 * -------------------
	 * Getters and Setters
	 */

	public void setSpawnarea(Spawnarea spawnarea) {
		this.spawnarea = spawnarea;
	}

	public ArrayList<Guest> getActiveGuests() {
		return activeGuests;
	}

	public Queue<Guest> getGuestQueue() {
		return guestQueue;
	}


}
