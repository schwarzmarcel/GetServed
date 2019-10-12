package handlers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.Queue;
import com.badlogic.gdx.utils.Timer;
import com.mygdx.game.MyGdxGame;

import entities.Foodtype;
import entities.Guest;
import entities.Spawnarea;
import entities.Table;

import java.util.*;

import static com.mygdx.game.MyGdxGame.WORLD_HEIGHT;
import static com.mygdx.game.MyGdxGame.WORLD_WIDTH;

public class Guesthandler {
	private Queue<Guest> guests;
	private ArrayList<Guest> activeGuests;
	private ArrayList<Guest> guestsToRemove;
	private Spawnarea spawnarea;
	private MyGdxGame game;

	public Guesthandler(MyGdxGame game) {
		guests = new Queue<>();
		activeGuests = new ArrayList<>();
		guestsToRemove = new ArrayList<>();
		this.game = game;
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
			if (guest.getHappiness() != 1) {
				guest.setHappiness(1);
				guest.setOrderTime(time);
			}
		} else if (timeElapsed >= (guest.getPatience() / 3)) {
			if (guest.getHappiness() != 2) {
				guest.setHappiness(2);
				guest.setOrderTime(time);
			
			}
		}

	}

	private void spawnGuest(Guest guest, Dishhandler dishhandler) {
		if (spawnarea.getFreeTables().size() != 0) {
			Random rndm = new Random();
			int tableID = rndm.nextInt(spawnarea.getFreeTables().size());
			Table table = spawnarea.getFreeTables().get(tableID);
			spawnarea.removeFreeTable(table);
			guest.setPosition(table.getPosition()[0], table.getPosition()[1]);
			guest.setTable(table);
			table.setGuest(guest);
			dishhandler.addToDishQueue(guest.getOrder());
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
		if (guests.isEmpty() && activeGuests.isEmpty()) {
			Gdx.app.log("INFO: ", "You won! Good job!");
			Timer.instance().clear();
			game.increaseLevel();
			game.showMenu();
		}
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
