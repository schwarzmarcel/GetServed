package handlers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Timer;
import entities.Gamelevel;
import entities.Spawnarea;
import entities.Waiter;
import exceptions.InputNotValidException;

import static com.mygdx.game.MyGdxGame.WORLD_HEIGHT;
import static com.mygdx.game.MyGdxGame.WORLD_WIDTH;

public class LevelManager {
    private Dishhandler dishhandler;
    private GuestManager guestManager;
    private int time;
    private int money;
    private World world;
    private Waiter waiter;
    private Spawnarea gameField;
    private Gamelevel level;
    private boolean levelOver = false;
    private String levelName;

    /**
     * this object handles the flow of the game structure for each level
     *
     * @param levelName the name of the level e.g. level 1 to be handled
     */
    public LevelManager(String levelName) {
        this.levelName = levelName;
        dishhandler = new Dishhandler();
        guestManager = new GuestManager();
    }

    /**
     * This method initializes and builds the level
     */
    public void initializeLevel() {
        world = new World(new Vector2(0, 0), true);
        JsonLevelReader reader = new JsonLevelReader();
        level = reader.generateLevel(levelName);
        level.generateWalls(world);
        money = level.getMoney();
        initializeField();
        intializeWaiter();
        initializeTimer();
        guestManager.intializeGuests(level.getGuests());
        dishhandler.initializeDishhandler(waiter, gameField.getCounters());
        dishhandler.initializeDishQueue(guestManager.getGuests().first().getOrder());
    }

    /**
     * this method initializes the waiter
     */
    private void intializeWaiter() {
        waiter = new Waiter(world, WORLD_WIDTH / 2, WORLD_HEIGHT / 2);
        Gdx.app.log("INFO: ", "Waiter created");
    }

    /**
     * this method initializes the basic game field
     */
    private void initializeField() {
        Gdx.app.log("INFO: ", "Begin drawing field");
        gameField = new Spawnarea();
        guestManager.setSpawnarea(gameField);
        try {
            gameField.initializeTables(level.getGridpositionList(), world);
        } catch (InputNotValidException e) {
            Gdx.app.log("ERROR: ", "", e);
        }
        Gdx.app.log("INFO: ", "Finished drawing field");

    }

    /**
     * this method starts the timer to keep track of the game progress
     */
    private void initializeTimer() {
        Gdx.app.log("INFO: ", "Timer was started");
        time = 0;
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                time++;
                if (money > 1) {
                    money = money - 2;
                } else if (money == 1) {
                    money = 0;
                }
                if (money == 0) {
                    Gdx.app.log("INFO: ", "The Game");
                    Timer.instance().clear();
                    levelOver = true;
                }
            }
        }, 0, 1);
    }

    /**
     * this method constantly updates the entire level as the game progresses over time
     */
    public void updateLevel() {
        guestManager.manageGuests(time, dishhandler);
        dishhandler.updateDishes(time, guestManager.getActiveGuests());
        dishhandler.trashBinHandler(waiter);
        if (guestManager.getActiveGuests().isEmpty() && guestManager.getGuests().isEmpty()) {
            Gdx.app.log("INFO: ", "Level was successfully finished");
            Timer.instance().clear();
            levelOver = true;
        }
    }


    /**
     * ------------------
     * Setters and Getters
     */
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

    public GuestManager getGuestManager() {
        return guestManager;
    }

    public Waiter getWaiter() {
        return waiter;
    }

    public World getWorld() {
        return world;
    }

    public Spawnarea getGameField() {
        return gameField;
    }

    public boolean isLevelOver() {
        return levelOver;
    }
}
