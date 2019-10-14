package entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import handlers.AssetsManager;

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
                sprite = new Sprite(AssetsManager.manager.get(AssetsManager.PIZZA, Texture.class));
                break;
            case BURGER:
                sprite = new Sprite(AssetsManager.manager.get(AssetsManager.BURGER, Texture.class));
                break;
            case CHICKEN:
                sprite = new Sprite(AssetsManager.manager.get(AssetsManager.CHICKEN, Texture.class));
                break;
            case POMMES:
                sprite = new Sprite(AssetsManager.manager.get(AssetsManager.POMMES, Texture.class));
                break;
            case FISH:
                sprite = new Sprite(AssetsManager.manager.get(AssetsManager.FISH, Texture.class));
                break;
            default: break;
        }
        sprite.setSize(WORLD_WIDTH / 40, WORLD_HEIGHT / 20);
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
