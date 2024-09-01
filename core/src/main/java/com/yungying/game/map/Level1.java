package com.yungying.game.map;

import com.badlogic.gdx.graphics.Texture;
import com.yungying.game.Main;

import java.util.Vector;

public class Level1 implements Map {
    private final Texture grass;
    private final Texture background;
    private final Vector<Tile> tiles;
    private Tile currentTile;
    private final float initialX;
    private final float mapSpeed;
    private final String nextMap;

    public Level1(float initialX) {
        this.initialX = initialX;
        grass = new Texture("Grass.png");
        background = new Texture("Background.png");
        nextMap = "Level2";
        this.mapSpeed = 300;
        tiles = new Vector<Tile>(50);
        tiles.add(new Tile("Grass", 0 + initialX, 0 ,2));
        tiles.add(new Tile("Grass", 128 + initialX, 0 ,2));
        tiles.add(new Tile("Grass", 256 + initialX, 0 ,2));
        tiles.add(new Tile("Grass", 384 + initialX, 0 ,2));
        tiles.add(new Tile("Grass", 512 + initialX, 0 ,2));
        tiles.add(new Tile("Grass", 640 + initialX, 0 ,2));
        tiles.add(new Tile("null", 768 + initialX, 0 ,2));
        tiles.add(new Tile("Grass", 896 + initialX, 0 ,2));
        tiles.add(new Tile("Grass", 1024 + initialX, 0 ,2));
        tiles.add(new Tile("Grass", 1152 + initialX, 0 ,2));
        tiles.add(new Tile("Grass", 1280 + initialX, 0 ,2));
        tiles.add(new Tile("null", 1408 + initialX, 0 ,2));
        tiles.add(new Tile("null", 1536 + initialX, 0 ,2));
        tiles.add(new Tile("Grass", 1664 + initialX, 0 ,2));
        tiles.add(new Tile("Grass", 1792 + initialX, 0 ,2));
        tiles.add(new Tile("Grass", 1920 + initialX, 0 ,2));
        tiles.add(new Tile("Grass", 2048 + initialX, 0 ,2));
        tiles.add(new Tile("Grass", 2176 + initialX, 0 ,2));
        tiles.add(new Tile("null", 2304 + initialX, 0 ,2));
        tiles.add(new Tile("null", 2432 + initialX, 0 ,2));
        tiles.add(new Tile("Grass", 2560 + initialX, 64 ,2));
        tiles.add(new Tile("Grass", 2688 + initialX, 64 ,2));
        tiles.add(new Tile("Grass", 2816 + initialX, 64 ,2));
        tiles.add(new Tile("Grass", 2944 + initialX, 64 ,2));
        tiles.add(new Tile("Grass", 3072 + initialX, 64 ,2));
        tiles.add(new Tile("null", 3200 + initialX, 64 ,2));
        tiles.add(new Tile("null", 3328 + initialX, 64 ,2));
        tiles.add(new Tile("Grass", 3456 + initialX, 128 ,2));
        tiles.add(new Tile("Grass", 3584 + initialX, 128 ,2));
        tiles.add(new Tile("Grass", 3712 + initialX, 128 ,2));
        tiles.add(new Tile("Grass", 3840 + initialX, 128 ,2));
        tiles.add(new Tile("Grass", 3968 + initialX, 128 ,2));
        tiles.add(new Tile("Grass", 4096 + initialX, 128 ,2));
        tiles.add(new Tile("null", 4224 + initialX, 128 ,2));
        tiles.add(new Tile("Grass", 4352 + initialX, 0 ,2));
        tiles.add(new Tile("Grass", 4480 + initialX, 0 ,2));
        tiles.add(new Tile("Grass", 4608 + initialX, 0 ,2));
        tiles.add(new Tile("Grass", 4736 + initialX, 0 ,2));
        tiles.add(new Tile("Grass", 4864 + initialX, 0 ,2));
        tiles.add(new Tile("null", 4992 + initialX, 0 ,2));
        tiles.add(new Tile("null", 5120 + initialX, 0 ,2));
        tiles.add(new Tile("Grass", 5248 + initialX, 0 ,2));
        tiles.add(new Tile("Grass", 5376 + initialX, 0 ,2));
    }

    @Override
    public Texture getTile() {
        return grass;
    }

    @Override
    public Vector<Tile> getTiles() {
        return tiles;
    }

    @Override
    public float getMapSpeed() {
        return mapSpeed;
    }

    @Override
    public Tile getCurrentTile(float playerX) {
        int index = ((int) (playerX - initialX) / 128) % tiles.size();
        if(index < 0 || index >= tiles.size()) return null;
        currentTile = tiles.elementAt(index);
        return tiles.elementAt(index);
    }

    @Override
    public Texture getBackground() {
        return background;
    }


    @Override
    public boolean isColliding(float playerX, float playerY) {
        return currentTile.isColliding(playerX, playerY);
    }

    @Override
    public Tile getLastTile() {
        return tiles.lastElement();
    }

    @Override
    public Tile getFirstTile() {
        return tiles.firstElement();
    }

    @Override
    public String getNextMap() {
        return nextMap;
    }

    @Override
    public void dispose() {
        grass.dispose();
        background.dispose();
    }
}
