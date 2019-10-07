package handlers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.Queue;
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
//	private ArrayList<Sprite> currentOrderSymbols;
//	private Map<Guest, Sprite> activeOrders;
//	private Map<Guest, Integer> lastOrder;

	public Guesthandler() {
		guests = new Queue<>();
		activeGuests = new ArrayList<>();
		guestsToRemove = new ArrayList<>();
//		currentOrderSymbols = new ArrayList<>();
//		lastOrder = new HashMap<>();
//		activeOrders = new HashMap<>();
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
		// updateOrderQueue();
	}

	public void updateGuest(Guest guest, int time) {
		long timeElapsed = time - guest.getSpawnTime();
		guest.setTimeElapsed(timeElapsed);
		if (timeElapsed >= guest.getPatience()) {
			guestsToRemove.add(guest);
		} else if (timeElapsed >= (guest.getPatience() / 1.5)) {
			if (guest.getHappiness() != 1) {
				guest.setHappiness(1);
				guest.setColor(Color.RED);
				guest.setOrderTime(time);
			}
		} else if (timeElapsed >= (guest.getPatience() / 3)) {
			if (guest.getHappiness() != 2) {
				guest.setHappiness(2);
				guest.setColor(Color.YELLOW);
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
			Gdx.app.log("INFO: ", "Added " + guest.getOrder() + "to dishqueue");
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
//		currentOrderSymbols.remove(guest.getDish().getSprite());
//		activeOrders.remove(guest);
		activeGuests.remove(guest);
		guest.getTable().removeGuest();
		Gdx.app.log("INFO: ", "Guest " + guest + " removed.");
		if (guests.isEmpty()) {
			Gdx.app.log("INFO: ", "You won! Good job!");
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

//	public ArrayList<Sprite> getCurrentOrderSymbols() {
//		return currentOrderSymbols;
//	}
//
//	private void updateOrderQueue() {
//		int x = 2;
//		// TODO: this is nasty so fix it
//		for (Guest g : activeGuests) {
//			Sprite sprite;
//			Foodtype foodtype = g.getOrder();
//			switch (foodtype) {
//			case BURGER:
//				sprite = new Sprite(Assets.manager.get(Assets.BURGER, Texture.class));
//				break;
//			case PASTA:
//				sprite = new Sprite(Assets.manager.get(Assets.PASTA, Texture.class));
//				break;
//			case PIZZA:
//				sprite = new Sprite(Assets.manager.get(Assets.PIZZA, Texture.class));
//				break;
//			default:
//				throw new IllegalStateException("Unexpected value: " + foodtype);
//			}
//			sprite.setPosition(x, 5);
//			sprite.setSize(WORLD_WIDTH / 32, WORLD_HEIGHT / 18);
//			if (lastOrder.containsKey(g))
//				activeOrders.put(g, sprite);
//			x = x + 5;
//		}
//	}
}
