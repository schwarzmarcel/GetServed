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
            case CHICKEN:
                sprite = new Sprite(Assets.manager.get(Assets.CHICKEN, Texture.class));
                break;
            case POMMES:
                sprite = new Sprite(Assets.manager.get(Assets.POMMES, Texture.class));
                break;
            case FISH:
                sprite = new Sprite(Assets.manager.get(Assets.FISH, Texture.class));
                break;
            default: break;
        }
        sprite.setSize(WORLD_WIDTH / 42, WORLD_HEIGHT / 22);
    }
    public void setPosition(float[] positions){
        position = positions;
        this.sprite.setPosition(position[0],position[1]);
    }
    public Sprite getSprite(){
        return sprite;
    }

    public void setSpriteSize(int width, int height) {
        sprite.setSize(WORLD_WIDTH / width, WORLD_HEIGHT / height);
    }
    @Override
    public String toString() {
        return "Dish{" +
                "type=" + type +
                '}';
    }
}
