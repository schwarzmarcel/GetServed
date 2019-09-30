package handlers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Timer;
import entities.Gamelevel;
import entities.Spawnarea;
import entities.Waiter;
import entities.Walls;
import exceptions.InputNotValidException;

import static com.mygdx.game.MyGdxGame.WORLD_HEIGHT;
import static com.mygdx.game.MyGdxGame.WORLD_WIDTH;

public class LevelHandler {
    private Dishhandler dishhandler;
    private Guesthandler guesthandler;
    private int time;
    private int money;
    private World world;
    private Waiter waiter;
    private Spawnarea spawnarea;
    private JsonLevelReader reader;
    private Gamelevel level;

    public LevelHandler() {
        dishhandler = new Dishhandler();
        guesthandler = new Guesthandler();
        world = new World(new Vector2(0, 0), true);
        Walls walls = new Walls(world);
        money = 25;
        reader = new JsonLevelReader();
        level = reader.generateLevel("../../level1");
    }

    public void initializeLevel() {
        drawField();
        intializeWaiter();
        startTimer();
        guesthandler.intializeGuests(level.getGuests());
    }

    private void intializeWaiter() {
        waiter = new Waiter(world, WORLD_WIDTH / 2, WORLD_HEIGHT / 2);
        Gdx.app.log("INFO: ", "Waiter created");
    }

    private void drawField() {
        spawnarea = new Spawnarea();
        guesthandler.setSpawnarea(spawnarea);
        try {
            spawnarea.initializeTables(level.getGridpositionList(), world);
        } catch (InputNotValidException e) {
            Gdx.app.log("ERROR: ", "", e);
        }
    }

    private void startTimer() {
        Gdx.app.log("INFO: ", "Timer was started");
        time = 0;
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                time++;
                if (money > 1) {
                    money = money - 2;
                }
                else if (money == 1) {
                	money = 0;
                }
                if(money == 0) 
                	Gdx.app.log("INFO: ", "You lost because you ran out of money :(");
            }
        }, 0, 1);
    }

    public void updateLevel() {
        guesthandler.handleGuests(time, dishhandler);
        dishhandler.updateDishes(waiter, spawnarea.getCounters(), time, guesthandler.getActiveGuests());
        dishhandler.trashBinHandler(waiter);
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public Dishhandler getDishhandler() {
        return dishhandler;
    }

    public int getTime() {
        return time;
    }

    public Guesthandler getGuesthandler() {
        return guesthandler;
    }

    public Waiter getWaiter() {
        return waiter;
    }

    public World getWorld() {
        return world;
    }
}
