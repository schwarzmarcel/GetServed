package entities;

import com.badlogic.gdx.physics.box2d.*;

public class Box {
    private BodyDef bodyDef;
    private float positionX;
    private float positionY;
    private float boxWidth;
    private float boxHeight;
    private Body body;

    public Box(boolean isDynamic, float positionX, float positionY, float boxWidth, float boxHeight, World world) {
        this.positionX = positionX;
        this.positionY = positionY;
        this.boxHeight = boxHeight;
        this.boxWidth = boxWidth;
        BodyDef bodyDef = new BodyDef();
        if(isDynamic){
            bodyDef.type = BodyDef.BodyType.DynamicBody;
        }else {
            bodyDef.type = BodyDef.BodyType.StaticBody;
        }
        bodyDef.position.set(positionX, positionY);
        body = world.createBody(bodyDef);
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(boxWidth, boxHeight);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1f;
        body.createFixture(fixtureDef);
        shape.dispose();
    }

    public double getPositionX() {
        return positionX;
    }

    public void setPositionX(float positionX) {
        this.positionX = positionX;
    }

    public double getPositionY() {
        return positionY;
    }

    public void setPositionY(float positionY) {
        this.positionY = positionY;
    }

    public BodyDef getBodyDef() {
        return bodyDef;
    }

    public Body getBody() {
        return body;
    }

    public float getBoxWidth() {
        return boxWidth;
    }

    public void setBoxWidth(float boxWidth) {
        this.boxWidth = boxWidth;
    }

    public float getBoxHeight() {
        return boxHeight;
    }

    public void setBoxHeight(float boxHeight) {
        this.boxHeight = boxHeight;
    }
}
