package handlers;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class Assets {

	public static final AssetManager manager = new AssetManager();

	public static final String WAITERIDLE = "character_sprites/Waiter_Idle.atlas";
	public static final String WAITERWALKING = "character_sprites/Waiter_Walking.atlas";
    public static final String COUNTER = "./Map/desk 1.png";
	public static final String PIZZA = "./Food/pizza.png";
	public static final String BURGER = "./Food/burger.png";
	public static final String POMMES = "./Food/pommes.png";
	public static final String CHICKEN = "./Food/chicken.png";
	public static final String FISH = "./Food/fish.png";
    public static final String TABLE = "./Map/table 1.png";
	public static final String CHAIR = "./Map/Chair 1.png";
	public static final String GUEST1 = "character_sprites/Guest1.atlas";
	public static final String GUEST2 = "character_sprites/Guest2.atlas";
	public static final String GUEST3 = "character_sprites/Guest3.atlas";
	public static final String GUEST4 = "character_sprites/Guest4.atlas";
	public static final String GUEST5 = "character_sprites/Guest5.atlas";
	public static final String COIN = "coin.png";
	public static final String BUBBLE = "bubble.png";
	public static final String MONEYFONT = "goldfont.fnt";
	public static final String NEXTACTIVE = "nextactive.png";
	public static final String NEXTPASSIVE = "nextpassive.png";
	public static final String EXITACTIVE = "exitactive.png";
	public static final String EXITPASSIVE = "exitpassive.png";

	public static final String ARROWS = "arrowkeys.png";
	public static final String SKEY = "Keyboard_White_S.png";
	public static final String DKEY = "Keyboard_White_D.png";
	public static final String MOVE = "move.png";
	public static final String SERVE = "serve.png";
	public static final String DISCARD = "discard.png";
	public static final String LOADING = "loading.png";
	public static final String ENTER = "continue.png";

	public static final String GUEST1_ANGRY = "character_sprites/Guest1_Kick.atlas";
	public static final String GUEST1_ORDERING = "character_sprites/Guest1_Jump.atlas";
	public static final String GUEST2_ORDERING = "character_sprites/Guest2_Jump.atlas";
	public static final String GUEST2_ANGRY = "character_sprites/Guest2_Kick.atlas";
	public static final String GUEST3_ANGRY = "character_sprites/Guest3_Kick.atlas";
	public static final String GUEST3_ORDERING = "character_sprites/Guest3_Jump.atlas";
	public static final String GUEST4_ANGRY = "character_sprites/Guest4_Kick.atlas";
	public static final String GUEST4_ORDERING = "character_sprites/Guest4_Jump.atlas";
	public static final String GUEST5_ANGRY = "character_sprites/Guest5_Kick.atlas";
	public static final String GUEST5_ORDERING = "character_sprites/Guest5_Jump.atlas";


	public static void load() {
		manager.load(COUNTER, Texture.class);
		manager.load(PIZZA, Texture.class);
		manager.load(BURGER, Texture.class);
		manager.load(FISH, Texture.class);
		manager.load(POMMES, Texture.class);
		manager.load(BURGER, Texture.class);
		manager.load(CHICKEN, Texture.class);
		manager.load(TABLE, Texture.class);
		manager.load(WAITERIDLE, TextureAtlas.class);
		manager.load(WAITERWALKING, TextureAtlas.class);
		manager.load(GUEST1, TextureAtlas.class);
		manager.load(GUEST2, TextureAtlas.class);
		manager.load(GUEST3, TextureAtlas.class);
		manager.load(GUEST4, TextureAtlas.class);
		manager.load(GUEST5, TextureAtlas.class);
		manager.load(MONEYFONT, BitmapFont.class);
		manager.load(NEXTACTIVE, Texture.class);
		manager.load(NEXTPASSIVE, Texture.class);
		manager.load(EXITACTIVE, Texture.class);
		manager.load(EXITPASSIVE, Texture.class);
		manager.load(CHAIR, Texture.class);
		manager.load(BUBBLE, Texture.class);
		manager.load(COIN, Texture.class);
		
		/* manager.load(GUEST1_ANGRY, Texture.class);
        manager.load(GUEST1_ORDERING, Texture.class);
        manager.load(GUEST2_ORDERING, Texture.class);
        manager.load(GUEST2_ANGRY, TextureAtlas.class);
        manager.load(GUEST3_ANGRY, TextureAtlas.class);
        manager.load(GUEST3_ORDERING, TextureAtlas.class);
        manager.load(GUEST4_ANGRY, TextureAtlas.class);
        manager.load(GUEST4_ORDERING, TextureAtlas.class);
        manager.load(GUEST5_ANGRY, TextureAtlas.class);
        manager.load(GUEST5_ORDERING, TextureAtlas.class);*/

	}

	public static void loadingScreen() {
		manager.load(ARROWS, Texture.class);
		manager.load(SKEY, Texture.class);
		manager.load(DKEY, Texture.class);
		manager.load(MOVE, Texture.class);
		manager.load(SERVE, Texture.class);
		manager.load(DISCARD, Texture.class);
		manager.load(LOADING, Texture.class);
		manager.load(ENTER, Texture.class);

	}

	public static void dispose() {
		manager.dispose();
	}

}
