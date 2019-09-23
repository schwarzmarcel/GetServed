package entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

import static com.mygdx.game.MyGdxGame.WORLD_HEIGHT;
import static com.mygdx.game.MyGdxGame.WORLD_WIDTH;

public class Dish {
    private Sprite sprite;
    public Foodtype type;
    private float[] position = new float[2];

    public Dish(){
        this.type = Foodtype.getRandomFoodType();
        Texture texture;
        switch (type) {
            case PIZZA:
                texture = new Texture("pizza.png");
                sprite = new Sprite(texture);
                break;
            case BURGER:
                texture = new Texture("burger.png");
                sprite = new Sprite(texture);
                break;
            case PASTA:
                texture = new Texture("pasta.png");
                sprite = new Sprite(texture);
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
}
