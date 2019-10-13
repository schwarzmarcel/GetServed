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
	
	
	
	public LoadingScreen(MyGdxGame game, SpriteBatch batch) {
		this.game = game;
		this.batch = batch;
		setFinishLoading(false);
		loading = new Sprite(new Texture("loading.png"));
		loading.setSize(WORLD_WIDTH / 3, WORLD_HEIGHT / 9);
		loading.setPosition(WORLD_WIDTH / 2 - (loading.getWidth() / 2), 10);
	//	pressEnter = new Sprite(new Texture("continue.png"));
		discard = new Sprite(new Texture("discard.png"));
		discard.setSize(WORLD_WIDTH / 3, WORLD_HEIGHT / 15);
		discard.setPosition(WORLD_WIDTH / 2 - (discard.getWidth() / 2), WORLD_HEIGHT / 2 - (discard.getHeight() / 2));
		serve = new Sprite(new Texture("serve.png"));
		serve.setSize(WORLD_WIDTH / 3, WORLD_HEIGHT / 15);
		serve.setPosition(WORLD_WIDTH / 2 - (serve.getWidth() / 2), WORLD_HEIGHT - serve.getHeight() - 15);
//		sKey = new Sprite(new Texture("Keyboard_Black_S.png"));
//		dKey = new Sprite(new Texture("Keyboard_Black_D.png"));
		
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
