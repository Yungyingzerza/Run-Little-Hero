package com.yungying.game.map;

import com.badlogic.gdx.utils.Array;

public class MapData {
    public String background;
    public String nextMap;
    public String nextMapPath;
    public float mapSpeed;
    public Array<TileData> tiles;
    public Array<Jelly> jellies;
    public Array<Spike> spikes;

    public static class TileData {
        public BlockType type;
        public float y;
        public float zoom;
    }

    public static class Jelly{
        public ItemType type;
        public float y;
    }

    public static class Spike{
        public SpikeType type;
        public float y;
    }
}
