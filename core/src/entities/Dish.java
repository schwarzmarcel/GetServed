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

    public Dish(Foodtype type){
        this.type = type;
        switch (type) {
            case PIZZA:
                sprite = new Sprite(Assets.manager.get(Assets.PIZZA, Texture.class));
                break;
            case BURGER:
                sprite = new Sprite(Assets.manager.get(Assets.BURGER, Texture.class));
                break;
            case PASTA:
                sprite = new Sprite(Assets.manager.get(Assets.PASTA, Texture.class));
                break;
            case SALAD:
                sprite = new Sprite(Assets.manager.get(Assets.SALAD, Texture.class));
                break;
            case FISH:
                sprite = new Sprite(Assets.manager.get(Assets.FISH, Texture.class));
                break;
            default: break;
        }
        sprite.setSize(WORLD_WIDTH / 32, WORLD_HEIGHT / 18);
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
                "type=" + type +
                '}';
    }
}
