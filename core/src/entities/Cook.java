package entities;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import handlers.Assets;

import static com.mygdx.game.MyGdxGame.WORLD_HEIGHT;
import static com.mygdx.game.MyGdxGame.WORLD_WIDTH;

public class Cook {
    private Animation<TextureRegion> idleAnimation;
    private float[] position = new float[2];
    private Sprite sprite;
    private Box box;

    public Cook(World world, float[] positions) {
        this.position = positions;
        TextureAtlas textureAtlas = Assets.manager.get(Assets.COOK, TextureAtlas.class);
        idleAnimation = new Animation<TextureRegion>(0.045f, textureAtlas.getRegions(), Animation.PlayMode.LOOP);
        sprite = new Sprite(textureAtlas.findRegion("Idle"));
        sprite.setSize(WORLD_WIDTH / 30, WORLD_HEIGHT / 15);
        sprite.setPosition(positions[0], positions[1]);
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        box = new Box(world, sprite, false, true);
        box.getBody().getFixtureList().first().setUserData(this);
    }

    public Animation<TextureRegion> getIdleAnimation() {
        return idleAnimation;
    }

    public void setIdleAnimation(Animation<TextureRegion> idleAnimation) {
        this.idleAnimation = idleAnimation;
    }

    public float[] getPosition() {
        return position;
    }

    public void setPosition(float[] position) {
        this.position = position;
    }
}
