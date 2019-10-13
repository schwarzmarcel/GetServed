package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import handlers.Assets;
import screens.GameScreen;
import screens.MenuScreen;

public class MyGdxGame extends Game {
	// TODO: improve visual feedback for guest and dish spawning / Guest movement?
	// TODO: maybe improve guest patience visualisation
	// TODO: improve overall logic for dish spawning
	// TODO: add a visual queue for guests
	// TODO: proper sprites

	private SpriteBatch batch;
	private ShapeRenderer shapeRenderer;
	private OrthographicCamera camera;
	private GameScreen gameScreen;
	private MenuScreen menuScreen;
	private int levelcount;
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
		levelcount = 1;
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
	
	public void startLevel() {
		String levelname = "level" + levelcount;
		gameScreen = new GameScreen(this, batch, shapeRenderer, camera, levelname);
		this.setScreen(gameScreen);
	}
	
	public void showMenu() {
		menuScreen = new MenuScreen(this, batch);
		gameScreen.dispose();
		this.setScreen(menuScreen);
	}
	
	public void increaseLevel() {
		if(levelcount == 1)
			levelcount++;
	}


}
