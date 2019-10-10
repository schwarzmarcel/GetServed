package screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.mygdx.game.MyGdxGame;
import entities.*;
import handlers.GsContactListener;
import handlers.LevelHandler;

import static com.mygdx.game.MyGdxGame.*;

public class GameScreen implements Screen{
	public MyGdxGame game;
	private SpriteBatch batch;
	private ShapeRenderer shapeRenderer;
	private LevelHandler level;
	private Box2DDebugRenderer debugRenderer;
	private OrthographicCamera camera;
	private GsContactListener contactListener;
	private float elapsedTime = 0;
	private BitmapFont moneyFont;
	private BitmapFont tipFont;
	private GlyphLayout layoutMoney;
	private GlyphLayout layoutTip;
	private int tip;
	private int lastTipTime;
	
	
	


	public GameScreen(MyGdxGame game, SpriteBatch batch, ShapeRenderer shapeRenderer, OrthographicCamera camera, String levelname) {
		this.game = game;
		this.batch = batch;
		this.shapeRenderer = shapeRenderer;
		this.camera = camera;
		initializeFonts();
		Gdx.app.log("INFO:", "fonts initialized");
		level = new LevelHandler(levelname, game);
		level.initializeLevel();
		Gdx.app.log("INFO:", "level initialized");
		debugRenderer = new Box2DDebugRenderer();
		contactListener = new GsContactListener();
		level.getWorld().setContactListener(contactListener);
	}

	@Override
	public void show() {
		
	}

	@Override
	public void render(float delta) {
		camera.update();
		level.getWorld().step(1f / 60f, 6, 2);
		level.updateLevel();
		level.getWaiter().move(1f);
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
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
		if (true)
			debugRenderer.render(level.getWorld(), debugMatrix);
		testContacts();
		
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

	@Override
	public void dispose() {
		level.getWorld().dispose();
	}
	
	private void initializeFonts() {
		moneyFont = new BitmapFont(Gdx.files.internal("moneyfont2.fnt"));
		moneyFont.getData().setScale(0.1f);
		layoutMoney = new GlyphLayout();
		tip = 0;
		lastTipTime = -3;
		tipFont = new BitmapFont(Gdx.files.internal("moneyfont2.fnt"));
		tipFont.getData().setScale(0.08f);
		layoutTip = new GlyphLayout();
	}
	
	private void drawWaiter() {
		TextureRegion currentFrame;

		if (!Gdx.input.isKeyPressed(Input.Keys.UP) && !Gdx.input.isKeyPressed(Input.Keys.DOWN)
				&& !Gdx.input.isKeyPressed(Input.Keys.LEFT) && !Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
			currentFrame = level.getWaiter().getIdleAnimation().getKeyFrame(elapsedTime);
		}else{
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
				WORLD_WIDTH / 16, WORLD_HEIGHT / 8);
	}
	
	private void drawGuests() {
		TextureRegion currentFrame;
		for (Guest g : level.getGuesthandler().getActiveGuests()) {
			currentFrame = g.getIdleAnimation().getKeyFrame(elapsedTime);
			batch.draw(currentFrame,
					g.getSprite().getX(),
					g.getSprite().getY(),
					WORLD_WIDTH / 16, WORLD_HEIGHT / 8);
		}
	}
	
	private void drawTables() {
		for (Table t : level.getSpawnarea().getTables()) {
			t.getSprite().draw(batch);
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
				g.getDish().getSprite().draw(batch);
			}
		}
	}

	private void drawDishProgress() {
		batch.end();
		shapeRenderer.begin(ShapeType.Filled);
		for (Counter c : level.getSpawnarea().getCounters()) {
			if (c.getRotation() == 0) {
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
				} else if (c.getRotation() == 1) {
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
				} else if (c.getRotation() == 2) {
					shapeRenderer.setColor(Color.LIGHT_GRAY);
					shapeRenderer.rect(c.getPosition()[0] + c.getSprite().getWidth() / 2 - c.getNextDish().getSprite().getWidth() / 2,
							c.getPosition()[1] + c.getSprite().getHeight() + 1,
							c.getNextDish().getSprite().getWidth(), 1);

					shapeRenderer.setColor(Color.BLUE);
					shapeRenderer.rect(c.getPosition()[0] + c.getSprite().getWidth() / 2 - c.getNextDish().getSprite().getWidth() / 2,
							c.getPosition()[1] + c.getSprite().getHeight() + 1,
							c.getNextDish().getSprite().getWidth()
									* ((level.getTime() - c.getLastDishTime()) / (6 / c.getCookSpeed())),
							1);
				} else if (c.getRotation() == 3) {
					shapeRenderer.setColor(Color.LIGHT_GRAY);
					shapeRenderer.rect(c.getPosition()[0] + c.getSprite().getWidth() / 2 - c.getNextDish().getSprite().getWidth() / 2,
							c.getPosition()[1] - 2,
							c.getNextDish().getSprite().getWidth(), 1);

					shapeRenderer.setColor(Color.BLUE);
					shapeRenderer.rect(c.getPosition()[0] + c.getSprite().getWidth() / 2 - c.getNextDish().getSprite().getWidth() / 2,
							c.getPosition()[1] - 2,
							c.getNextDish().getSprite().getWidth()
									* ((level.getTime() - c.getLastDishTime()) / (6 / c.getCookSpeed())),
							1);
				}
		}
		shapeRenderer.end();
		batch.begin();

	}
	
	private void showMoney() {
		String moneyText = "" + level.getMoney() + " $";
		layoutMoney.setText(moneyFont, moneyText);
		moneyFont.draw(batch, layoutMoney, WORLD_WIDTH - layoutMoney.width - 1, WORLD_HEIGHT - 1);
	}

	private void showTip() {
		String tipText = "+ " + tip + " $";
		layoutTip.setText(tipFont, tipText);
		tipFont.draw(batch, layoutTip, WORLD_WIDTH - layoutTip.width - 1, WORLD_HEIGHT - layoutMoney.height - 3);
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
							if (contactTable.getGuest().getOrder() == (level.getWaiter().getDish().type)) {
								level.getWaiter().getDish().setPosition(contactTable.getPosition());
								level.getWaiter().removeDish();
								level.getDishhandler().removeDish(dish);
								tip = contactTable.getGuest().getTip();
								lastTipTime = level.getTime();
								level.setMoney(level.getMoney() + contactTable.getGuest().getTip());
								level.getGuesthandler().removeActiveGuest(contactTable.getGuest());
								Gdx.app.log("INFO: ", "Delivered correct Dish to Guest");
							} else {
								Gdx.app.log("INFO: ", "Tried to deliver wrong dish to Guest");
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
						contactCounter.removeDish();

					}

				}
			}
		}
	}

}
