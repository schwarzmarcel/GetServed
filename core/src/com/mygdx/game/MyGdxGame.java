package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;

import entities.Box;
import entities.Guest;
import entities.Table;
import entities.Waiter;
import entities.Walls;
import handlers.GsContactListener;

/**
 * @author Marcel
 *
 */
/**
 * @author Marcel
 *
 */
public class MyGdxGame extends ApplicationAdapter {

	private SpriteBatch batch;
	private World world;
	private Box2DDebugRenderer debugRenderer;
	private OrthographicCamera camera;
	private Waiter waiter;
	private Table table;
	private Guest guest;
	private Walls walls;
	private float time;
	private GsContactListener contactListener;
	public static final float PIXELS_TO_METERS = 50f;
	public static final float WORLD_WIDTH = 160;
	public static final float WORLD_HEIGHT = 90;

	@Override
	public void create() {
		startTimer();
		batch = new SpriteBatch();
		world = new World(new Vector2(0, 0), true);
		waiter = new Waiter(world, WORLD_WIDTH / 2, WORLD_HEIGHT / 2);
		table = new Table(world, WORLD_WIDTH / 4, WORLD_HEIGHT / 2);
		guest = new Guest(world, WORLD_WIDTH / 4, (WORLD_HEIGHT / 2) + 30, time);
		walls = new Walls(world);
		debugRenderer = new Box2DDebugRenderer();
		camera = new OrthographicCamera(160, 90);
		camera.position.set(WORLD_WIDTH / 2, WORLD_HEIGHT / 2, 0);
		contactListener = new GsContactListener();
		world.setContactListener(contactListener);

	}

	@Override
	public void render() {
		camera.update();
		world.step(1f / 60f, 6, 2);

		adjustWaiterSprite();
		guest.update(time);
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		batch.setProjectionMatrix(camera.combined);
		Matrix4 debugMatrix = batch.getProjectionMatrix().cpy().scale(PIXELS_TO_METERS, PIXELS_TO_METERS, 0);
		waiter.move(2.0f);
		batch.begin();
		drawWaiter();
		drawTables();
		drawGuests();
		batch.end();
		if (true)
			debugRenderer.render(world, debugMatrix);

		testContacts();

	}

	private void adjustWaiterSprite() {
		((Sprite) waiter.getBody().getUserData()).setPosition(
				(waiter.getBody().getPosition().x * PIXELS_TO_METERS) - waiter.getSprite().getWidth() / 2,
				(waiter.getBody().getPosition().y * PIXELS_TO_METERS) - waiter.getSprite().getHeight() / 2);
	}

	private void drawWaiter() {
		waiter.getSprite().draw(batch);
	}

	private void drawTables() {
		table.getSprite().draw(batch);
	}

	private void drawGuests() {
		guest.getSprite().draw(batch);
	}

	private void startTimer() {
		time = 0;
		Timer.schedule(new Task() {
			@Override
			public void run() {
				time++;
			}
		}, 0, 1);
	}

	/*
	 * tests if the player currently has contact with a guest, if yes then x can be
	 * clicked to get the guests current happiness
	 */
	private void testContacts() {
		if(contactListener.getContact() != null) {
			Contact contact = contactListener.getContact();
			Fixture fixtureA = contact.getFixtureA();
			Fixture fixtureB = contact.getFixtureB();
			Guest contactGuest;

			if ((fixtureA.getUserData() instanceof Guest) || (fixtureB.getUserData() instanceof Guest)) {
				if ((fixtureA.getUserData() instanceof Waiter) || (fixtureB.getUserData() instanceof Waiter)) {
					
					if (fixtureA.getUserData() instanceof Guest)
						contactGuest = (Guest) fixtureA.getUserData();
					else
						contactGuest = (Guest) fixtureB.getUserData();

					if (Gdx.input.isKeyJustPressed(Input.Keys.X))
						System.out.println(contactGuest.getHappiness());

				}
			}
		}
	}

	@Override
	public void dispose() {
		world.dispose();
	}
}
