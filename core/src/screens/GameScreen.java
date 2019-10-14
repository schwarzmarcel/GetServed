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
import handlers.LevelManager;

import static com.mygdx.game.MyGdxGame.*;

public class GameScreen implements Screen {
    public MyGdxGame game;
    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;
    private LevelManager levelManager;
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

    public GameScreen(MyGdxGame game, SpriteBatch batch, ShapeRenderer shapeRenderer,
                      String levelname) {
        this.game = game;
        this.batch = batch;
        this.shapeRenderer = shapeRenderer;


        initializeFonts();
        Gdx.app.log("INFO: ", "Fonts initialized");
        levelManager = new LevelManager(levelname);
        Gdx.app.log("INFO: ", "Beginning level initializing");
        levelManager.initializeLevel();
        Gdx.app.log("INFO: ", "Finished Level initialized");

        contactListener = new GsContactListener();
        levelManager.getWorld().setContactListener(contactListener);

        tiledMap = new TmxMapLoader().load("Map/tile-map.tmx");
        MapProperties properties = tiledMap.getProperties();
        int tileWidth = properties.get("tilewidth", Integer.class);
        int tileHeight = properties.get("tileheight", Integer.class);
        int mapWidthInTiles = properties.get("width", Integer.class);
        int mapHeightInTiles = properties.get("height", Integer.class);
        int mapWidthInPixels = mapWidthInTiles * tileWidth;
        int mapHeightInPixels = mapHeightInTiles * tileHeight;
        renderer = new OrthogonalTiledMapRenderer(tiledMap);

        camera = new OrthographicCamera(1024.f, 576.f);
        camera.position.x = mapWidthInPixels * .5f;
        camera.position.y = mapHeightInPixels * .5f;

        debugRenderer = new Box2DDebugRenderer();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(.5f, .7f, .9f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.update();
        renderer.setView(camera);
        renderer.render();
        levelManager.getWorld().step(1f / 60f, 6, 2);
        levelManager.updateLevel();
        levelManager.getWaiter().move(1f);
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
        if (levelManager.isLevelOver()) {
            game.increaseLevel();
            game.showMenu();
        }
        if ((lastTipTime + 2) >= levelManager.getTime())
            showTip();
        batch.end();
        if (false)
            debugRenderer.render(levelManager.getWorld(), debugMatrix);
        testContacts();

    }

    @Override
    public void dispose() {
        //level.getWorld().dispose();
        //debugRenderer.dispose();

    }

    private void initializeFonts() {
        moneyFont = Assets.manager.get(Assets.MONEYFONT, BitmapFont.class);
        moneyFont.getData().setScale(0.1f);
        layoutMoney = new GlyphLayout();
        tip = 0;
        lastTipTime = -3;
        layoutTip = new GlyphLayout();
    }

    private void drawWaiter() {
        TextureRegion currentFrame;

        if (!Gdx.input.isKeyPressed(Input.Keys.UP) && !Gdx.input.isKeyPressed(Input.Keys.DOWN)
                && !Gdx.input.isKeyPressed(Input.Keys.LEFT) && !Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            currentFrame = levelManager.getWaiter().getIdleAnimation().getKeyFrame(elapsedTime);
        } else {
            currentFrame = levelManager.getWaiter().getRunningAnimation().getKeyFrame(elapsedTime);
        }

        if (levelManager.getWaiter().getOrientation().equals("right")) {
            if (currentFrame.isFlipX()) {
                currentFrame.flip(true, false);
            }
        }
        if (levelManager.getWaiter().getOrientation().equals("left")) {
            if (!currentFrame.isFlipX()) {
                currentFrame.flip(true, false);
            }
        }
        batch.draw(currentFrame,
                (levelManager.getWaiter().getBody().getPosition().x * PIXELS_TO_METERS)
                        - levelManager.getWaiter().getSprite().getWidth() / 2,
                (levelManager.getWaiter().getBody().getPosition().y * PIXELS_TO_METERS)
                        - levelManager.getWaiter().getSprite().getHeight() / 2,
                WORLD_WIDTH / 32, WORLD_HEIGHT / 16);
    }

    private void drawGuests() {
        TextureRegion currentFrame;
        for (Guest g : levelManager.getGuestManager().getActiveGuests()) {
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
        for (Table t : levelManager.getGameField().getTables()) {
            t.getTableSprite().draw(batch);
            t.getChairSprite().draw(batch);
        }
    }

    private void drawCounters() {
        for (Counter c : levelManager.getGameField().getCounters()) {
            c.getSprite().draw(batch);
        }
    }

    private void drawDishes() {
        if (levelManager.getDishhandler().getDishes() != null) {
            for (Dish d : levelManager.getDishhandler().getDishes()) {
                d.getSprite().draw(batch);
            }
        }
    }

    private void drawOrders() {
        for (Guest g : levelManager.getGuestManager().getActiveGuests()) {
            if ((g.getOrderTime() + 1) >= levelManager.getTime()) {
                g.getDish().getSprite().draw(batch);
            }
        }
    }

    private void drawDishProgress() {
        batch.end();
        shapeRenderer.begin(ShapeType.Filled);
        for (Counter c : levelManager.getGameField().getCounters()) {
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
                                    * ((levelManager.getTime() - c.getLastDishTime()) / (6 / c.getCookSpeed())),
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
                                    * ((levelManager.getTime() - c.getLastDishTime()) / (6 / c.getCookSpeed())),
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
                                    * ((levelManager.getTime() - c.getLastDishTime()) / (6 / c.getCookSpeed())),
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
                                    * ((levelManager.getTime() - c.getLastDishTime()) / (6 / c.getCookSpeed())),
                            1);
                    break;
            }
        }
        shapeRenderer.end();
        batch.begin();

    }

    private void showMoney() {
        String moneyText = "" + levelManager.getMoney() + " $";
        layoutMoney.setText(moneyFont, moneyText);
        moneyFont.draw(batch, layoutMoney, WORLD_WIDTH - layoutMoney.width - 1, WORLD_HEIGHT - 1);
    }

    private void showTip() {
        String tipText = "+ " + tip + " $";
        layoutTip.setText(moneyFont, tipText);
        moneyFont.draw(batch, layoutTip, WORLD_WIDTH - layoutTip.width - 1, WORLD_HEIGHT - layoutMoney.height - 3);
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
                        if (levelManager.getWaiter().getDish() != null && contactTable.getGuest() != null) {
                            Dish dish = levelManager.getWaiter().getDish();
                            Waiter waiter = levelManager.getWaiter();
                            Guest guest = contactTable.getGuest();
                            if (guest.getOrder() == (dish.type)) {
                                dish.setPosition(contactTable.getPosition());
                                dish.setSpriteSize(40, 20);
                                waiter.removeDish();
                                levelManager.getDishhandler().removeDish(dish);
                                tip = guest.getTip();
                                lastTipTime = levelManager.getTime();
                                levelManager.setMoney(levelManager.getMoney() + guest.getTip());
                                levelManager.getGuestManager().removeActiveGuest(guest);
                                Gdx.app.log("INFO: ", "Delivered correct Dish to Guest");
                            } else {
                                guest.receivedWrongDish(levelManager.getTime());
                                waiter.removeDish();
                                levelManager.getDishhandler().removeDish(dish);
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
                    if (contactCounter.getDish() != null && levelManager.getWaiter().getDish() == null) {
                        levelManager.getWaiter().setDish(contactCounter.getDish());
                        levelManager.getWaiter().getDish().setSpriteSize(64, 32);
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
