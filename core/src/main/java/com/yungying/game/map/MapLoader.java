package com.yungying.game.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;

import java.util.Vector;

public class MapLoader implements Map {
    private final Vector<Tile> tiles;
    private final Texture grassTexture;
    private final Texture grassWinterTexture;
    private final Texture backgroundTexture;
    private final float mapSpeed;
    private final String nextMap;
    private final String nextMapPath;
    private Tile currentTile;
    private final float initialX;

    public MapLoader(String jsonFilePath, float initialX) {
        // Initialize textures
        grassTexture = new Texture("Grass.png");
        grassWinterTexture = new Texture("GrassWinter.png");
        tiles = new Vector<Tile>();
        this.initialX = initialX;

        // Load and parse JSON
        FileHandle file = Gdx.files.internal(jsonFilePath);
        Json json = new Json();
        MapData mapData = json.fromJson(MapData.class, file);


        // Map configuration
        mapSpeed = mapData.mapSpeed;
        nextMap = mapData.nextMap;
        nextMapPath = mapData.nextMapPath;
        backgroundTexture = new Texture(mapData.background);

        // Load tiles from JSON
        for (int i = 0; i < mapData.tiles.size; i++) {
            MapData.TileData tileData = mapData.tiles.get(i);
            float x = 128 * i + initialX; // Position calculation
            String type = tileData.type;
            float y = tileData.y;
            float zoom = tileData.zoom;

            // Create and add the tile
            tiles.add(new Tile(type, x, y, zoom));
        }

        if (!tiles.isEmpty()) {
            currentTile = tiles.getFirst();
        }
    }

    @Override
    public Texture getTileTextureAtIndex(int index) {
        if (index >= 0 && index < tiles.size()) {
            String type = tiles.get(index).getType();
            if ("Grass".equals(type)) {
                return grassTexture;
            } else if ("GrassWinter".equals(type)) {
                return grassWinterTexture;
            }
        }
        return null;
    }

    public Vector<Tile> getTiles() {
        return tiles;
    }

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
        return backgroundTexture;
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
    public String getNextMapPath() {
        return nextMapPath;
    }

    public void dispose() {
        grassTexture.dispose();
        backgroundTexture.dispose();
    }
}
