package com.yungying.game.map;

import com.badlogic.gdx.graphics.Texture;
import com.yungying.game.Main;

import java.util.Vector;

public class Map1 {
    private final Texture grass;
    private final Texture background;
    private final Vector<Tile> tiles;
    private  Tile currentTile;
    private final float initialX;
    private final float initialY;

    Main game;

    public Map1(float initialX, float initialY) {
        this.initialX = initialX;
        this.initialY = initialY;
        grass = new Texture("Grass.png");
        background = new Texture("Background.png");
        tiles = new Vector<Tile>(50);
        tiles.add(new Tile("grass", 0 + initialX, 0 + initialY,2));
        tiles.add(new Tile("grass", 128 + initialX, 0 + initialY,2));
        tiles.add(new Tile("grass", 256 + initialX, 0 + initialY,2));
        tiles.add(new Tile("grass", 384 + initialX, 0 + initialY,2));
        tiles.add(new Tile("grass", 512 + initialX, 0 + initialY,2));
        tiles.add(new Tile("grass", 640 + initialX, 0 + initialY,2));
        tiles.add(new Tile("null", 768 + initialX, 0 + initialY,2));
        tiles.add(new Tile("grass", 896 + initialX, 0 + initialY,2));
        tiles.add(new Tile("grass", 1024 + initialX, 0 + initialY,2));
        tiles.add(new Tile("grass", 1152 + initialX, 0 + initialY,2));
        tiles.add(new Tile("grass", 1280 + initialX, 0 + initialY,2));
        tiles.add(new Tile("null", 1408 + initialX, 0 + initialY,2));
        tiles.add(new Tile("null", 1536 + initialX, 0 + initialY,2));
        tiles.add(new Tile("grass", 1664 + initialX, 0 + initialY,2));
        tiles.add(new Tile("grass", 1792 + initialX, 0 + initialY,2));
        tiles.add(new Tile("grass", 1920 + initialX, 0 + initialY,2));
        tiles.add(new Tile("grass", 2048 + initialX, 0 + initialY,2));
        tiles.add(new Tile("grass", 2176 + initialX, 0 + initialY,2));
        tiles.add(new Tile("null", 2304 + initialX, 0 + initialY,2));
        tiles.add(new Tile("null", 2432 + initialX, 0 + initialY,2));
        tiles.add(new Tile("grass", 2560 + initialX, 64 + initialY,2));
        tiles.add(new Tile("grass", 2688 + initialX, 64 + initialY,2));
        tiles.add(new Tile("grass", 2816 + initialX, 64 + initialY,2));
        tiles.add(new Tile("grass", 2944 + initialX, 64 + initialY,2));
        tiles.add(new Tile("grass", 3072 + initialX, 64 + initialY,2));
        tiles.add(new Tile("null", 3200 + initialX, 64 + initialY,2));
        tiles.add(new Tile("null", 3328 + initialX, 64 + initialY,2));
        tiles.add(new Tile("grass", 3456 + initialX, 128 + initialY,2));
        tiles.add(new Tile("grass", 3584 + initialX, 128 + initialY,2));
        tiles.add(new Tile("grass", 3712 + initialX, 128 + initialY,2));
        tiles.add(new Tile("grass", 3840 + initialX, 128 + initialY,2));
        tiles.add(new Tile("grass", 3968 + initialX, 128 + initialY,2));
        tiles.add(new Tile("grass", 4096 + initialX, 128 + initialY,2));
        tiles.add(new Tile("null", 4224 + initialX, 128 + initialY,2));
        tiles.add(new Tile("grass", 4352 + initialX, 0 + initialY,2));
        tiles.add(new Tile("grass", 4480 + initialX, 0 + initialY,2));
        tiles.add(new Tile("grass", 4608 + initialX, 0 + initialY,2));
        tiles.add(new Tile("grass", 4736 + initialX, 0 + initialY,2));
        tiles.add(new Tile("grass", 4864 + initialX, 0 + initialY,2));
        tiles.add(new Tile("null", 4992 + initialX, 0 + initialY,2));
        tiles.add(new Tile("null", 5120 + initialX, 0 + initialY,2));
        tiles.add(new Tile("grass", 5248 + initialX, 0 + initialY,2));
        tiles.add(new Tile("grass", 5376 + initialX, 0 + initialY,2));
    }

    public Texture getGrass() {
        return grass;
    }

    public Vector<Tile> getTiles() {
        return tiles;
    }

    public Tile getCurrentTile(float playerX) {
        int index = (int) (playerX - initialX) / 128;
        if(index < 0 || index >= tiles.size()) return null;
        currentTile = tiles.elementAt(index);
        return tiles.elementAt(index);
    }


    public Texture getBackground() {
        return background;
    }

    public void addTile(String type, float x, float y, float zoom) {
        tiles.add(new Tile(type, x + 128, y, zoom));
    }


    public boolean isColliding(float playerX, float playerY) {
        return currentTile.isColliding(playerX, playerY);
    }

    public void dispose() {
        grass.dispose();
        background.dispose();
    }
}
