package entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

import handlers.Assets;

import java.util.Arrays;

import static com.mygdx.game.MyGdxGame.WORLD_HEIGHT;
import static com.mygdx.game.MyGdxGame.WORLD_WIDTH;

public class Table {
    private Sprite sprite;
    private Box box;
    private float[] positions = new float[2];
    private Guest guest;

    public Table(World world, float positionX, float positionY) {
        this.positions[0] = positionX;
        this.positions[1] = positionY;
        sprite = new Sprite(Assets.manager.get(Assets.table, Texture.class));
        sprite.setSize(WORLD_WIDTH / 24, WORLD_HEIGHT / 16);
        sprite.setPosition(positionX, positionY);
        box = new Box(world, sprite, false);
        box.getBody().getFixtureList().first().setUserData(this);
        guest = null;
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

    public Guest getGuest() {
        return guest;
    }
    
    public void setGuest(Guest guest) {
        this.guest = guest;
    }
    
    public void removeGuest() {
    	this.guest = null;
    }

    @Override
    public String toString() {
        return "Table{" +
                "positions=" + Arrays.toString(positions) +
                ", guest=" + guest +
                '}';
    }
}
