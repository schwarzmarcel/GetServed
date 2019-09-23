package entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.World;

import static com.mygdx.game.MyGdxGame.WORLD_HEIGHT;
import static com.mygdx.game.MyGdxGame.WORLD_WIDTH;

public class Dish {
    private Sprite sprite;
    private boolean wasPickedUp = false;
    private float[] position = new float[2];
    Dish(World world){
        Texture texture = new Texture("testdish.png");
        sprite = new Sprite(texture);
        sprite.setSize(WORLD_WIDTH / 32, WORLD_HEIGHT / 18);
        sprite.setColor(0, 1, 0 , 1);
    }
    public void setPosition(float[] positions){
        position = positions;
        this.sprite.setPosition(position[0],position[1]);
    }
    public float[] getPosition(){
        return position;
    }
    public Sprite getSprite(){
        return sprite;
    }

    public void setWasPickedUp(boolean wasPickedUp) {
        this.wasPickedUp = wasPickedUp;
    }
}
