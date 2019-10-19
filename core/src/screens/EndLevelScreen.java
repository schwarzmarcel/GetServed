package screens;

import static com.mygdx.game.MyGdxGame.WORLD_HEIGHT;
import static com.mygdx.game.MyGdxGame.WORLD_WIDTH;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.MyGdxGame;

import handlers.Assets;

public class EndLevelScreen implements Screen{

	private MyGdxGame game;
	private SpriteBatch batch;
	private Sprite cleared;
	private Sprite failed;
	private Sprite pressEnter;
	private boolean win = false;
	private Sound applause;
	private Sound boo;
	
	public EndLevelScreen(MyGdxGame game, SpriteBatch batch) {
		this.game = game;
		this.batch = batch;
		cleared = new Sprite(Assets.manager.get(Assets.LEVELCLEARED, Texture.class));
		failed = new Sprite(Assets.manager.get(Assets.LEVELFAILED, Texture.class));
		cleared.setSize(WORLD_WIDTH / 3, WORLD_HEIGHT / 8);
		failed.setSize(WORLD_WIDTH / 3, WORLD_HEIGHT / 8);
		cleared.setPosition(WORLD_WIDTH / 2 - cleared.getWidth() / 2, WORLD_HEIGHT / 2 - cleared.getHeight() / 2 + 7);
		failed.setPosition(WORLD_WIDTH / 2 - failed.getWidth() / 2, WORLD_HEIGHT / 2 - failed.getHeight() / 2 + 7);
		pressEnter = new Sprite(Assets.manager.get(Assets.ENTER, Texture.class));
		pressEnter.setSize(WORLD_WIDTH / 4, WORLD_HEIGHT / 16);
		pressEnter.setPosition(WORLD_WIDTH / 2 - pressEnter.getWidth() / 2, 10);
		
		applause = Assets.manager.get(Assets.APPLAUSE, Sound.class);
		boo = Assets.manager.get(Assets.BOO, Sound.class);
	}
	
	@Override
	public void show() {
		
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		if(win) {
			cleared.draw(batch);
		}else {
			failed.draw(batch);
		}
		pressEnter.draw(batch);
		batch.end();
		
		if(Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
			game.showMenu();
		}
		
	}
	
	public void playEndSound() {
		if(win) {
			long soundId = applause.play();
			applause.setVolume(soundId, 0.7f);
			applause.setPitch(soundId, 1.1f);
		}else {
			long soundId = boo.play();
			boo.setVolume(soundId, 0.5f);
		}
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

	public boolean isWin() {
		return win;
	}

	public void setWin(boolean win) {
		this.win = win;
	}
	
	
}
