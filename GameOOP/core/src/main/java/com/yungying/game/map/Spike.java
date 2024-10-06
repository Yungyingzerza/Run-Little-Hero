package com.yungying.game.map;

public class Spike {
    private final SpikeType type;
    private final float x;
    private final float y;

    public Spike(SpikeType type, float x, float y) {
        this.type = type;
        this.x = x;
        this.y = y;
    }

    public SpikeType getType() {
        return type;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public int isColliding(float playerX, float playerY) {

        if(type == SpikeType.LONG_METAL){
            if(playerX < x + 64 && playerX + 64 > x && (playerY == y || playerY <= y + 32) ) {
                return -50;
            }
        }
        if(type == SpikeType.Bird){
            if(playerX < x + 64 && playerX + 64 > x && (playerY == y-80 || playerY >= y-80 ) ) {
                return -50;
            }
        }

        return 0;

    }

}
