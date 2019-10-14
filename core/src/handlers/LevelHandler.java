package handlers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Timer;
import entities.GameField;
import entities.Gamelevel;
import entities.Waiter;
import exceptions.InputNotValidException;

import static com.mygdx.game.MyGdxGame.WORLD_HEIGHT;
import static com.mygdx.game.MyGdxGame.WORLD_WIDTH;

/**
 * The LevelHandler initializes a level, builds the world for the level
 * and is responsible for all changes during the game
 */
public class LevelHandler {
    private DishHandler dishHandler;
    private GuestHandler guestHandler;
    private int time;
    private World world;
    private Waiter waiter;
    private GameField gameField;
    private Gamelevel level;
    private boolean levelOver = false;
    private String levelName;

    /**
     * this object handles the flow of the game structure for each level
     *
     * @param levelName the name of the level e.g. level 1 to be handled
     */
    public LevelHandler(String levelName) {
        this.levelName = levelName;
        dishHandler = new DishHandler();
        guestHandler = new GuestHandler();
    }

    /**
     * This method initializes and builds the level
     */
    public void initializeLevel() {
        world = new World(new Vector2(0, 0), true);
        JsonLevelReader reader = new JsonLevelReader();
        level = reader.readLevelConfiguration(levelName);
        initializeGameField();
        initializeWaiter();
        initializeTimer();
        guestHandler.initializeGuests(level.getGuests());
        dishHandler.initializeDishManager(waiter, gameField.getCounters());
        dishHandler.initializeDishQueue(guestHandler.getGuestQueue().first().getOrder());
    }

    /**
     * this method initializes the waiter
     */
    private void initializeWaiter() {
        waiter = new Waiter(world, WORLD_WIDTH / 2, WORLD_HEIGHT / 2);
        Gdx.app.log("INFO: ", "Waiter created");
    }

    /**
     * this method initializes the basic game field
     */
    private void initializeGameField() {
        Gdx.app.log("INFO: ", "Begin drawing field");
        gameField = new GameField();
        guestHandler.setGameField(gameField);
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
        guestHandler.manageGuests(time, dishHandler);
        dishHandler.updateDishes(time, guestHandler.getActiveGuests());
        dishHandler.trashBinHandler(waiter);
        if (level.getMoney() == 0) {
            levelOver = true;
        }
        if (guestHandler.getActiveGuests().isEmpty() && guestHandler.getGuestQueue().isEmpty()) {
            Gdx.app.log("INFO: ", "Level was successfully finished");
            Timer.instance().clear();
            levelOver = true;
        }
    }

    @Override
    public String toString() {
        return "LevelHandler{" +
                "waiter=" + waiter +
                ", levelOver=" + levelOver +
                ", levelName='" + levelName + '\'' +
                '}';
    }

    /*
     * ------------------
     * Setters and Getters
     */

    public DishHandler getDishHandler() {
        return dishHandler;
    }

    public int getTime() {
        return time;
    }

    public GuestHandler getGuestHandler() {
        return guestHandler;
    }

    public Waiter getWaiter() {
        return waiter;
    }

    public World getWorld() {
        return world;
    }

    public GameField getGameField() {
        return gameField;
    }

    public boolean isLevelOver() {
        return levelOver;
    }

    public Gamelevel getLevel() {
        return level;
    }

}
