package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import entities.Table;
import entities.Waiter;

public class MyGdxGame extends ApplicationAdapter {

    private SpriteBatch batch;
    private World world;
    private Box2DDebugRenderer debugRenderer;
    private OrthographicCamera camera;
    private Waiter waiter;
    private Table table;
    public static final float PIXELS_TO_METERS = 50f;
    public static final float WORLD_WIDTH = 160;
    public static final float WORLD_HEIGHT = 90;

    @Override
    public void create() {
        batch = new SpriteBatch();
        world = new World(new Vector2(0, 0), true);
        waiter = new Waiter(world, WORLD_WIDTH / 2, WORLD_HEIGHT / 2);
	table = new Table(world,WORLD_WIDTH / 4, WORLD_HEIGHT / 2);
        debugRenderer = new Box2DDebugRenderer();
        camera = new OrthographicCamera(160, 90);
        camera.position.set(WORLD_WIDTH/2,WORLD_HEIGHT/2,0);
    }

    @Override
    public void render() {
        camera.update();
        world.step(1f / 60f, 6, 2);

        adjustWaiterSprite();

        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(camera.combined);
        Matrix4 debugMatrix = batch.getProjectionMatrix().cpy().scale(PIXELS_TO_METERS,
                PIXELS_TO_METERS, 0);
        waiter.move(2.0f);
        batch.begin();
            drawWaiter();
            drawTables();
        batch.end();
        if(true) debugRenderer.render(world, debugMatrix);
    }

    private void adjustWaiterSprite(){
        ((Sprite)waiter.getBody().getUserData()).setPosition(
                (waiter.getBody().getPosition().x * PIXELS_TO_METERS) - waiter.getSprite().getWidth() / 2,
                (waiter.getBody().getPosition().y * PIXELS_TO_METERS) - waiter.getSprite().getHeight() / 2
        );
    }

    private void drawWaiter(){
        batch.draw(waiter.getSprite(),
                waiter.getSprite().getX(),
                waiter.getSprite().getY(),
                waiter.getSprite().getWidth(),
                waiter.getSprite().getHeight()
        );
    }

    private void drawTables(){
        batch.draw(table.getSprite(),
                table.getSprite().getX(),
                table.getSprite().getY(),
                table.getSprite().getWidth(),
                table.getSprite().getHeight()
        );
    }

    @Override
    public void dispose() {
        world.dispose();
    }
}
