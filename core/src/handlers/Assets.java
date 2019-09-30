package handlers;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;

public class Assets {

	public static final AssetManager manager = new AssetManager();
	
	public static final String guest = "Chef.png";
	public static final String waiter = "hero.png";
	public static final String counter = "counter.png";
	public static final String pizza = "pizza.png";
	public static final String burger = "burger.png";
	public static final String pasta = "pasta.png";
	public static final String table = "table_round.png";
	
	
	public static void load() {
	manager.load(guest, Texture.class);	
	manager.load(waiter, Texture.class);	
	manager.load(counter, Texture.class);	
	manager.load(pizza, Texture.class);	
	manager.load(burger, Texture.class);	
	manager.load(pasta, Texture.class);	
	manager.load(table, Texture.class);
	}
	
	public static void dispose() {
		manager.dispose();
	}
	
}
