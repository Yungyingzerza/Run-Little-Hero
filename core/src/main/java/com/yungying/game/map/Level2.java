package com.yungying.game.map;

import com.badlogic.gdx.graphics.Texture;

import java.util.Vector;

public class Level2 implements Map {
    private final Texture grass;
    private final Texture background;
    private final Vector<Tile> tiles;
    private Tile currentTile;
    private final float initialX;
    private final float mapSpeed;
    private final String nextMap;

    public Level2(float initialX) {
        this.initialX = initialX;
        grass = new Texture("GrassWinter.png");
        background = new Texture("Background2.png");
        nextMap = "Level1";
        this.mapSpeed = 500;
        tiles = new Vector<Tile>(50);
        tiles.add(new Tile("GrassWinter", 0 + initialX, 0 ,2));
        tiles.add(new Tile("GrassWinter", 128 + initialX, 0 ,2));
        tiles.add(new Tile("GrassWinter", 256 + initialX, 0 ,2));
        tiles.add(new Tile("GrassWinter", 384 + initialX, 0 ,2));
        tiles.add(new Tile("GrassWinter", 512 + initialX, 0 ,2));
        tiles.add(new Tile("GrassWinter", 640 + initialX, 0 ,2));
        tiles.add(new Tile("null", 768 + initialX, 0 ,2));
        tiles.add(new Tile("GrassWinter", 896 + initialX, 0 ,2));
        tiles.add(new Tile("GrassWinter", 1024 + initialX, 0 ,2));
        tiles.add(new Tile("GrassWinter", 1152 + initialX, 0 ,2));
        tiles.add(new Tile("GrassWinter", 1280 + initialX, 0 ,2));
        tiles.add(new Tile("null", 1408 + initialX, 0 ,2));
        tiles.add(new Tile("null", 1536 + initialX, 0 ,2));
        tiles.add(new Tile("GrassWinter", 1664 + initialX, 0 ,2));
        tiles.add(new Tile("GrassWinter", 1792 + initialX, 0 ,2));
        tiles.add(new Tile("GrassWinter", 1920 + initialX, 0 ,2));
        tiles.add(new Tile("GrassWinter", 2048 + initialX, 0 ,2));
        tiles.add(new Tile("GrassWinter", 2176 + initialX, 0 ,2));
        tiles.add(new Tile("null", 2304 + initialX, 0 ,2));
        tiles.add(new Tile("null", 2432 + initialX, 0 ,2));
        tiles.add(new Tile("GrassWinter", 2560 + initialX, 64 ,2));
        tiles.add(new Tile("GrassWinter", 2688 + initialX, 64 ,2));
        tiles.add(new Tile("GrassWinter", 2816 + initialX, 64 ,2));
        tiles.add(new Tile("GrassWinter", 2944 + initialX, 64 ,2));
        tiles.add(new Tile("GrassWinter", 3072 + initialX, 64 ,2));
        tiles.add(new Tile("null", 3200 + initialX, 64 ,2));
        tiles.add(new Tile("null", 3328 + initialX, 64 ,2));
        tiles.add(new Tile("GrassWinter", 3456 + initialX, 128 ,2));
        tiles.add(new Tile("GrassWinter", 3584 + initialX, 128 ,2));
        tiles.add(new Tile("GrassWinter", 3712 + initialX, 128 ,2));
        tiles.add(new Tile("GrassWinter", 3840 + initialX, 128 ,2));
        tiles.add(new Tile("GrassWinter", 3968 + initialX, 128 ,2));
        tiles.add(new Tile("GrassWinter", 4096 + initialX, 128 ,2));
        tiles.add(new Tile("null", 4224 + initialX, 128 ,2));
        tiles.add(new Tile("GrassWinter", 4352 + initialX, 0 ,2));
        tiles.add(new Tile("GrassWinter", 4480 + initialX, 0 ,2));
        tiles.add(new Tile("GrassWinter", 4608 + initialX, 0 ,2));
        tiles.add(new Tile("GrassWinter", 4736 + initialX, 0 ,2));
        tiles.add(new Tile("GrassWinter", 4864 + initialX, 0 ,2));
        tiles.add(new Tile("null", 4992 + initialX, 0 ,2));
        tiles.add(new Tile("null", 5120 + initialX, 0 ,2));
        tiles.add(new Tile("GrassWinter", 5248 + initialX, 0 ,2));
        tiles.add(new Tile("GrassWinter", 5376 + initialX, 0 ,2));
    }

    public Texture getTile() {
        return grass;
    }

    public Vector<Tile> getTiles() {
        return tiles;
    }

    public float getMapSpeed() {
        return mapSpeed;
    }

    public Tile getCurrentTile(float playerX) {
        int index = ((int) (playerX - initialX) / 128) % tiles.size();
        if(index < 0 || index >= tiles.size()) return null;
        currentTile = tiles.elementAt(index);
        return tiles.elementAt(index);
    }

    public Texture getBackground() {
        return background;
    }


    public boolean isColliding(float playerX, float playerY) {
        return currentTile.isColliding(playerX, playerY);
    }

    public Tile getLastTile() {
        return tiles.lastElement();
    }

    public Tile getFirstTile() {
        return tiles.firstElement();
    }

    public String getNextMap() {
        return nextMap;
    }

    public void dispose() {
        grass.dispose();
        background.dispose();
    }
}
