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

    public int isColliding(float playerX, float playerY) {
        if(playerX < x + 64 && playerX + 64 > x && playerY < y && playerY + 128 > y){

            if("Coin".equals(type)){
                return 50;
            }else if("Cherry".equals(type)){
                return 100;
            }else {
                return 0;
            }

        }else{
            return 0;
        }
    }
}
