package entities;

import java.util.Random;

public enum Foodtype {
    BURGER,
    PIZZA,
    PASTA;

    public static Foodtype getRandomFoodType() {
        Random random = new Random();
        return values()[random.nextInt(values().length)];
    }

}
