package com.yungying.game.map;

import com.badlogic.gdx.graphics.Texture;

import java.util.Vector;

public  interface Map {

    Texture getJellyTextureAtIndex(int index);

    Texture getTileTextureAtIndex(int index);

    Vector<Tile> getTiles();

    Vector<Jelly> getJellies();

    float getMapSpeed();

    Tile getCurrentTile(float playerX);

    Texture getBackground();

    boolean isColliding(float playerX, float playerY);

    Tile getLastTile();

    Tile getFirstTile();

    String getNextMap();

    String getNextMapPath();

    void dispose();
}
