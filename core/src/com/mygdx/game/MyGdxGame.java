package com.mygdx.game;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Fixture;
import entities.*;
import handlers.Assets;
import handlers.GsContactListener;
import handlers.LevelHandler;

import java.util.Iterator;
import java.util.Map;

public class MyGdxGame extends ApplicationAdapter {
	// TODO: improve visual feedback for guest and dish spawning / Guest movement?
	// Maybe make the guest look around? fade out for guests?
	// TODO: maybe improve guest patience visualisation
	// TODO: improve overall logic for dish spawning
	// TODO: add a visual queue for guests
	// TODO: consider the distance between counters and guests
	// TODO: more dishes and proper sprites
	// TODO: improve distance between counters / maybe they should not be placed
	// right next to each other
	// TODO: adjust box size, maybe make it a little bit smaller to improve colision
	// feeling
	private SpriteBatch batch;
	private Box2DDebugRenderer debugRenderer;
	private OrthographicCamera camera;
	private GsContactListener contactListener;
	private BitmapFont moneyFont;
	private float elapsedTime = 0;
	private BitmapFont tipFont;
	private int tip;
	private int lastTipTime;
	private GlyphLayout layoutMoney;
	private GlyphLayout layoutTip;
	private LevelHandler level;
	private ShapeRenderer shapeRenderer;
	public static final float PIXELS_TO_METERS = 50f;
	public static final float WORLD_WIDTH = 160;
	public static final float WORLD_HEIGHT = 90;

	@Override
	public void create() {
		Gdx.app.setLogLevel(Application.LOG_ERROR);
		Assets.load();
		Assets.manager.finishLoading();
		batch = new SpriteBatch();
		level = new LevelHandler();
		level.initializeLevel();
		Gdx.app.log("INFO:", "level initialized");
		initializeFonts();
		Gdx.app.log("INFO:", "fonts initialized");
		debugRenderer = new Box2DDebugRenderer();
		camera = new OrthographicCamera(160, 90);
		camera.position.set(WORLD_WIDTH / 2, WORLD_HEIGHT / 2, 0);
		contactListener = new GsContactListener();
		level.getWorld().setContactListener(contactListener);
		shapeRenderer = new ShapeRenderer();
	}

	@Override
	public void render() {
		camera.update();
		level.getWorld().step(1f / 60f, 6, 2);
		level.updateLevel();
		level.getWaiter().move(1f);
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.setProjectionMatrix(camera.combined);
		shapeRenderer.setProjectionMatrix(camera.combined);
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
	public void dispose() {
		level.getWorld().dispose();
		Assets.dispose();
		shapeRenderer.dispose();
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

	private void drawWaiter() {
		TextureRegion currentFrame = level.getWaiter().getRunningAnimation().getKeyFrame(elapsedTime);

		if (!Gdx.input.isKeyPressed(Input.Keys.UP) && !Gdx.input.isKeyPressed(Input.Keys.DOWN)
				&& !Gdx.input.isKeyPressed(Input.Keys.LEFT) && !Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
			currentFrame = level.getWaiter().getSprite();
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
		for (Guest g : level.getGuesthandler().getActiveGuests()) {
			g.getSprite().draw(batch);
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
//        for (Sprite s : level.getCurrentOrderSymbols()
//        ) {
//            if (s != null) {
//                s.draw(batch);
//            }
//        }
//        for (Sprite s : level.getActiveOrders()
//        ) {
//            if (s != null) s.draw(batch);
//        }
	}

	private void drawDishProgress() {
		batch.end();
		shapeRenderer.begin(ShapeType.Filled);
		for (Counter c : level.getSpawnarea().getCounters()) {

			shapeRenderer.setColor(Color.LIGHT_GRAY);
			shapeRenderer.rect(c.getPosition()[0] - c.getSprite().getWidth(),
					c.getPosition()[1] + (c.getSprite().getHeight() - c.getNextDish().getSprite().getHeight() - 4),
					c.getNextDish().getSprite().getWidth(), 1);

			shapeRenderer.setColor(Color.BLUE);
			shapeRenderer.rect(c.getPosition()[0] - c.getSprite().getWidth(),
					c.getPosition()[1] + (c.getSprite().getHeight() - c.getNextDish().getSprite().getHeight() - 4),
					(c.getNextDish().getSprite().getWidth()) * ((level.getTime() - c.getLastDishTime()) / (6 / c.getCookSpeed())), 1);
		}
		shapeRenderer.end();
		batch.begin();

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
