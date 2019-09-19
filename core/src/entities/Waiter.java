package entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.World;

public class Waiter {
    private Sprite sprite;
    private TextureAtlas textureAtlas;
    private TextureRegion textureRegion;
    private Box box;

    public Waiter(World world) {
        textureAtlas = new TextureAtlas(Gdx.files.internal("spritesheets/avatarsprites.atlas"));
        textureRegion = textureAtlas.findRegion("6_ATTACK");
        sprite = new Sprite(textureRegion);
        sprite.setPosition(Gdx.graphics.getWidth() / 2 - sprite.getWidth() / 2,
                Gdx.graphics.getHeight() / 2);
        box = new Box(true,sprite.getX(), sprite.getY(), sprite.getWidth()/2,sprite.getHeight()/2,world);
    }

    public Sprite getSprite() {
        return sprite;
    }

    public void setSprite(Sprite sprite) {
        this.sprite = sprite;
    }

    public TextureAtlas getTextureAtlas() {
        return textureAtlas;
    }

    public void setTextureAtlas(TextureAtlas textureAtlas) {
        this.textureAtlas = textureAtlas;
    }

    public TextureRegion getTextureRegion() {
        return textureRegion;
    }

    public void setTextureRegion(TextureRegion textureRegion) {
        this.textureRegion = textureRegion;
    }

    public Box getBox() {
        return box;
    }

    public void setBox(Box box) {
        this.box = box;
    }
}
