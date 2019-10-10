package entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import handlers.Assets;

import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

import static com.mygdx.game.MyGdxGame.WORLD_HEIGHT;
import static com.mygdx.game.MyGdxGame.WORLD_WIDTH;

public class Guest {
    private Animation<TextureRegion> idleAnimation;
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

    public Guest(long spawnTime, long happiness, long patience, long wealth) {
        int randomNum = ThreadLocalRandom.current().nextInt(1, 5 + 1);
        TextureAtlas textureAtlas = new TextureAtlas(Gdx.files.internal("character_sprites/Guest"+randomNum+".atlas"));
        idleAnimation = new Animation<TextureRegion>(0.045f, textureAtlas.getRegions(), Animation.PlayMode.LOOP);
        order = Foodtype.getRandomFoodType();
        dish = new Dish(order);
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
        float[] position = {positionX + dish.getSprite().getWidth() + 2, positionY};
        dish.setPosition(position);
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

    public Animation<TextureRegion> getIdleAnimation() {
        return idleAnimation;
    }

    public float[] getPosition() {
        return position;
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
