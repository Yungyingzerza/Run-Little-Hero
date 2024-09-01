package com.yungying.game.map;

import com.badlogic.gdx.graphics.Texture;
import com.yungying.game.Main;

import java.util.Vector;

public abstract interface Map {

    Texture getTile();

    Vector<Tile> getTiles();

    float getMapSpeed();

    Tile getCurrentTile(float playerX);

    Texture getBackground();

    boolean isColliding(float playerX, float playerY);

    Tile getLastTile();

    Tile getFirstTile();

    String getNextMap();

    void dispose();
}
