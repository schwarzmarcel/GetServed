package entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

import handlers.Assets;

import static com.mygdx.game.MyGdxGame.WORLD_HEIGHT;
import static com.mygdx.game.MyGdxGame.WORLD_WIDTH;

public class Counter {
    private Sprite sprite;
    private float[] positions = new float[2];
    private Dish dish;

    public Counter(World world, float positionX, float positionY) {
        this.positions[0] = positionX;
        this.positions[1] = positionY;
        sprite = new Sprite(Assets.manager.get(Assets.counter, Texture.class));
        sprite.setSize(WORLD_WIDTH / 32, WORLD_HEIGHT / 18);
        sprite.setPosition(positionX, positionY);
        sprite.setColor(0, 1, 0, 1);
        Box box = new Box(world, sprite, false);
        box.getBody().getFixtureList().first().setUserData(this);
        dish = null;
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
    
    public void removeDish() {
    	dish = null;
    }
}
