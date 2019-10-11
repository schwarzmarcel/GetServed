package handlers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class Assets {

	public static final AssetManager manager = new AssetManager();
	
	
	public static final String WAITERIDLE = "character_sprites/Waiter_Idle.atlas";
	public static final String WAITERWALKING = "character_sprites/Waiter_Walking.atlas";
	public static final String COUNTER = "counter.png";
	public static final String PIZZA = "pizza.png";
	public static final String BURGER = "burger.png";
	public static final String PASTA = "pasta.png";
	public static final String SALAD = "salad.png";
	public static final String FISH = "fish.png";
	public static final String TABLE = "table_round.png";
	public static final String GUEST1 = "character_sprites/Guest1.atlas";
	public static final String GUEST2 = "character_sprites/Guest2.atlas";
	public static final String GUEST3 = "character_sprites/Guest3.atlas";
	public static final String GUEST4 = "character_sprites/Guest4.atlas";
	public static final String GUEST5 = "character_sprites/Guest5.atlas";
	public static final String MONEYFONT = "moneyfont2.fnt";

	
	
	
	public static void load() {
	manager.load(COUNTER, Texture.class);
	manager.load(PIZZA, Texture.class);
	manager.load(BURGER, Texture.class);
	manager.load(PASTA, Texture.class);
	manager.load(SALAD, Texture.class);
	manager.load(FISH, Texture.class);
	manager.load(TABLE, Texture.class);
	manager.load(WAITERIDLE, TextureAtlas.class);
	manager.load(WAITERWALKING, TextureAtlas.class);
	manager.load(GUEST1, TextureAtlas.class);
	manager.load(GUEST2, TextureAtlas.class);
	manager.load(GUEST3, TextureAtlas.class);
	manager.load(GUEST4, TextureAtlas.class);
	manager.load(GUEST5, TextureAtlas.class);
	manager.load(MONEYFONT, BitmapFont.class);
	}
	
	public static void dispose() {
		manager.dispose();
	}
	
}
