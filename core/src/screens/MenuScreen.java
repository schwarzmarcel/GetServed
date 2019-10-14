package screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.MyGdxGame;
import handlers.Assets;

import static com.mygdx.game.MyGdxGame.WORLD_HEIGHT;
import static com.mygdx.game.MyGdxGame.WORLD_WIDTH;

public class MenuScreen implements Screen{
	
	private MyGdxGame game;
	private SpriteBatch batch;
	private Sprite nextactive;
	private Sprite nextpassive;
	private Sprite exitactive;
	private Sprite exitpassive;
	private boolean nextorexit;
	
	public MenuScreen(MyGdxGame game, SpriteBatch batch) {
		this.game = game;
		this.batch = batch;
		nextorexit = true;
        nextactive = new Sprite(Assets.manager.get(Assets.NEXTACTIVE, Texture.class));
		nextactive.setSize(WORLD_WIDTH / 3, WORLD_HEIGHT / 8);
		nextactive.setPosition(WORLD_WIDTH / 2 - (nextactive.getWidth() / 2), WORLD_HEIGHT / 2 - (nextactive.getHeight() / 2) + 15);
        exitactive = new Sprite(Assets.manager.get(Assets.EXITACTIVE, Texture.class));
		exitactive.setSize(WORLD_WIDTH / 7, WORLD_HEIGHT / 8);
		exitactive.setPosition(WORLD_WIDTH / 2 - (exitactive.getWidth() / 2), WORLD_HEIGHT / 2 - (exitactive.getHeight() / 2) - 15);
        nextpassive = new Sprite(Assets.manager.get(Assets.NEXTPASSIVE, Texture.class));
		nextpassive.setSize(WORLD_WIDTH / 3, WORLD_HEIGHT / 8);
		nextpassive.setPosition(WORLD_WIDTH / 2 - (nextpassive.getWidth() / 2), WORLD_HEIGHT / 2 - (nextpassive.getHeight() / 2) + 15);
        exitpassive = new Sprite(Assets.manager.get(Assets.EXITPASSIVE, Texture.class));
		exitpassive.setSize(WORLD_WIDTH / 7, WORLD_HEIGHT / 8);
		exitpassive.setPosition(WORLD_WIDTH / 2 - (exitpassive.getWidth() / 2), WORLD_HEIGHT / 2 - (exitpassive.getHeight() / 2) - 15);
	}
	
	@Override
	public void show() {
		nextorexit = true;
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		if(nextorexit) {
			nextactive.draw(batch);
			exitpassive.draw(batch);
		}else {
			nextpassive.draw(batch);
			exitactive.draw(batch);
		}
		batch.end();
		if(Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
			if(nextorexit)
				game.startLevel();
			else
				Gdx.app.exit();
		}
		if(Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
			if(!nextorexit)
				nextorexit = true;
		}
		if(Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
			if(nextorexit)
				nextorexit = false;
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
		
	}

}
