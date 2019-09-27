package handlers;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Timer;
import entities.Gridposition;
import entities.Spawnarea;
import entities.Waiter;
import entities.Walls;

import static com.mygdx.game.MyGdxGame.WORLD_HEIGHT;
import static com.mygdx.game.MyGdxGame.WORLD_WIDTH;

public class LevelHandler {
    private Dishhandler dishhandler;
    private Guesthandler guesthandler;
    private int numberOfDishes;
    private int time;
    private int money;
    private World world;
    private Waiter waiter;
    private Spawnarea spawnarea;

    public LevelHandler(int numberOfDishes) {
        dishhandler = new Dishhandler();
        guesthandler = new Guesthandler();
        this.numberOfDishes = numberOfDishes;
        world = new World(new Vector2(0, 0), true);
        Walls walls = new Walls(world);
        money = 60;
    }

    public void initializeLevel() {
        drawField();
        intializeWaiter();
        startTimer();
        guesthandler.intializeGuests();
    }

    private void intializeWaiter() {
        waiter = new Waiter(world, WORLD_WIDTH / 2, WORLD_HEIGHT / 2);
    }

    private void drawField() {
        spawnarea = new Spawnarea();
        spawnarea.printGridDimensions();
        Gridposition pos1 = new Gridposition(6, 20, "table");
        Gridposition pos2 = new Gridposition(8, 20, "table");
        Gridposition pos3 = new Gridposition(10, 20, "table");
        Gridposition pos4 = new Gridposition(12, 20, "table");
        Gridposition pos5 = new Gridposition(14, 20, "table");
        Gridposition pos6 = new Gridposition(16, 20, "table");
        Gridposition pos7 = new Gridposition(10, 3, "counter");
        Gridposition pos8 = new Gridposition(5, 3, "counter");
        Gridposition pos9 = new Gridposition(15, 3, "counter");
        Gridposition[] gridpositions = new Gridposition[9];
        gridpositions[0] = pos1;
        gridpositions[1] = pos2;
        gridpositions[2] = pos3;
        gridpositions[3] = pos4;
        gridpositions[4] = pos5;
        gridpositions[5] = pos6;
        gridpositions[6] = pos7;
        gridpositions[7] = pos8;
        gridpositions[8] = pos9;
        spawnarea.initializeTables(gridpositions, world);
    }

    private void startTimer() {
        time = 0;
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                time++;
                if (money > 0)
                    money--;
            }
        }, 0, 1);
    }

    public Waiter getWaiter() {
        return waiter;
    }

    public World getWorld() {
        return world;
    }

    public void updateLevel() {
        guesthandler.handleGuests(time, spawnarea, dishhandler);
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
}
