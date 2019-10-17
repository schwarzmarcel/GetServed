package entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import handlers.Assets;

import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

public class Guest {
    private Animation<TextureRegion> idleAnimation;
    private Animation<TextureRegion> angryAnimation;
    private Animation<TextureRegion> orderAnimation;
    private Animation<TextureRegion> activeAnimation;
    private String currentAnimation;
    private int lastAnimationTime = 0;
    private int despawnTime = 0;
    private boolean justOrdered = false;
    private boolean isServed = false;
    private Table table;
    private String type;
    private Dish dish;
    private Sprite bubble;
    private long spawnTime;
    private long orderTime;
    private float patience;
    private float dynamicPatience;
    private float maxPatience;
    private long wealth;
    private Foodtype order;
    private float[] position = new float[2];

    public Guest(long spawnTime, long patience, long wealth, String type) {
        int randomNum = ThreadLocalRandom.current().nextInt(1, 5 + 1);
        TextureAtlas textureAtlasIdle = null;
        TextureAtlas textureAtlasAngry = null;
        TextureAtlas textureAtlasOrdering = null;
        if (type.equals("normal")) {
            switch (randomNum) {
                case 1:
                    textureAtlasIdle = Assets.manager.get(Assets.GUEST1, TextureAtlas.class);
                    textureAtlasAngry = Assets.manager.get(Assets.GUEST1_ORDERING, TextureAtlas.class);
                    textureAtlasOrdering = Assets.manager.get(Assets.GUEST1_ANGRY, TextureAtlas.class);
                    break;
                case 2:
                    textureAtlasIdle = Assets.manager.get(Assets.GUEST2, TextureAtlas.class);
                    textureAtlasAngry = Assets.manager.get(Assets.GUEST2_ORDERING, TextureAtlas.class);
                    textureAtlasOrdering = Assets.manager.get(Assets.GUEST2_ANGRY, TextureAtlas.class);
                    break;
                case 3:
                    textureAtlasIdle = Assets.manager.get(Assets.GUEST3, TextureAtlas.class);
                    textureAtlasAngry = Assets.manager.get(Assets.GUEST3_ORDERING, TextureAtlas.class);
                    textureAtlasOrdering = Assets.manager.get(Assets.GUEST3_ANGRY, TextureAtlas.class);
                    break;
                case 4:
                    textureAtlasIdle = Assets.manager.get(Assets.GUEST4, TextureAtlas.class);
                    textureAtlasAngry = Assets.manager.get(Assets.GUEST4_ORDERING, TextureAtlas.class);
                    textureAtlasOrdering = Assets.manager.get(Assets.GUEST4_ANGRY, TextureAtlas.class);
                    break;
                case 5:
                    textureAtlasIdle = Assets.manager.get(Assets.GUEST5, TextureAtlas.class);
                    textureAtlasAngry = Assets.manager.get(Assets.GUEST5_ORDERING, TextureAtlas.class);
                    textureAtlasOrdering = Assets.manager.get(Assets.GUEST5_ANGRY, TextureAtlas.class);
                    break;
                case 6:
                default:
                    break;
            }
        } else if (type.equals("king")) {
            textureAtlasIdle = Assets.manager.get(Assets.KING, TextureAtlas.class);
            textureAtlasAngry = Assets.manager.get(Assets.KING_KICK, TextureAtlas.class);
            textureAtlasOrdering = Assets.manager.get(Assets.KING_JUMP, TextureAtlas.class);
        }

        idleAnimation = new Animation<TextureRegion>(0.045f, textureAtlasIdle.getRegions(), Animation.PlayMode.LOOP);
        angryAnimation = new Animation<TextureRegion>(0.05f, textureAtlasAngry.getRegions(), Animation.PlayMode.LOOP);
        orderAnimation = new Animation<TextureRegion>(0.07f, textureAtlasOrdering.getRegions(), Animation.PlayMode.LOOP);
        this.activeAnimation = idleAnimation;
        this.currentAnimation = "idle";
        order = Foodtype.getRandomFoodType();
        dish = new Dish(order);
        bubble = new Sprite(Assets.manager.get(Assets.BUBBLE, Texture.class));
        bubble.setSize(8, 8);
        this.spawnTime = spawnTime;
        this.orderTime = spawnTime + 1;
        this.type = type;
        randomNum = ThreadLocalRandom.current().nextInt((int) (0.2 * patience) * (-1), (int) (0.2 * patience) + 1);
        this.patience = patience + randomNum;
        this.maxPatience = patience + randomNum;
        randomNum = ThreadLocalRandom.current().nextInt((int) (0.2 * wealth) * (-1), (int) (0.2 * wealth) + 1);
        this.wealth = wealth + randomNum;
    }

    public void receivedWrongDish(int time) {
        patience = patience - ((dynamicPatience / 100) * 25);
        setOrderTime(time);
    }

    public int getTip() {
        float tip = (wealth * (dynamicPatience / maxPatience));
        return (int) Math.ceil(tip);
    }

    public void calculatePatience(long timeElapsed) {
        dynamicPatience = (patience - timeElapsed * 3);
        if (dynamicPatience < 0) {
            dynamicPatience = 0;
        }
    }

    public void setPosition(float positionX, float positionY) {
        position[0] = positionX;
        position[1] = positionY;
        float[] position = {positionX + dish.getSprite().getWidth() + 2, positionY + 4};
        dish.setPosition(position);
        bubble.setPosition(positionX + dish.getSprite().getWidth(), positionY + 2);
    }

    public void setDish(Dish dish) {
        this.dish = dish;
    }
    public long getSpawnTime() {
        return spawnTime;
    }

    public long getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(long orderTime) {
        this.orderTime = orderTime;
    }

    public float getPatience() {
        return dynamicPatience;
    }

    public Table getTable() {
        return table;
    }

    public void setTable(Table table) {
        this.table = table;
    }

    public Dish getDish() {
        return dish;
    }

    public float getMaxPatience() {
        return maxPatience;
    }

    public Foodtype getOrder() {
        return order;
    }

    public Sprite getBubble() {
        return bubble;
    }

    public Animation<TextureRegion> getIdleAnimation() {
        return idleAnimation;
    }

    public float[] getPosition() {
        return position;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Animation<TextureRegion> getActiveAnimation() {
        return activeAnimation;
    }

    public void setActiveAnimation(String animation, int time) {
        switch (animation) {
            case "idle":
                this.activeAnimation = this.idleAnimation;
                this.currentAnimation = "idle";
                this.lastAnimationTime = time;
                break;
            case "ordering":
                this.activeAnimation = this.orderAnimation;
                this.currentAnimation = "ordering";
                this.lastAnimationTime = time;
                break;
            case "angry":
                this.activeAnimation = this.angryAnimation;
                this.currentAnimation = "angry";
                this.lastAnimationTime = time;
        }

    }

    public String getCurrentAnimation() {
        return currentAnimation;
    }

    public int getLastAnimationTime() {
        return lastAnimationTime;
    }

    public boolean isJustOrdered() {
        return justOrdered;
    }

    public void setJustOrdered(boolean justOrdered) {
        this.justOrdered = justOrdered;
    }

    public int getDespawnTime() {
        return despawnTime;
    }

    public void setDespawnTime(int despawnTime) {
        this.despawnTime = despawnTime;
    }

    public boolean isServed() {
        return isServed;
    }

    public void setServed(boolean served) {
        isServed = served;
    }

    @Override
    public String toString() {
        return "Guest{" +
                "spawnTime=" + spawnTime +
                ", patience=" + patience +
                ", wealth=" + wealth +
                ", order=" + order +
                ", position=" + Arrays.toString(position) +
                '}';
    }
}
