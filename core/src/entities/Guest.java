package entities;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import handlers.Assets;

import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

public class Guest {
    private Animation<TextureRegion> idleAnimation;
    private Table table;
    private Dish dish;
    private long spawnTime;
    private long orderTime;
    private long patience;
    private long dynamicPatience;
    private long wealth;
    private Foodtype order;
    private float[] position = new float[2];

    public Guest(long spawnTime, long patience, long wealth) {
        int randomNum = ThreadLocalRandom.current().nextInt(1, 5 + 1);
        TextureAtlas textureAtlas = null;
        switch (randomNum){
        case 1: textureAtlas = Assets.manager.get(Assets.GUEST1, TextureAtlas.class);
        	break;
        case 2: textureAtlas = Assets.manager.get(Assets.GUEST2, TextureAtlas.class);
    		break;
        case 3: textureAtlas = Assets.manager.get(Assets.GUEST3, TextureAtlas.class);
			break;
        case 4: textureAtlas = Assets.manager.get(Assets.GUEST4, TextureAtlas.class);
			break;
        case 5: textureAtlas = Assets.manager.get(Assets.GUEST5, TextureAtlas.class);
        	break;
        default: break;
        }
        idleAnimation = new Animation<TextureRegion>(0.045f, textureAtlas.getRegions(), Animation.PlayMode.LOOP);
        order = Foodtype.getRandomFoodType();
        dish = new Dish(order);
        this.spawnTime = spawnTime;
        this.orderTime = spawnTime;
        this.patience = patience;
        this.wealth = wealth;
    }

    public void receivedWrongDish() {
        dynamicPatience = dynamicPatience - 25;
    }

    public int getTip() {
        long tip = wealth * (dynamicPatience / 100);
        return (int) Math.ceil(tip);
    }

    public void calculatePatience(long timeElapsed) {
        dynamicPatience = (patience - timeElapsed * 5);
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
        return dynamicPatience;
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

	public Foodtype getOrder() {
        return order;
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
                ", patience=" + patience +
                ", wealth=" + wealth +
                ", order=" + order +
                ", position=" + Arrays.toString(position) +
                '}';
    }
}
