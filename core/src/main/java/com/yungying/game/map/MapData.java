package com.yungying.game.map;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.Array;

public class MapData {
    public String background;
    public String nextMap;
    public String nextMapPath;
    public float mapSpeed;
    public Array<TileData> tiles;
    public Array<Jelly> jellies;

    public static class TileData {
        public String type;
        public float y;
        public float zoom;
    }

    public static class Jelly{
        public String type;
        public float y;
    }
}
