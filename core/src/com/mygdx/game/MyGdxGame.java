package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Fixture;
import entities.*;
import handlers.GsContactListener;
import handlers.LevelHandler;
import handlers.Assets;

public class MyGdxGame extends ApplicationAdapter {

    private SpriteBatch batch;
    private Box2DDebugRenderer debugRenderer;
    private OrthographicCamera camera;
    private GsContactListener contactListener;
    private BitmapFont moneyFont;
    private BitmapFont tipFont;
    private String moneyText;
    private String tipText;
    private int tip;
    private int lastTipTime;
    private GlyphLayout layoutMoney;
    private GlyphLayout layoutTip;
    private LevelHandler level;
    public static final float PIXELS_TO_METERS = 50f;
    public static final float WORLD_WIDTH = 160;
    public static final float WORLD_HEIGHT = 90;

    @Override
    public void create() {
    	Assets.load();
    	Assets.manager.finishLoading();
        batch = new SpriteBatch();
        initializeFonts();
        level = new LevelHandler(1);
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
        drawOrders();
        showMoney();
        if((lastTipTime + 2) >= level.getTime())
        showTip();
        batch.end();
        if (true) debugRenderer.render(level.getWorld(), debugMatrix);
        testContacts();
    }

    @Override
	public void dispose() {
	    level.getWorld().dispose();
	    Assets.dispose();
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

    private void drawGuests() {
        for (Guest g : level.getGuesthandler().getActiveGuests()) {
            g.getSprite().draw(batch);
        }
    }

    private void showMoney() {
        moneyText = "" + level.getMoney() + " $";
        layoutMoney.setText(moneyFont, moneyText);
        moneyFont.draw(batch, layoutMoney, WORLD_WIDTH - layoutMoney.width - 1, WORLD_HEIGHT - 1);
    }
    
    private void showTip() {
        tipText = "+ " + tip + " $";
        layoutTip.setText(tipFont, tipText);
        tipFont.draw(batch, layoutTip, WORLD_WIDTH - layoutTip.width - 1, WORLD_HEIGHT - layoutMoney.height - 3);
    }

    private void drawDishes() {
        if (level.getDishhandler().getDishes() != null) {
            for (Dish d : level.getDishhandler().getDishes()
            ) {
                d.getSprite().draw(batch);
            }
        }
    }
    
    private void drawOrders() {
    	for (Guest g : level.getGuesthandler().getActiveGuests()) {
    		if((g.getSpawnTime() + 1) >= level.getTime()) {
    			//TODO: draw textures for order
    		}
    	}
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

                    if (Gdx.input.isKeyJustPressed(Input.Keys.F)) {
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
                        level.getDishhandler().updateDishTimer(level.getTime());
                    }

                }
            }
        }
    }
}
