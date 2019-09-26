package entities;

import static com.mygdx.game.MyGdxGame.WORLD_HEIGHT;
import static com.mygdx.game.MyGdxGame.WORLD_WIDTH;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.World;

public class Guest {
    private Sprite sprite;
    private Table table;
    private float spawnTime;
    private int happiness;
    private int patience;
	private int wealth;
    private float timeElapsed;
    private Foodtype order;
    private float[] position = new float[2];


    public Guest(float positionX, float positionY, float time) {
        position[0] = positionX;
        position[1] = positionY;
        spawnTime = time;
        patience = 30;
        wealth = 40;
        happiness = 3;
        order = Foodtype.getRandomFoodType();
        Texture texture = new Texture("Chef.png");
        sprite = new Sprite(texture);
        sprite.setSize(WORLD_WIDTH / 32, WORLD_HEIGHT / 18);
        sprite.setPosition(positionX, positionY);
        sprite.setColor(0, 1, 0, 1);
    }

    public Sprite getSprite() {
        return sprite;
    }

    
    public void setColor(Color color) {
    	sprite.setColor(color);
    }

    public float getSpawnTime() {
		return spawnTime;
	}

	public int getPatience() {
		return patience;
	}
    
    public int getTip() {
    	double tip = wealth * ((patience - timeElapsed) / patience);
    	return (int) Math.ceil(tip);
    }

    public int getHappiness() {
        return happiness;
    }

    public void setHappiness(int happiness) {
		this.happiness = happiness;
	}

	public void setTable(Table table) {
        this.table = table;
    }
   

    
    public float getTimeElapsed() {
		return timeElapsed;
	}

	public Foodtype getOrder() {
        return order;
    }
    public void setOrder(Foodtype order) {
        this.order = order;
    }
}
