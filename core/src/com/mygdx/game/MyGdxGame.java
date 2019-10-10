package com.mygdx.game;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
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
import screens.GameScreen;
import screens.MenuScreen;

import static com.mygdx.game.MyGdxGame.WORLD_HEIGHT;
import static com.mygdx.game.MyGdxGame.WORLD_WIDTH;

import java.util.Iterator;
import java.util.Map;

public class MyGdxGame extends Game {
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
	private ShapeRenderer shapeRenderer;
	private OrthographicCamera camera;
	private GameScreen gameScreen;
	private MenuScreen menuScreen;
	public static final float PIXELS_TO_METERS = 50f;
	public static final float WORLD_WIDTH = 160;
	public static final float WORLD_HEIGHT = 90;
	

	@Override
	public void create() {
		Assets.load();
		Assets.manager.finishLoading();
		batch = new SpriteBatch();
		shapeRenderer = new ShapeRenderer();
		menuScreen = new MenuScreen(this, batch);
		this.setScreen(menuScreen);
		camera = new OrthographicCamera(WORLD_WIDTH, WORLD_HEIGHT);
		camera.position.set(WORLD_WIDTH / 2, WORLD_HEIGHT / 2, 0);
		camera.update();
		batch.setProjectionMatrix(camera.combined);
		shapeRenderer.setProjectionMatrix(camera.combined);
	}

	@Override
	public void render() {
		super.render();
	}

	@Override
	public void dispose() {
		Assets.dispose();
		batch.dispose();
		shapeRenderer.dispose();
	}
	
	public void startLevel(int level) {
		String levelname = "level" + level;
		gameScreen = new GameScreen(this, batch, shapeRenderer, camera, levelname);
		this.setScreen(gameScreen);
		menuScreen.dispose();
	}
	
	public void showMenu() {
		menuScreen = new MenuScreen(this, batch);
		this.setScreen(menuScreen);
		gameScreen.dispose();
		
	}


}
