package screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.mygdx.game.MyGdxGame;
import entities.*;
import handlers.Assets;
import handlers.GsContactListener;
import handlers.LevelHandler;

import static com.mygdx.game.MyGdxGame.*;

public class GameScreen implements Screen {
	public MyGdxGame game;
	private SpriteBatch batch;
	private ShapeRenderer shapeRenderer;
	private LevelHandler level;
	private Box2DDebugRenderer debugRenderer;
	private OrthographicCamera camera;
	private TiledMap tiledMap;
	private OrthogonalTiledMapRenderer renderer;
	private GsContactListener contactListener;
	private float elapsedTime = 0;
	private BitmapFont moneyFont;
	private GlyphLayout layoutMoney;
	private GlyphLayout layoutTip;
	private int tip;
	private int lastTipTime;
	private Sprite coin;

	public GameScreen(MyGdxGame game, SpriteBatch batch, ShapeRenderer shapeRenderer,
					  String levelname) {
		this.game = game;
		this.batch = batch;
		this.shapeRenderer = shapeRenderer;
		this.camera = camera;
		initializeFonts();
		Gdx.app.log("INFO: ", "fonts initialized");
		level = new LevelHandler(levelname, game);
		level.initializeLevel();
		Gdx.app.log("INFO: ", "level initialized");
		debugRenderer = new Box2DDebugRenderer();
		contactListener = new GsContactListener();
		level.getWorld().setContactListener(contactListener);
		tiledMap = new TmxMapLoader().load("Map/tile-map.tmx");
		//tiledMap = Assets.manager.get(Assets.MAP, TiledMap.class);
		MapProperties properties = tiledMap.getProperties();
		int tileWidth = properties.get("tilewidth", Integer.class);
		int tileHeight = properties.get("tileheight", Integer.class);
		int mapWidthInTiles = properties.get("width", Integer.class);
		int mapHeightInTiles = properties.get("height", Integer.class);
		int mapWidthInPixels = mapWidthInTiles * tileWidth;
		int mapHeightInPixels = mapHeightInTiles * tileHeight;
		camera = new OrthographicCamera(1024.f, 576.f);
		camera.position.x = mapWidthInPixels * .5f;
		camera.position.y = mapHeightInPixels * .5f;
		renderer = new OrthogonalTiledMapRenderer(tiledMap);
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(.5f, .7f, .9f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		camera.update();
		renderer.setView(camera);
		renderer.render();
		level.getWorld().step(1f / 60f, 6, 2);
		level.updateLevel();
		level.getWaiter().move(1f);
		Matrix4 debugMatrix = batch.getProjectionMatrix().cpy().scale(PIXELS_TO_METERS, PIXELS_TO_METERS, 0);
		batch.begin();
		elapsedTime += Gdx.graphics.getDeltaTime();
		drawWaiter();
		drawTables();
		drawGuests();
		drawCounters();
		drawDishes();
		drawOrders();
		drawDishProgress();
		showMoney();
		if ((lastTipTime + 2) >= level.getTime())
			showTip();
		batch.end();
		if (false)
			debugRenderer.render(level.getWorld(), debugMatrix);
		testContacts();

	}

	@Override
	public void dispose() {
		level.getWorld().dispose();
		debugRenderer.dispose();
		
	}

	private void initializeFonts() {
		moneyFont = Assets.manager.get(Assets.MONEYFONT, BitmapFont.class);
		moneyFont.getData().setScale(0.08f);
		layoutMoney = new GlyphLayout();
		tip = 0;
		lastTipTime = -3;
		layoutTip = new GlyphLayout();
		coin = new Sprite(Assets.manager.get(Assets.COIN, Texture.class));
		coin.setSize(7, 7);
		coin.setPosition(WORLD_WIDTH - coin.getWidth() - 4, WORLD_HEIGHT - coin.getHeight() - 3);
	}

	private void drawWaiter() {
		TextureRegion currentFrame;

		if (!Gdx.input.isKeyPressed(Input.Keys.UP) && !Gdx.input.isKeyPressed(Input.Keys.DOWN)
				&& !Gdx.input.isKeyPressed(Input.Keys.LEFT) && !Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
			currentFrame = level.getWaiter().getIdleAnimation().getKeyFrame(elapsedTime);
		} else {
			currentFrame = level.getWaiter().getRunningAnimation().getKeyFrame(elapsedTime);
		}

		if (level.getWaiter().getOrientation().equals("right")) {
			if (currentFrame.isFlipX()) {
				currentFrame.flip(true, false);
			}
		}
		if (level.getWaiter().getOrientation().equals("left")) {
			if (!currentFrame.isFlipX()) {
				currentFrame.flip(true, false);
			}
		}
		batch.draw(currentFrame,
				(level.getWaiter().getBody().getPosition().x * PIXELS_TO_METERS)
						- level.getWaiter().getSprite().getWidth() / 2,
				(level.getWaiter().getBody().getPosition().y * PIXELS_TO_METERS)
						- level.getWaiter().getSprite().getHeight() / 2,
				WORLD_WIDTH / 32, WORLD_HEIGHT / 16);
	}

	private void drawGuests() {
		TextureRegion currentFrame;
		for (Guest g : level.getGuesthandler().getActiveGuests()) {
			currentFrame = g.getActiveAnimation().getKeyFrame(elapsedTime);
			batch.draw(currentFrame, g.getPosition()[0], g.getPosition()[1], WORLD_WIDTH / 32, WORLD_HEIGHT / 16);
			drawPatience(g);
		}
	}

	private void drawPatience(Guest guest) {
		batch.end();
		shapeRenderer.begin(ShapeType.Filled);
		float patience = guest.getPatience();
		if (patience > 75)
			shapeRenderer.setColor(Color.GREEN);
		else if (patience > 50)
			shapeRenderer.setColor(Color.YELLOW);
		else
			shapeRenderer.setColor(Color.RED);
		shapeRenderer.rect(guest.getPosition()[0] - 2, guest.getPosition()[1], 1, ((float) guest.getPatience() / 100) * 5);
		shapeRenderer.end();
		batch.begin();
	}

	private void drawTables() {
		for (Table t : level.getSpawnarea().getTables()) {
			t.getTableSprite().draw(batch);
			t.getChairSprite().draw(batch);
		}
	}

	private void drawCounters() {
		for (Counter c : level.getSpawnarea().getCounters()) {
			c.getSprite().draw(batch);
		}
	}

	private void drawDishes() {
		if (level.getDishhandler().getDishes() != null) {
			for (Dish d : level.getDishhandler().getDishes()) {
				d.getSprite().draw(batch);
			}
		}
	}

	private void drawOrders() {
		for (Guest g : level.getGuesthandler().getActiveGuests()) {
			if ((g.getOrderTime() + 1) >= level.getTime()) {
				g.getBubble().draw(batch);
				g.getDish().getSprite().draw(batch);
			}
		}
	}

	private void drawDishProgress() {
		batch.end();
		shapeRenderer.begin(ShapeType.Filled);
		for (Counter c : level.getSpawnarea().getCounters()) {
			switch (c.getRotation()) {
				case 0:
					shapeRenderer.setColor(Color.LIGHT_GRAY);
					shapeRenderer.rect(c.getPosition()[0] - c.getSprite().getWidth(),
							c.getPosition()[1] + (c.getSprite().getHeight() - c.getNextDish().getSprite().getHeight() - 4),
							c.getNextDish().getSprite().getWidth(), 1);

					shapeRenderer.setColor(Color.BLUE);
					shapeRenderer.rect(c.getPosition()[0] - c.getSprite().getWidth(),
							c.getPosition()[1] + (c.getSprite().getHeight() - c.getNextDish().getSprite().getHeight() - 4),
							(c.getNextDish().getSprite().getWidth())
									* ((level.getTime() - c.getLastDishTime()) / (6 / c.getCookSpeed())),
							1);
					break;
				case 1:
					shapeRenderer.setColor(Color.LIGHT_GRAY);
					shapeRenderer.rect(c.getPosition()[0] + c.getSprite().getWidth() + 1,
							c.getPosition()[1] + (c.getSprite().getHeight() - c.getNextDish().getSprite().getHeight() - 4),
							c.getNextDish().getSprite().getWidth(), 1);

					shapeRenderer.setColor(Color.BLUE);
					shapeRenderer.rect(c.getPosition()[0] + c.getSprite().getWidth() + 1,
							c.getPosition()[1] + (c.getSprite().getHeight() - c.getNextDish().getSprite().getHeight() - 4),
							(c.getNextDish().getSprite().getWidth())
									* ((level.getTime() - c.getLastDishTime()) / (6 / c.getCookSpeed())),
							1);
					break;
				case 2:
					shapeRenderer.setColor(Color.LIGHT_GRAY);
					shapeRenderer.rect(
							c.getPosition()[0] + c.getSprite().getWidth() / 2 - c.getNextDish().getSprite().getWidth() / 2,
							c.getPosition()[1] + c.getSprite().getHeight() + 1, c.getNextDish().getSprite().getWidth(), 1);

					shapeRenderer.setColor(Color.BLUE);
					shapeRenderer.rect(
							c.getPosition()[0] + c.getSprite().getWidth() / 2 - c.getNextDish().getSprite().getWidth() / 2,
							c.getPosition()[1] + c.getSprite().getHeight() + 1, c.getNextDish().getSprite().getWidth()
									* ((level.getTime() - c.getLastDishTime()) / (6 / c.getCookSpeed())),
							1);
					break;
				case 3:
					shapeRenderer.setColor(Color.LIGHT_GRAY);
					shapeRenderer.rect(
							c.getPosition()[0] + c.getSprite().getWidth() / 2 - c.getNextDish().getSprite().getWidth() / 2,
							c.getPosition()[1] - 2, c.getNextDish().getSprite().getWidth(), 1);

					shapeRenderer.setColor(Color.BLUE);
					shapeRenderer.rect(
							c.getPosition()[0] + c.getSprite().getWidth() / 2 - c.getNextDish().getSprite().getWidth() / 2,
							c.getPosition()[1] - 2, c.getNextDish().getSprite().getWidth()
									* ((level.getTime() - c.getLastDishTime()) / (6 / c.getCookSpeed())),
							1);
					break;
			}
		}
		shapeRenderer.end();
		batch.begin();

	}

	private void showMoney() {
		String moneyText = "" + level.getMoney();
		layoutMoney.setText(moneyFont, moneyText);
		moneyFont.draw(batch, layoutMoney, WORLD_WIDTH - layoutMoney.width - 12, WORLD_HEIGHT - 4);
		coin.draw(batch);
	}

	private void showTip() {
		String tipText = "+ " + tip;
		layoutTip.setText(moneyFont, tipText);
		moneyFont.draw(batch, layoutTip, WORLD_WIDTH - layoutTip.width - 12, WORLD_HEIGHT - layoutMoney.height - 6);
	}

	private void testContacts() {
		if (contactListener.getContact() != null) {
			Contact contact = contactListener.getContact();
			Fixture fixtureA = contact.getFixtureA();
			Fixture fixtureB = contact.getFixtureB();
			Table contactTable;
			Counter contactCounter;

			if ((fixtureA.getUserData() instanceof Table) || (fixtureB.getUserData() instanceof Table)) {
				if ((fixtureA.getUserData() instanceof Waiter) || (fixtureB.getUserData() instanceof Waiter)) {

					if (fixtureA.getUserData() instanceof Table)
						contactTable = (Table) fixtureA.getUserData();
					else
						contactTable = (Table) fixtureB.getUserData();

					if (Gdx.input.isKeyJustPressed(Input.Keys.S)) {
						if (level.getWaiter().getDish() != null && contactTable.getGuest() != null) {
							Dish dish = level.getWaiter().getDish();
							Waiter waiter = level.getWaiter();
							Guest guest = contactTable.getGuest();
							if (guest.getOrder() == (dish.type)) {
								dish.setPosition(contactTable.getPosition());
								dish.setSpriteSize(40, 20);
								waiter.removeDish();
								level.getDishhandler().removeDish(dish);
								tip = guest.getTip();
								lastTipTime = level.getTime();
								level.setMoney(level.getMoney() + guest.getTip());
								level.getGuesthandler().removeActiveGuest(guest);
								Gdx.app.log("INFO: ", "Delivered correct Dish to Guest");
							} else {
								guest.receivedWrongDish(level.getTime());
								waiter.removeDish();
								level.getDishhandler().removeDish(dish);
								Gdx.app.log("INFO: ", "Delivered wrong dish to Guest");
							}
						}
					}
				}
			}
			if ((fixtureA.getUserData() instanceof Counter) || (fixtureB.getUserData() instanceof Counter)) {
				if ((fixtureA.getUserData() instanceof Waiter) || (fixtureB.getUserData() instanceof Waiter)) {
					if (fixtureA.getUserData() instanceof Counter)
						contactCounter = (Counter) fixtureA.getUserData();
					else
						contactCounter = (Counter) fixtureB.getUserData();
					if (contactCounter.getDish() != null && level.getWaiter().getDish() == null) {
						level.getWaiter().setDish(contactCounter.getDish());
						level.getWaiter().getDish().setSpriteSize(64, 32);
						contactCounter.removeDish();
					}

				}
			}
		}
	}

	@Override
	public void show() {

	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}
}
