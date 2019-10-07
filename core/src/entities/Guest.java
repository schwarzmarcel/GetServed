package entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import handlers.Assets;

import java.util.Arrays;

import static com.mygdx.game.MyGdxGame.WORLD_HEIGHT;
import static com.mygdx.game.MyGdxGame.WORLD_WIDTH;

public class Guest {
    private Sprite sprite;
    private Table table;
    private Dish dish;
    private long spawnTime;
    private long orderTime;
    private long happiness;
    private long patience;
    private long wealth;
    private float timeElapsed;
    private Foodtype order;
    private float[] position = new float[2];
    private boolean wantsToOrder = true;

    public Guest(long spawnTime, long happiness, long patience, long wealth) {
        order = Foodtype.getRandomFoodType();
        dish = new Dish(order);
        sprite = new Sprite(Assets.manager.get(Assets.GUEST, Texture.class));
        sprite.setSize(WORLD_WIDTH / 32, WORLD_HEIGHT / 18);
        sprite.setColor(0, 1, 0, 1);
        this.spawnTime = spawnTime;
        this.orderTime = spawnTime;
        this.happiness = happiness;
        this.patience = patience;
        this.wealth = wealth;
        timeElapsed = 0;
    }

    public void setPosition(float positionX, float positionY) {
    	position[0] = positionX;
        position[1] = positionY;
        sprite.setPosition(positionX, positionY);
        float[] position = {positionX + dish.getSprite().getWidth() + 2, positionY};
        dish.setPosition(position);
    }

    public Sprite getSprite() {
        return sprite;
    }

    public void setColor(Color color) {
    	sprite.setColor(color);
    }

    public long getSpawnTime() {
		return spawnTime;
	}

    public long getOrderTime() {
		return orderTime;
	}

	public void setOrderTime(long orderTime) {
		this.orderTime = orderTime;
	}

	public long getPatience() {
		return patience;
	}

    public int getTip() {
    	double tip = (wealth * ((patience - timeElapsed) / patience));
    	return (int) Math.ceil(tip);
    }

    public void setHappiness(int happiness) {
		this.happiness = happiness;
	}

	public void setTable(Table table) {
        this.table = table;
    }

    public Table getTable() {
		return table;
	}

    public Dish getDish() {
    	return dish;
    }

    public void setTimeElapsed(float time) {
    	timeElapsed = time;
    }

	public Foodtype getOrder() {
        return order;
    }

    public long getHappiness() {
        return happiness;
    }
    public void setWantsToOrder(boolean wantsToOrder) {
        this.wantsToOrder = wantsToOrder;
    }

    public boolean isWantsToOrder() {
        return wantsToOrder;
    }

    @Override
    public String toString() {
        return "Guest{" +
                "spawnTime=" + spawnTime +
                ", happiness=" + happiness +
                ", patience=" + patience +
                ", wealth=" + wealth +
                ", order=" + order +
                ", position=" + Arrays.toString(position) +
                '}';
    }
}
