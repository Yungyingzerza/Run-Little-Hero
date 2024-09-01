package com.yungying.game.map;

public class Jelly {
    private final String type;
    private final float x;
    private final float y;

    public Jelly(String type, float x, float y) {
        this.type = type;
        this.x = x;
        this.y = y;
    }

    public String getType() {
        return type;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }
}
