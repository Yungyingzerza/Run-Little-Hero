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
    private final float mapSpeed;

    Main game;

    public Map1(float initialX, float initialY, String mapType) {
        this.initialX = initialX;
        this.initialY = initialY;
        if(mapType.equals("Grass")){
            grass = new Texture("Grass.png");
            background = new Texture("Background.png");
            this.mapSpeed = 300;
        }else{
            grass = new Texture("GrassWinter.png");
            background = new Texture("Background2.png");
            this.mapSpeed = 700;
        }
        tiles = new Vector<Tile>(50);
        tiles.add(new Tile(mapType, 0 + initialX, 0 + initialY,2));
        tiles.add(new Tile(mapType, 128 + initialX, 0 + initialY,2));
        tiles.add(new Tile(mapType, 256 + initialX, 0 + initialY,2));
        tiles.add(new Tile(mapType, 384 + initialX, 0 + initialY,2));
        tiles.add(new Tile(mapType, 512 + initialX, 0 + initialY,2));
        tiles.add(new Tile(mapType, 640 + initialX, 0 + initialY,2));
        tiles.add(new Tile("null", 768 + initialX, 0 + initialY,2));
        tiles.add(new Tile(mapType, 896 + initialX, 0 + initialY,2));
        tiles.add(new Tile(mapType, 1024 + initialX, 0 + initialY,2));
        tiles.add(new Tile(mapType, 1152 + initialX, 0 + initialY,2));
        tiles.add(new Tile(mapType, 1280 + initialX, 0 + initialY,2));
        tiles.add(new Tile("null", 1408 + initialX, 0 + initialY,2));
        tiles.add(new Tile("null", 1536 + initialX, 0 + initialY,2));
        tiles.add(new Tile(mapType, 1664 + initialX, 0 + initialY,2));
        tiles.add(new Tile(mapType, 1792 + initialX, 0 + initialY,2));
        tiles.add(new Tile(mapType, 1920 + initialX, 0 + initialY,2));
        tiles.add(new Tile(mapType, 2048 + initialX, 0 + initialY,2));
        tiles.add(new Tile(mapType, 2176 + initialX, 0 + initialY,2));
        tiles.add(new Tile("null", 2304 + initialX, 0 + initialY,2));
        tiles.add(new Tile("null", 2432 + initialX, 0 + initialY,2));
        tiles.add(new Tile(mapType, 2560 + initialX, 64 + initialY,2));
        tiles.add(new Tile(mapType, 2688 + initialX, 64 + initialY,2));
        tiles.add(new Tile(mapType, 2816 + initialX, 64 + initialY,2));
        tiles.add(new Tile(mapType, 2944 + initialX, 64 + initialY,2));
        tiles.add(new Tile(mapType, 3072 + initialX, 64 + initialY,2));
        tiles.add(new Tile("null", 3200 + initialX, 64 + initialY,2));
        tiles.add(new Tile("null", 3328 + initialX, 64 + initialY,2));
        tiles.add(new Tile(mapType, 3456 + initialX, 128 + initialY,2));
        tiles.add(new Tile(mapType, 3584 + initialX, 128 + initialY,2));
        tiles.add(new Tile(mapType, 3712 + initialX, 128 + initialY,2));
        tiles.add(new Tile(mapType, 3840 + initialX, 128 + initialY,2));
        tiles.add(new Tile(mapType, 3968 + initialX, 128 + initialY,2));
        tiles.add(new Tile(mapType, 4096 + initialX, 128 + initialY,2));
        tiles.add(new Tile("null", 4224 + initialX, 128 + initialY,2));
        tiles.add(new Tile(mapType, 4352 + initialX, 0 + initialY,2));
        tiles.add(new Tile(mapType, 4480 + initialX, 0 + initialY,2));
        tiles.add(new Tile(mapType, 4608 + initialX, 0 + initialY,2));
        tiles.add(new Tile(mapType, 4736 + initialX, 0 + initialY,2));
        tiles.add(new Tile(mapType, 4864 + initialX, 0 + initialY,2));
        tiles.add(new Tile("null", 4992 + initialX, 0 + initialY,2));
        tiles.add(new Tile("null", 5120 + initialX, 0 + initialY,2));
        tiles.add(new Tile(mapType, 5248 + initialX, 0 + initialY,2));
        tiles.add(new Tile(mapType, 5376 + initialX, 0 + initialY,2));
    }

    public Texture getGrass() {
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

    public void dispose() {
        grass.dispose();
        background.dispose();
    }
}
