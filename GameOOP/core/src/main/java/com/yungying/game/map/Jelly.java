package com.yungying.game.map;

public class Jelly {
    private final ItemType type;
    private final float x;
    private final float y;

    public Jelly(ItemType type, float x, float y) {
        this.type = type;
        this.x = x;
        this.y = y;
    }

    public ItemType getType() {
        return type;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public CollectJelly isColliding(float playerX, float playerY) {
        if(playerX < x + 64 && playerX + 64 > x && playerY - 32 <= y && playerY + 128 >= y){

            if(type == ItemType.Coin){
                return new CollectJelly(ItemType.Coin, 10);
            }else if(type == ItemType.Cherry){
                return new CollectJelly(ItemType.Cherry, 20);
            }else if(type == ItemType.Potion){
                return new CollectJelly(ItemType.Potion, 15);
            }else{
                return new CollectJelly(ItemType.Air, 0);
            }

        }else{
            return new CollectJelly(ItemType.Air, 0);
        }
    }
}
