package screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.MyGdxGame;

import static com.mygdx.game.MyGdxGame.WORLD_HEIGHT;
import static com.mygdx.game.MyGdxGame.WORLD_WIDTH;

public class MenuScreen implements Screen{
	
	private MyGdxGame game;
	private SpriteBatch batch;
	private Texture test;
	
	public MenuScreen(MyGdxGame game, SpriteBatch batch) {
		this.game = game;
		this.batch = batch;
		test = new Texture("badlogic.jpg");
	}
	
	@Override
	public void show() {
		
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		batch.draw(test, 0, 0, WORLD_WIDTH / 8, WORLD_HEIGHT / 4.5f);
		if(Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) 
			game.startLevel(1);
		if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE))
			game.startLevel(2);
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
		test.dispose();
	}

}
