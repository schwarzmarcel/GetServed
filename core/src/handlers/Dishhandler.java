package handlers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.utils.Queue;
import entities.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.mygdx.game.MyGdxGame.PIXELS_TO_METERS;

public class Dishhandler {

	private Queue<Foodtype> dishQueue;
	private ArrayList<Dish> dishes;
	private List<Counter> counters;
	private Waiter waiter;

	public Dishhandler() {
		dishQueue = new Queue<Foodtype>();
		dishes = new ArrayList<Dish>();

	}
	
	public void initializeDishhandler(Waiter waiter, List<Counter> counters) {
		this.waiter = waiter;
		this.counters = counters;
	}

	public void updateDishes(int time, ArrayList<Guest> activeGuests) {
		if (waiter.getDish() != null) {
			waiter.getDish().getSprite().setPosition(
					(waiter.getBody().getPosition().x * PIXELS_TO_METERS) - waiter.getSprite().getWidth() / 2,
					(waiter.getBody().getPosition().y * PIXELS_TO_METERS) - waiter.getSprite().getHeight() / 2);
		}

		for (Counter c : counters) {

			// for initialization
			if (c.getNextDish() == null) {
				Dish init;
				if (dishQueue.notEmpty()) {
					init = new Dish(dishQueue.removeFirst());
				} else {
					init = new Dish(Foodtype.getRandomFoodType());
				}
				c.setNextDish(init);
				c.setLastDishTime(time);
				dishes.add(init);
				init.setPosition(c.getDisplayPos());
			}

			// makes dishes spawn faster when counter is empty
			if (c.getDish() == null)
				c.setCookSpeed(2);

			if ((time - c.getLastDishTime()) >= (6 / c.getCookSpeed()) + 1) {
				Dish nextDish = null;
				if (dishQueue.isEmpty()) {
					for (Guest g : activeGuests) {
						boolean dishAvailable = false;
						for (Dish d : dishes) {
							if (d.type == g.getOrder()) {
								dishAvailable = true;
								break;
							}
						}
						if (!dishAvailable) {
							nextDish = new Dish(g.getOrder());
							break;
						}
					}
					if (nextDish == null)
						nextDish = new Dish(Foodtype.getRandomFoodType());
				} else {
					nextDish = new Dish(dishQueue.removeFirst());
				}
				c.setLastDishTime(time);
				if (c.getDish() != null) {
					dishes.remove(c.getDish());
				}
				dishes.add(nextDish);
				nextDish.setPosition(c.getDisplayPos());
				c.getNextDish().setPosition(c.getDishPos());
				c.setDish(c.getNextDish());
				c.setNextDish(nextDish);
				c.setCookSpeed(1);
			}
		}

	}

	public void addToDishQueue(Foodtype type) {
		dishQueue.addLast(type);
	}

	public void trashBinHandler(Waiter waiter) {
		if (Gdx.input.isKeyJustPressed(Input.Keys.D)) {
			if (waiter.getDish() != null) {
				dishes.remove(waiter.getDish());
				Gdx.app.log("INFO: ", "Dish has been removed by Button Press");
			}
			waiter.setDish(null);
			Gdx.app.log("INFO: ", "Waiter no longer carries a dish");
		}
	}

	public void removeDish(Dish dish) {
		Gdx.app.log("INFO: ", "Dish " + dish + " has been served and removed");
		dishes.remove(dish);
	}

	public ArrayList<Dish> getDishes() {
		return dishes;
	}
}
