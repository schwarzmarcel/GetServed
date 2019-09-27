package entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

import handlers.Assets;

import static com.mygdx.game.MyGdxGame.WORLD_HEIGHT;
import static com.mygdx.game.MyGdxGame.WORLD_WIDTH;

public class Dish {
    private Sprite sprite;
    public Foodtype type;
    private float[] position;

    public Dish(Foodtype type, float[] positions){
        this.type = type;
        this.position = positions;
        switch (type) {
            case PIZZA:
                sprite = new Sprite(Assets.manager.get(Assets.pizza, Texture.class));
                break;
            case BURGER:
                sprite = new Sprite(Assets.manager.get(Assets.burger, Texture.class));
                break;
            case PASTA:
                sprite = new Sprite(Assets.manager.get(Assets.pasta, Texture.class));
                break;
            default: break;
        }
        sprite.setSize(WORLD_WIDTH / 32, WORLD_HEIGHT / 18);
        sprite.setPosition(position[0],position[1]);
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

    @Override
    public String toString() {
        return "Dish{" +
                ", type=" + type +
                '}';
    }
}
