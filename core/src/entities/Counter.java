package entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.World;

import handlers.Assets;

import java.util.Arrays;

import static com.mygdx.game.MyGdxGame.WORLD_HEIGHT;
import static com.mygdx.game.MyGdxGame.WORLD_WIDTH;

public class Counter {
    private Sprite sprite;
    private float[] positions = new float[2];
    private Dish dish;
    private Dish nextDish;
    private float lastDishTime;
    private int cookSpeed;
    

    public Counter(World world, float positionX, float positionY) {
        this.positions[0] = positionX;
        this.positions[1] = positionY;
        sprite = new Sprite(Assets.manager.get(Assets.COUNTER, Texture.class));
        sprite.setSize(WORLD_WIDTH / 24, WORLD_HEIGHT / 9);
        sprite.setPosition(positionX, positionY);
        Box box = new Box(world, sprite, false);
        box.getBody().getFixtureList().first().setUserData(this);
        dish = null;
        lastDishTime = 0;
        cookSpeed = 0;
    }
    public Sprite getSprite() {
        return sprite;
    }

    public float[] getPosition() {
        return positions;
    }

    public Dish getDish() {
        return dish;
    }

    public void setDish(Dish dish) {
        this.dish = dish;
    }
    
    public float getLastDishTime() {
		return lastDishTime;
	}
	public void setLastDishTime(float lastDishTime) {
		this.lastDishTime = lastDishTime;
	}
	public void removeDish() {
    	dish = null;
    }

    @Override
    public String toString() {
        return "Counter{" +
                ", positions=" + Arrays.toString(positions) +
                ", dish=" + dish +
                '}';
    }
	public Dish getNextDish() {
		return nextDish;
	}
	public void setNextDish(Dish nextDish) {
		this.nextDish = nextDish;
	}
	public int getCookSpeed() {
		return cookSpeed;
	}
	public void setCookSpeed(int cookSpeed) {
		this.cookSpeed = cookSpeed;
	}
}
