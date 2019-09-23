package entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.*;

import static com.mygdx.game.MyGdxGame.*;

public class Table {
    private Sprite sprite;
    private Box box;
    private float[] positions = new float[2];

    public Table(World world, float positionX, float positionY) {
        this.positions[0] = positionX;
        this.positions[1] = positionY;
        Texture texture = new Texture("Chef.png");
        sprite = new Sprite(texture);
        sprite.setSize(WORLD_WIDTH / 32, WORLD_HEIGHT / 18);
        sprite.setPosition(positionX, positionY);
        sprite.setColor(0, 1, 0, 1);
        box = new Box(world, sprite, false);
        box.getBody().getFixtureList().first().setUserData(this);
    }

    public Sprite getSprite() {
        return sprite;
    }

    public Body getBody() {
        return box.getBody();
    }

    public float[] getPosition() {
        return positions;
    }

}
