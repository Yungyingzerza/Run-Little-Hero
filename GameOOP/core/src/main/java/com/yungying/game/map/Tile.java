package com.yungying.game.map;

public class Tile {
    private final float startX;
    private final float endX;
    private final float startY;
    private final float endY;
    private final BlockType type;
    private final float zoom;

    public Tile(BlockType type, float x, float y, float zoom) {
        this.type = type;
        this.startX = x;
        this.endX = x + 128;
        this.startY = y;
        this.endY = y + 128;
        this.zoom = zoom;
    }

    public float getStartX() {
        return startX;
    }

    public float getStartY() {
        return startY;
    }

    public float getEndX() {
        return endX;
    }

    public float getEndY() {
        return endY;
    }

    public float getZoom() {
        return zoom;
    }

    public BlockType getType() {
        return type;
    }

    public boolean isColliding(float playerY) {
        return ((playerY + 64) >= endY || (playerY + 64)/endY >= 0.98f)  && !type.equals(BlockType.Air);
    }

}
