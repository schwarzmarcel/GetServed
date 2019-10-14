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

/**
 * The LevelManager initializes a level, builds the world for the level
 * and is responsible for all changes during the game
 */
public class LevelManager {
    private DishManager dishManager;
    private GuestManager guestManager;
    private int time;
    private World world;
    private Waiter waiter;
    private Spawnarea gameField;
    private Gamelevel level;
    private boolean levelOver = false;
    private String levelName;

    /**
     * this object handles the flow of the game structure for each level
     * @param levelName the name of the level e.g. level 1 to be handled
     */
    public LevelManager(String levelName) {
        this.levelName = levelName;
        dishManager = new DishManager();
        guestManager = new GuestManager();
    }

    /**
     * This method initializes and builds the level
     */
    public void initializeLevel() {
        world = new World(new Vector2(0, 0), true);
        JsonLevelReader reader = new JsonLevelReader();
        level = reader.readLevelConfiguration(levelName);
        initializeGameField();
        intializeWaiter();
        initializeTimer();
        guestManager.intializeGuests(level.getGuests());
        dishManager.initializeDishhandler(waiter, gameField.getCounters());
        dishManager.initializeDishQueue(guestManager.getGuests().first().getOrder());
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
    private void initializeGameField() {
        Gdx.app.log("INFO: ", "Begin drawing field");
        gameField = new Spawnarea();
        guestManager.setSpawnarea(gameField);
        gameField.generateWalls(world);
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
                level.decrementMoney();
            }
        }, 0, 1);
    }

    /**
     * this method constantly updates the entire level as the game progresses over time
     */
    public void updateLevel() {
        guestManager.manageGuests(time, dishManager);
        dishManager.updateDishes(time, guestManager.getActiveGuests());
        dishManager.trashBinHandler(waiter);
        if (level.getMoney() == 0) {
            levelOver = true;
        }
        if (guestManager.getActiveGuests().isEmpty() && guestManager.getGuests().isEmpty()) {
            Gdx.app.log("INFO: ", "Level was successfully finished");
            Timer.instance().clear();
            levelOver = true;
        }
    }

    @Override
    public String toString() {
        return "LevelManager{" +
                "waiter=" + waiter +
                ", levelOver=" + levelOver +
                ", levelName='" + levelName + '\'' +
                '}';
    }

    /**
     * ------------------
     * Setters and Getters
     */

    public DishManager getDishManager() {
        return dishManager;
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

    public Gamelevel getLevel() {
        return level;
    }

}
