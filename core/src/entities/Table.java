package entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.*;

import static com.mygdx.game.MyGdxGame.*;

public class Table{
    private Sprite sprite;
    private Box box;

    public Table (World world, float positionX, float positionY){
        //TODO: add proper texture
        Texture texture = new Texture("Chef.png");
        sprite = new Sprite(texture);
        sprite.setSize(WORLD_WIDTH / 16, WORLD_HEIGHT / 9);
        sprite.setPosition(positionX,positionY);
        box = new Box(world,sprite,false);
        box.getBody().getFixtureList().first().setUserData(this);
    }

    public Sprite getSprite() {
        return sprite;
    }

    public Body getBody() {
        return box.getBody();
    }
}
