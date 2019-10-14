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
	private int rotation;
	private float[] positions = new float[2];
	private float[] displayPos = new float[2];
	private float[] dishPos = new float[2];
	private Dish dish;
	private Dish nextDish;
	private float lastDishTime;
	private int cookSpeed;

	public Counter(World world, float positionX, float positionY, int rotation) {
		this.positions[0] = positionX;
		this.positions[1] = positionY;
		this.rotation = rotation;
		sprite = new Sprite(Assets.manager.get(Assets.COUNTER, Texture.class));
		if(rotation == 0 || rotation == 1) {
			sprite.setSize(WORLD_WIDTH / 24, WORLD_HEIGHT / 9);
		}else {
			sprite.setSize(WORLD_HEIGHT / 9, WORLD_WIDTH / 24);
		}
		sprite.setPosition(positionX, positionY);
		Box box = new Box(world, sprite, false);
		box.getBody().getFixtureList().first().setUserData(this);
		dish = new Dish(Foodtype.getRandomFoodType());
		float[] dpos = { positions[0] + (sprite.getWidth() - dish.getSprite().getWidth()) / 2,
				positions[1] + (sprite.getHeight() - dish.getSprite().getHeight()) / 2 };
		dishPos = dpos;
		if (rotation == 0) {
			float[] displpos = { positions[0] - sprite.getWidth(),
					positions[1] + (sprite.getHeight() - dish.getSprite().getHeight()) / 2 };
			displayPos = displpos;
		} else if (rotation == 1) {
			float[] displpos = { positions[0] + sprite.getWidth() + 1,
					positions[1] + (sprite.getHeight() - dish.getSprite().getHeight()) / 2 };
			displayPos = displpos;
		} else if (rotation == 2) {
			float[] displpos = { positions[0] + sprite.getWidth() / 2 - dish.getSprite().getWidth() / 2,
					positions[1] + sprite.getHeight() + 3};
			displayPos = displpos;
		} else if (rotation == 3) {
			float[] displpos = { positions[0] + sprite.getWidth() / 2 - dish.getSprite().getWidth() / 2,
					positions[1] - dish.getSprite().getHeight() - 3};
			displayPos = displpos;
		}
		
		dish = null;
		lastDishTime = 0;
		cookSpeed = 1;
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
		return "Counter{" + ", positions=" + Arrays.toString(positions) + ", dish=" + dish + '}';
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

	public float[] getDishPos() {
		return dishPos;
	}

	public float[] getDisplayPos() {
		return displayPos;
	}

	public int getRotation() {
		return rotation;
	}
}
