package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import entities.Box;
import entities.Waiter;

public class MyGdxGame extends ApplicationAdapter {

    private SpriteBatch batch;
    private World world;
    private Box2DDebugRenderer debugRenderer;
    private OrthographicCamera camera;
    private Waiter waiter;
    private Waiter waiter2;
    public static final float PIXELS_TO_METERS = 100f;

    @Override
    public void create() {
        batch = new SpriteBatch();
        world = new World(new Vector2(0, 0), true);
        waiter = new Waiter(world, Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);
        waiter2 = new Waiter(world, Gdx.graphics. getWidth() / 4, Gdx.graphics.getHeight() / 2) ;

        debugRenderer = new Box2DDebugRenderer();
        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.
                getHeight());
        camera.translate(camera.viewportWidth / 2, camera.viewportHeight / 2);
       
    }
    @Override
    public void render() {
        camera.update();
        world.step(1f / 60f, 6, 2);

        ((Sprite)waiter.getBody().getUserData()).setPosition(
                (waiter.getBody().getPosition().x * PIXELS_TO_METERS) - waiter.getSprite().getWidth() / 2,
                (waiter.getBody().getPosition().y * PIXELS_TO_METERS) - waiter.getSprite().getHeight() / 2
        );

        ((Sprite)waiter2.getBody().getUserData()).setPosition(
                (waiter2.getBody().getPosition().x * PIXELS_TO_METERS) - waiter2.getSprite().getWidth() / 2,
                (waiter2.getBody().getPosition().y * PIXELS_TO_METERS) - waiter2.getSprite().getHeight() / 2
        );

        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(camera.combined);
        Matrix4 debugMatrix = batch.getProjectionMatrix().cpy().scale(PIXELS_TO_METERS,
                PIXELS_TO_METERS, 0);
        waiter.move();
        batch.begin();
        batch.draw(waiter.getSprite(),
                waiter.getSprite().getX(),
                waiter.getSprite().getY(),
                waiter.getSprite().getWidth(),
                waiter.getSprite().getHeight()
        );
        batch.draw(waiter2.getSprite(),
                waiter2.getSprite().getX(),
                waiter2.getSprite().getY(),
                waiter.getSprite().getWidth(),
                waiter.getSprite().getHeight()
        );
        batch.end();
        if(true) debugRenderer.render(world, debugMatrix);
    }

    @Override
    public void dispose() {
        world.dispose();
    }
}
