package screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.MyGdxGame;

import handlers.Assets;

import static com.mygdx.game.MyGdxGame.WORLD_HEIGHT;
import static com.mygdx.game.MyGdxGame.WORLD_WIDTH;

public class LoadingScreen implements Screen{
	private MyGdxGame game;
	private SpriteBatch batch;
	private boolean finishLoading;
	private Sprite loading;
	private Sprite pressEnter;
	private Sprite discard;
	private Sprite serve;
	private Sprite sKey;
	private Sprite dKey;
	private Sprite move;
	private Sprite arrows;
	
	
	
	public LoadingScreen(MyGdxGame game, SpriteBatch batch) {
		this.game = game;
		this.batch = batch;
		setFinishLoading(false);
		loading = new Sprite(Assets.manager.get(Assets.LOADING, Texture.class));
		loading.setSize(64, WORLD_HEIGHT / 10);
		loading.setPosition(WORLD_WIDTH / 2 - (loading.getWidth() / 2), 5);
	//	pressEnter = new Sprite(new Texture("continue.png"));
		arrows = new Sprite (Assets.manager.get(Assets.ARROWS, Texture.class));
		arrows.setSize(15, 10);
		arrows.setPosition(40, WORLD_HEIGHT - 25);
		move = new Sprite (Assets.manager.get(Assets.MOVE, Texture.class));
		move.setSize(23, 5);
		move.setPosition(51 + arrows.getWidth(), WORLD_HEIGHT - 24);
		sKey = new Sprite(Assets.manager.get(Assets.SKEY, Texture.class));
		sKey.setSize(7, 7);
		sKey.setPosition(44, WORLD_HEIGHT - 40);
		serve = new Sprite(Assets.manager.get(Assets.SERVE, Texture.class));
		serve.setSize(45, 6);
		serve.setPosition(50 + arrows.getWidth(), WORLD_HEIGHT - 40);
		dKey = new Sprite(Assets.manager.get(Assets.DKEY, Texture.class));
		dKey.setSize(7, 7);
		dKey.setPosition(44, WORLD_HEIGHT - 55);
		discard = new Sprite(Assets.manager.get(Assets.DISCARD, Texture.class));
		discard.setSize(50, 6);
		discard.setPosition(50 + arrows.getWidth(), WORLD_HEIGHT - 55);
		queueAssets();
		
	}
	
	@Override
	public void show() {
		
	}
	
	private void update(float delta) {
		if(Assets.manager.update()) {
			game.showMenu();
		}
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		update(delta);
		batch.begin();
		loading.draw(batch);
		discard.draw(batch);
		serve.draw(batch);
		arrows.draw(batch);
		sKey.draw(batch);
		dKey.draw(batch);
		move.draw(batch);
		batch.end();
		
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

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}
	
	private void queueAssets() {
		Assets.load();
	}

	public boolean isFinishLoading() {
		return finishLoading;
	}

	public void setFinishLoading(boolean finishLoading) {
		this.finishLoading = finishLoading;
	}

}
