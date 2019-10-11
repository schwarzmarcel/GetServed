
package entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import handlers.Assets;
import interfaces.Moveable;

import static com.mygdx.game.MyGdxGame.WORLD_HEIGHT;
import static com.mygdx.game.MyGdxGame.WORLD_WIDTH;

public class Waiter implements Moveable {
    private Animation<TextureRegion> runningAnimation;
    private Animation<TextureRegion> idleAnimation;
    private Sprite sprite;
    private Box box;
    private Dish dish;
    private String orientation = "right";

    public Waiter(World world, float positionX, float positionY) {
        TextureAtlas textureAtlas = Assets.manager.get(Assets.WAITERWALKING, TextureAtlas.class);
    	runningAnimation = new Animation<TextureRegion>(0.015f, textureAtlas.getRegions(), Animation.PlayMode.LOOP);
        textureAtlas = Assets.manager.get(Assets.WAITERIDLE, TextureAtlas.class);
        sprite = new Sprite(textureAtlas.findRegion("Idle"));
        sprite.setSize(WORLD_WIDTH / 32, WORLD_HEIGHT / 16);
        sprite.setPosition(positionX, positionY);
        idleAnimation = new Animation<TextureRegion>(0.045f, textureAtlas.getRegions(), Animation.PlayMode.LOOP);
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
            orientation = "right";
            velX = speed;
        } else if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            velY = (speed * -1);
        } else if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            orientation = "left";
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

    public Animation<TextureRegion> getRunningAnimation() {
        return runningAnimation;
    }

    public String getOrientation() {
        return orientation;
    }

    public Animation<TextureRegion> getIdleAnimation() {
        return idleAnimation;
    }

    public void setIdleAnimation(Animation<TextureRegion> idleAnimation) {
        this.idleAnimation = idleAnimation;
    }

    @Override
    public String toString() {
        return "Waiter{" +
                "box=" + box +
                ", dish=" + dish +
                '}';
    }
}
