
package entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.*;
import interfaces.Moveable;


import static com.mygdx.game.MyGdxGame.PIXELS_TO_METERS;
import static com.mygdx.game.MyGdxGame.WORLD_WIDTH;
import static com.mygdx.game.MyGdxGame.WORLD_HEIGHT;;


public class Waiter implements Moveable {
    private TextureAtlas textureAtlas;
    private TextureRegion textureRegion;
    private Sprite sprite;
    private Box box;
    private Dish dish;

    public Waiter(World world, float positionX, float positionY) {
        //TODO: Use a proper Texture!
        /*
        textureAtlas = new TextureAtlas(Gdx.files.internal("spritesheets/avatarsprites.atlas"));
        textureRegion = textureAtlas.findRegion("6_ATTACK");
        sprite = new Sprite(textureRegion);
        */

        Texture texture = new Texture("Chef.png");
        sprite = new Sprite(texture);
        sprite.setSize(WORLD_WIDTH / 32, WORLD_HEIGHT / 18);
        sprite.setPosition(positionX, positionY);

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        box = new Box(world, sprite, true);

        box.getBody().getFixtureList().first().setUserData(this);
        dish = null;
    }

    public void move(float speed) {
        float velX = 0, velY = 0;
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            velY = speed;
        } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            velX = speed;
        } else if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            velY = (speed * -1);
        } else if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            velX = (speed * -1);
        }
        box.getBody().setLinearVelocity(velX, velY);
    }

    public Sprite getSprite() {
        return sprite;
    }

    public Body getBody() {
        return box.getBody();
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
