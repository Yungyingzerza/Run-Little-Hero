package com.yungying.game.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Json;

import java.util.Vector;

public class MapLoader implements Map {
    private final Vector<Tile> tiles;
    private final Vector<Jelly> jellies;
    private final Vector<Spike> spikes;
    private final Texture Coin;
    private final Texture Cherry;
    private final Texture grassTexture;
    private final Texture grassWinterTexture;
    private final Texture backgroundTexture;
    private final float mapSpeed;
    private final String nextMap;
    private final String nextMapPath;
    private Tile currentTile;
    private final float initialX;
    Music music;

    private final Texture LongMetal;

    public MapLoader(String jsonFilePath, float initialX) {
        // Initialize textures
        grassTexture = new Texture("Grass.png");
        Coin = new Texture("Point/Coin.png");
        Cherry = new Texture("Point/Cherry.png");
        grassWinterTexture = new Texture("GrassWinter.png");
        LongMetal = new Texture("Spikes/LongMetal/long_metal_spike.png");
        tiles = new Vector<>();
        jellies = new Vector<>();
        spikes = new Vector<>();
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
        music = Gdx.audio.newMusic(Gdx.files.internal(mapData.song));

        // Load tiles from JSON
        for (int i = 0; i < mapData.tiles.size; i++) {
            MapData.TileData tileData = mapData.tiles.get(i);
            float x = 128 * i + initialX; // Position calculation
            BlockType type = tileData.type;
            float y = tileData.y;
            float zoom = tileData.zoom;

            // Create and add the tile
            tiles.add(new Tile(type, x, y, zoom));
        }

        //load jellies from JSON
        for (int i = 0; i < mapData.jellies.size; i++) {
            MapData.Jelly jellyData = mapData.jellies.get(i);
            ItemType type = jellyData.type;
            float x = 128 * i + initialX; // Position calculation
            float y = jellyData.y;

            // Create and add the jelly
            jellies.add(new Jelly(type, x, y));
        }

        //load spikes from JSON
        for (int i = 0; i < mapData.spikes.size; i++) {
            MapData.Spike spikeData = mapData.spikes.get(i);
            SpikeType type = spikeData.type;
            float x = 128 * i + initialX; // Position calculation
            float y = spikeData.y;

            // Create and add the spike
            spikes.add(new Spike(type, x, y));
        }

        if (!tiles.isEmpty()) {
            currentTile = tiles.getFirst();
        }
    }

    @Override
    public Texture getTileTextureAtIndex(int index) {
        if (index >= 0 && index < tiles.size()) {
            BlockType type = tiles.get(index).getType();
            if (type.equals(BlockType.Grass)) {
                return grassTexture;
            } else if (type.equals(BlockType.GrassWinter)) {
                return grassWinterTexture;
            }
        }
        return null;
    }

    @Override
    public Music getMusic() {
        return music;
    }

    @Override
    public Texture getSpikesTextureAtIndex(int index) {
        if (index >= 0 && index < spikes.size()) {
            SpikeType type = spikes.get(index).getType();
            if (type.equals(SpikeType.LONG_METAL)) {
                return LongMetal;
            }
        }
        return null;
    }

    @Override
    public Texture getJellyTextureAtIndex(int index) {
        if (index >= 0 && index < jellies.size()) {
            ItemType type = jellies.get(index).getType();
            if (type.equals(ItemType.Coin)) {
                return Coin;
            }else if (type.equals(ItemType.Cherry)) {
                return Cherry;
            }
        }
        return null;
    }

    @Override
    public Vector<Tile> getTiles() {
        return tiles;
    }

    @Override
    public Vector<Jelly> getJellies() {
        return jellies;
    }

    @Override
    public Vector<Spike> getSpikes() {
        return spikes;
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
        return backgroundTexture;
    }

    @Override
    public boolean isColliding(float playerX, float playerY) {
        return currentTile.isColliding( playerY);
    }

    @Override
    public int isCollectJelly(float playerX, float playerY) {
        for (Jelly jelly : jellies) {
            int score = jelly.isColliding(playerX, playerY);
            if (score > 0) {
                jellies.remove(jelly);
                return score;
            }
        }
        return 0;
    }

    @Override
    public int isCollidingSpike(float playerX, float playerY) {
        for (Spike spike : spikes) {
            int health = spike.isColliding(playerX, playerY);
            if (health < 0) {
                spikes.remove(spike);
                return health;
            }
        }
        return 0;
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
