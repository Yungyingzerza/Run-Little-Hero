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

    public int isColliding(float playerX, float playerY, boolean isSliding) {

        if(type == SpikeType.LONG_METAL){
            if(playerX < x + 64 && playerX + 64 > x && (playerY <= y + 32 && !(playerY <= y)) ) {
                return -50;
            }
        }
        if(type == SpikeType.Bird){
            if(!isSliding && playerX < x + 64 && playerX + 64 > x && (playerY + 128 >= y && playerY <= y + 64) ) {
                return -50;
            }else if(isSliding && playerX < x + 64 && playerX + 64 > x && (playerY + 64 >= y && playerY <= y + 64) ) {
                return -50;
            }
        }

        return 0;

    }

}
