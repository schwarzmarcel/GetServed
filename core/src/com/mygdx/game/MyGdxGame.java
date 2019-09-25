package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Timer;
import entities.*;
import handlers.GsContactListener;

public class MyGdxGame extends ApplicationAdapter {

    private SpriteBatch batch;
    private Box2DDebugRenderer debugRenderer;
    private OrthographicCamera camera;
    private GsContactListener contactListener;
    private BitmapFont moneyFont;
    private String money;
    private GlyphLayout layout;
    private Level level;
    public static final float PIXELS_TO_METERS = 50f;
    public static final float WORLD_WIDTH = 160;
    public static final float WORLD_HEIGHT = 90;

    @Override
    public void create() {
        batch = new SpriteBatch();
        moneyFont = new BitmapFont(Gdx.files.internal("moneyfont2.fnt"));
        moneyFont.getData().setScale(0.1f);
        layout = new GlyphLayout();
        
        level = new Level(5,5,1,1);
        level.initializeLevel();
       
        debugRenderer = new Box2DDebugRenderer();
        camera = new OrthographicCamera(160, 90);
        camera.position.set(WORLD_WIDTH / 2, WORLD_HEIGHT / 2, 0);
        
        contactListener = new GsContactListener();
        level.getWorld().setContactListener(contactListener);
    }

    @Override
    public void render() {
        camera.update();
        level.getWorld().step(1f / 60f, 6, 2);
        level.updateLevel();
        level.getWaiter().move(2.0f);

        adjustWaiterSprite();

        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.setProjectionMatrix(camera.combined);
        Matrix4 debugMatrix = batch.getProjectionMatrix().cpy().scale(PIXELS_TO_METERS,
                PIXELS_TO_METERS, 0);
        batch.begin();
        drawWaiter();
        drawGuests();
        drawDishes();
        drawCounters();
        showMoney();
        batch.end();
        if (true) debugRenderer.render(level.getWorld(), debugMatrix);
        testContacts();
    }

    private void adjustWaiterSprite() {
        ((Sprite) level.getWaiter().getBody().getUserData()).setPosition(
                (level.getWaiter().getBody().getPosition().x * PIXELS_TO_METERS) - level.getWaiter().getSprite().getWidth() / 2,
                (level.getWaiter().getBody().getPosition().y * PIXELS_TO_METERS) - level.getWaiter().getSprite().getHeight() / 2
        );
    }

    private void drawWaiter() {
        batch.draw(level.getWaiter().getSprite(),
                level.getWaiter().getSprite().getX(),
                level.getWaiter().getSprite().getY(),
                level.getWaiter().getSprite().getWidth(),
                level.getWaiter().getSprite().getHeight()
        );
    }

    private void drawTables() {
        for (Table t : level.getTables()
        ) {
            batch.draw(t.getSprite(),
                    t.getSprite().getX(),
                    t.getSprite().getY(),
                    t.getSprite().getWidth(),
                    t.getSprite().getHeight()
            );
        }

    }

    private void drawCounters() {
        for (Counter c : level.getCounters()
        ) {
            batch.draw(c.getSprite(),
                    c.getSprite().getX(),
                    c.getSprite().getY(),
                    c.getSprite().getWidth(),
                    c.getSprite().getHeight()
            );
        }

    }

    private void drawGuests() {
        for (Guest g : level.getGuests()
        ) {
            g.getSprite().draw(batch);
        }
    }

	private void showMoney() {
		money = "" + level.getMoney() + " $";
		layout.setText(moneyFont, money);
		moneyFont.draw(batch, layout, WORLD_WIDTH - layout.width - 1, WORLD_HEIGHT - 1);
	}

    private void drawDishes() {
        if (level.getDishes()[0] != null) {
            for (Dish d : level.getDishes()
            ) {
                d.getSprite().draw(batch);
            }
        }
    }

    private void testContacts() {
        if (contactListener.getContact() != null) {
            Contact contact = contactListener.getContact();
            Fixture fixtureA = contact.getFixtureA();
            Fixture fixtureB = contact.getFixtureB();
            Guest contactGuest;
            Counter contactCounter;

            if ((fixtureA.getUserData() instanceof Guest) || (fixtureB.getUserData() instanceof Guest)) {
                if ((fixtureA.getUserData() instanceof Waiter) || (fixtureB.getUserData() instanceof Waiter)) {

                    if (fixtureA.getUserData() instanceof Guest)
                        contactGuest = (Guest) fixtureA.getUserData();
                    else
                        contactGuest = (Guest) fixtureB.getUserData();

                    if (Gdx.input.isKeyJustPressed(Input.Keys.X))
                        level.setMoney(level.getMoney() + contactGuest.getTip());
                  
                    if (Gdx.input.isKeyJustPressed(Input.Keys.F)) {
                        if (level.getWaiter().getDish() != null) {
                            if (contactGuest.getOrder() == (level.getWaiter().getDish().type)) {
                                level.getWaiter().getDish().setPosition(contactGuest.getTable().getPosition());
                                contactGuest.serve();
                                level.getWaiter().removeDish();
                            }
                        }
                    }
                }
            }
            if ((fixtureA.getUserData() instanceof Counter) || (fixtureB.getUserData() instanceof Counter)) {
                if ((fixtureA.getUserData() instanceof Waiter) || (fixtureB.getUserData() instanceof Waiter)) {
                    if (fixtureA.getUserData() instanceof Guest)
                        contactCounter = (Counter) fixtureA.getUserData();
                    else
                        contactCounter = (Counter) fixtureB.getUserData();
                    if (contactCounter.getDish() != null) {
                        level.getWaiter().setDish(contactCounter.getDish());
                        contactCounter.removeDish();
                    }

                }
            }
        }
    }
    @Override
    public void dispose() {
        level.getWorld().dispose();
    }
}
