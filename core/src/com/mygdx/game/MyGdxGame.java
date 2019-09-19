package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import entities.Box;
import entities.Waiter;

public class MyGdxGame extends ApplicationAdapter {
	private SpriteBatch batch;
	private World world;
	private Waiter waiter;
	
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		world = new World(new Vector2(0, 0), true);
		waiter = new Waiter(world);
	
		
	}

	@Override
	public void render () {
		world.step(Gdx.graphics.getDeltaTime(), 6, 2);
		waiter.getSprite().setPosition(waiter.getBox().getBody().getPosition().x,waiter.getBox().getBody().getPosition().y);
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		move();
		batch.begin();
		batch.draw(waiter.getSprite(), waiter.getSprite().getX(), waiter.getSprite().getY(),Gdx.graphics.getWidth() / 6, Gdx.graphics.getHeight() / 5);
		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		
		
	}
	
	public void move() {
		int horizontalForce = 0;
		
		if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
			horizontalForce += 1;
		}
		if(Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
			horizontalForce -= 1;
		}
		
		waiter.getBox().getBody().setLinearVelocity(horizontalForce * 5000, waiter.getBox().getBody().getLinearVelocity().y);
	}
}
