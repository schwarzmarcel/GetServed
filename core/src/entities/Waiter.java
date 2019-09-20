
package entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.*;


import static com.mygdx.game.MyGdxGame.PIXELS_TO_METERS;
import static com.mygdx.game.MyGdxGame.WORLD_WIDTH;
import static com.mygdx.game.MyGdxGame.WORLD_HEIGHT;;


public class Waiter {
    private TextureAtlas textureAtlas;
    private TextureRegion textureRegion;
    private Sprite sprite;
    private Body body;
    public Waiter(World world, float positionX, float positionY) {
       // textureAtlas = new TextureAtlas(Gdx.files.internal("spritesheets/avatarsprites.atlas"));
       // textureRegion = textureAtlas.findRegion("6_ATTACK");
       // sprite = new Sprite(textureRegion);
    	Texture texture = new Texture("Chef.png");
    	sprite = new Sprite(texture);
    	sprite.setSize(WORLD_WIDTH / 16, WORLD_HEIGHT / 9);
        sprite.setPosition(positionX,positionY);

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;

        bodyDef.position.set((sprite.getX() + sprite.getWidth() / 2) / PIXELS_TO_METERS, (sprite.getY() + sprite.getHeight() / 2) / PIXELS_TO_METERS);
        body = world.createBody(bodyDef);
        body.setUserData(sprite);
        PolygonShape shape = new PolygonShape();
        shape.setAsBox((sprite.getWidth()/2) / PIXELS_TO_METERS,(sprite.getHeight()/2) / PIXELS_TO_METERS);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        body.createFixture(fixtureDef);
        shape.dispose();
    }

    public void move() {
        float velX = 0, velY = 0;
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
        	System.out.println(body.getPosition().x * PIXELS_TO_METERS + "," + body.getPosition().y * PIXELS_TO_METERS);
            velY = 2.0f;
        } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            velX = 2.0f;
        } else if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            velY = -2.0f;
        } else if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            velX = -2.0f;
        }
        body.setLinearVelocity(velX, velY);
    }

    public Sprite getSprite() {
        return sprite;
    }

    public Body getBody() {
        return body;
    }
}
