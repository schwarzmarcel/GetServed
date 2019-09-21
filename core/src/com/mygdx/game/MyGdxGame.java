package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import entities.Level;
import entities.Table;
import entities.Waiter;
import entities.Walls;

public class MyGdxGame extends ApplicationAdapter {

    private SpriteBatch batch;
    private Box2DDebugRenderer debugRenderer;
    private OrthographicCamera camera;
    private Walls walls;
    private Level level;

    public static final float PIXELS_TO_METERS = 50f;
    public static final float WORLD_WIDTH = 160;
    public static final float WORLD_HEIGHT = 90;

    @Override
    public void create() {
        batch = new SpriteBatch();
        level = new Level(1,0,1);
        level.initializeLevel();

        debugRenderer = new Box2DDebugRenderer();
        camera = new OrthographicCamera(160, 90);
        camera.position.set(WORLD_WIDTH/2,WORLD_HEIGHT/2,0);
    }

    @Override
    public void render() {
        camera.update();
        level.getWorld().step(1f / 60f, 6, 2);

        adjustWaiterSprite();

        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(camera.combined);
        Matrix4 debugMatrix = batch.getProjectionMatrix().cpy().scale(PIXELS_TO_METERS,
                PIXELS_TO_METERS, 0);
        level.getWaiter().move(2.0f);
        batch.begin();
            drawWaiter();
            drawTables();
        batch.end();
        if(true) debugRenderer.render(level.getWorld(), debugMatrix);
    }

    private void adjustWaiterSprite(){
        ((Sprite)level.getWaiter().getBody().getUserData()).setPosition(
                (level.getWaiter().getBody().getPosition().x * PIXELS_TO_METERS) - level.getWaiter().getSprite().getWidth() / 2,
                (level.getWaiter().getBody().getPosition().y * PIXELS_TO_METERS) - level.getWaiter().getSprite().getHeight() / 2
        );
    }

    private void drawWaiter(){
        batch.draw(level.getWaiter().getSprite(),
                level.getWaiter().getSprite().getX(),
                level.getWaiter().getSprite().getY(),
                level.getWaiter().getSprite().getWidth(),
                level.getWaiter().getSprite().getHeight()
        );
    }

    private void drawTables(){
        for (Table t: level.getTables()
             ) {
            batch.draw(t.getSprite(),
                    t.getSprite().getX(),
                    t.getSprite().getY(),
                    t.getSprite().getWidth(),
                    t.getSprite().getHeight()
            );
        }
    }

    @Override
    public void dispose() {
        level.getWorld().dispose();
    }
}
