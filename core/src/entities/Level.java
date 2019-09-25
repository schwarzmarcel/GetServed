package entities;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Timer;

import static com.mygdx.game.MyGdxGame.*;

public class Level {
    private int numberOfTables;
    private int numberOfGuests;
    private int numberOfCounters;
    private int numberOfDishes;
    private float time;
    private int money;
    private Table[] tables;
    private Guest[] guests;
    private Counter[] counters;
    private Dish[] dishes;
    private World world;
    private Waiter waiter;
    private boolean checkIfInstanceAlreadyCreated;

    public Level(int numberOfTables, int numberOfGuests, int numberOfCounters, int numberOfDishes) {
        this.numberOfTables = numberOfTables;
        this.numberOfGuests = numberOfGuests;
        this.numberOfCounters = numberOfCounters;
        this.numberOfDishes = numberOfDishes;
        world = new World(new Vector2(0, 0), true);
        Walls walls = new Walls(world);
        money = 60;

    }

    public void initializeLevel() {
        tables = new Table[numberOfTables];
        guests = new Guest[numberOfGuests];
        counters = new Counter[numberOfCounters];
        dishes = new Dish[numberOfDishes];
        intializeTables();
        intializeGuests();
        intializeWaiter();
        intializeCounters();
        startTimer();
    }

    private void intializeCounters() {
        float positionX = WORLD_WIDTH / 7;
        float positionY = WORLD_HEIGHT / 2;
        for (int i = 0; i < numberOfCounters; i++) {
            //positionY = (float) (positionY + (0.2*WORLD_HEIGHT));
            counters[i] = new Counter(world, positionX, positionY);
        }
    }

    private void intializeWaiter() {
        waiter = new Waiter(world, WORLD_WIDTH / 2, WORLD_HEIGHT / 2);
    }

    private void intializeGuests() {
        float positionX = 0;
        float positionY = 0;
        int currentTable = 0;
        for (int i = 0; i < numberOfGuests; i++) {
            if (currentTable < tables.length) {
                positionX = tables[currentTable].getPosition()[0];
                positionY = tables[currentTable].getPosition()[1];
                positionX = positionX + (WORLD_WIDTH / 30);
                guests[i] = new Guest(world, positionX, positionY, time);
                guests[i].setTable(tables[currentTable]);
                System.out.println(positionX);
                System.out.println(tables[currentTable].getPosition()[0]);

            } else {
                currentTable = 0;
            }
            currentTable++;
        }
    }

    private void intializeTables() {
        float positionX = (float) (WORLD_WIDTH / 1.5 + (WORLD_WIDTH / 4));
        float positionY = WORLD_WIDTH / 10;
        for (int i = 0; i < numberOfTables; i++) {
            positionY = (float) (positionY + (0.15 * WORLD_HEIGHT));
            positionX = positionX - (10);
            tables[i] = new Table(world, positionX, positionY);
        }
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

    public Table[] getTables() {
        return tables;
    }

    public Counter[] getCounters() {
        return counters;
    }

    public World getWorld() {
        return world;
    }

    public Guest[] getGuests() {
        return guests;
    }

    public Dish[] getDishes() {
        return dishes;
    }

    public void updateLevel() {
        if ((time % 10) == 0) {
            if(!checkIfInstanceAlreadyCreated) {
                checkIfInstanceAlreadyCreated = true;
                dishes[0] = new Dish();
            }
            dishes[0].setPosition(counters[0].getPosition());
            counters[0].setDish(dishes[0]);
        }
        if (waiter.getDish() != null) {
            waiter.getDish().getSprite().setPosition(
                    (waiter.getBody().getPosition().x * PIXELS_TO_METERS) - waiter.getSprite().getWidth() / 2,
                    (waiter.getBody().getPosition().y * PIXELS_TO_METERS) - waiter.getSprite().getHeight() / 2
            );
        }
        for (Guest g : guests
        ) {
            g.update(time);
        }
    }

	public int getMoney() {
		return money;
	}

	public void setMoney(int money) {
		this.money = money;
	}
}
