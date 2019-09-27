package handlers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.utils.Queue;
import entities.*;

import java.util.ArrayList;
import java.util.List;

import static com.mygdx.game.MyGdxGame.PIXELS_TO_METERS;

public class Dishhandler {
    private int dishTimer;
    private Queue<Foodtype> dishQueue;
    private ArrayList<Dish> dishes;


    public Dishhandler() {
        dishQueue = new Queue<Foodtype>();
        dishes = new ArrayList<Dish>();
    }

    public void updateDishes(Waiter waiter, List<Counter> counters, int time, ArrayList<Guest> activeGuests) {
        if (waiter.getDish() != null) {
            waiter.getDish().getSprite().setPosition(
                    (waiter.getBody().getPosition().x * PIXELS_TO_METERS) - waiter.getSprite().getWidth() / 2,
                    (waiter.getBody().getPosition().y * PIXELS_TO_METERS) - waiter.getSprite().getHeight() / 2
            );
        }
        for (Counter c : counters
        ) {
            if (dishTimer + 2 < time) {
                boolean neededDishAvailable = false;
                for (Dish d : dishes
                ) {
                    for (Guest g : activeGuests
                    ) {
                        if (d.type == g.getOrder()) neededDishAvailable = true;
                    }
                }
                if (c.getDish() == null && !dishQueue.isEmpty()) {
                    Dish tempDish = new Dish(dishQueue.removeFirst(), c.getPosition());
                    c.setDish(tempDish);
                    dishes.add(tempDish);
                }
                if (dishQueue.isEmpty() && c.getDish() == null && neededDishAvailable) {
                    Dish tempDish = new Dish(Foodtype.getRandomFoodType(), c.getPosition());
                    c.setDish(tempDish);
                    dishes.add(tempDish);
                }
            }
        }
    }

    public void addToDishQueue(Foodtype type) {
        dishQueue.addLast(type);
    }

    public void trashBinHandler(Waiter waiter) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.X)) {
            if (waiter.getDish() != null) {
                dishes.remove(waiter.getDish());
            }
            waiter.setDish(null);
        }
    }

    public void removeDish(Dish dish) {
        dishes.remove(dish);
    }

    public void updateDishTimer(int t) {
        this.dishTimer = t;
    }

    public ArrayList<Dish> getDishes() {
        return dishes;
    }
}

