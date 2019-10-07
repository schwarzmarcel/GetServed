package handlers;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;

public class Assets {

	public static final AssetManager manager = new AssetManager();
	
	public static final String GUEST = "Chef.png";
	public static final String WAITER = "hero.png";
	public static final String COUNTER = "counter.png";
	public static final String PIZZA = "pizza.png";
	public static final String BURGER = "burger.png";
	public static final String PASTA = "pasta.png";
	public static final String SALAD = "salad.png";
	public static final String FISH = "fish.png";
	public static final String TABLE = "table_round.png";
	
	
	public static void load() {
	manager.load(GUEST, Texture.class);
	manager.load(WAITER, Texture.class);
	manager.load(COUNTER, Texture.class);
	manager.load(PIZZA, Texture.class);
	manager.load(BURGER, Texture.class);
	manager.load(PASTA, Texture.class);
	manager.load(SALAD, Texture.class);
	manager.load(FISH, Texture.class);
	manager.load(TABLE, Texture.class);
	}
	
	public static void dispose() {
		manager.dispose();
	}
	
}
