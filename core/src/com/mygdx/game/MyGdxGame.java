package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import entities.Box;

public class MyGdxGame extends ApplicationAdapter {
	private SpriteBatch batch;
	private Texture img;
	private World world;
	private Sprite sprite;
	private Box box;
	private TextureAtlas textureAtlas;
	private TextureRegion textureRegion;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		textureAtlas = new TextureAtlas(Gdx.files.internal("spritesheets/avatarsprites.atlas"));
		textureRegion = textureAtlas.findRegion("6_ATTACK");
		sprite = new Sprite(textureRegion);
		sprite.setPosition(Gdx.graphics.getWidth() / 2 - sprite.getWidth() / 2,
				Gdx.graphics.getHeight() / 2);
		world = new World(new Vector2(0, 0), true);
		box = new Box(true,sprite.getX(), sprite.getY(), sprite.getWidth()/2,sprite.getHeight()/2,world);
	}

	@Override
	public void render () {
		world.step(Gdx.graphics.getDeltaTime(), 6, 2);
		sprite.setPosition(box.getBody().getPosition().x,box.getBody().getPosition().y);
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		batch.draw(sprite, sprite.getX(), sprite.getY(),Gdx.graphics.getWidth() / 6, Gdx.graphics.getHeight() / 5);
		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}
}
