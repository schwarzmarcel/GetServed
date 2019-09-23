package entities;

import static com.mygdx.game.MyGdxGame.WORLD_HEIGHT;
import static com.mygdx.game.MyGdxGame.WORLD_WIDTH;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

public class Guest {
    private Sprite sprite;
    private Box box;
    private Table table;
    private float spawnTime;
    private float timeElapsed;
    private int happiness;
    private int patience;
    private int wealth;
    private boolean hasBeenServed = false;


    public Guest (World world, float positionX, float positionY, float time){
        //TODO: add proper texture
        Texture texture = new Texture("Chef.png");
        sprite = new Sprite(texture); 
        sprite.setSize(WORLD_WIDTH / 32, WORLD_HEIGHT / 18);
        sprite.setPosition(positionX,positionY);
        sprite.setColor(0, 1, 0 , 1);
        box = new Box(world,sprite,false);
        box.getBody().getFixtureList().first().setUserData(this);
        spawnTime = time;
        patience = 30;
        wealth = 40;
        happiness = 3;
    }

    public Sprite getSprite() {
        return sprite;
    }

    public Body getBody() {
        return box.getBody();
    }
    
    public void update(float time) {
        if(!hasBeenServed){
            changeColor("black");
        }
    	timeElapsed = time - spawnTime;
    	if(timeElapsed >= patience) {
    		//TODO: despawn with guest handler
    	}else if(timeElapsed >= (patience / 1.5)){
    		happiness = 1;
    		changeColor("");
    	}else if(timeElapsed >= (patience / 3)){
    		happiness = 2;
    		changeColor("");
    	}
    }

	public int getHappiness() {
		return happiness;
	}

    public void setTable(Table table) {
        this.table = table;
    }

    public void changeColor(String color){
        switch (color) {
            case "black":
                this.sprite.setColor(Color.BLACK);
                break;
            case "red":
                this.sprite.setColor(Color.RED);
                break;
            case "yellow":
                this.sprite.setColor(Color.YELLOW);
                break;
            case "green":
                this.sprite.setColor(Color.GREEN);
                break;
        }
    }
    public void serve(){
        this.hasBeenServed = true;
    }
}
