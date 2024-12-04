package com.yungying.game.textureLoader;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.yungying.game.Main;

public class mapTextureList {
    // Initialize textures
    public static final Texture grassTexture = new Texture("Tiles/Grass.png");
    public static final Texture grassMidnightTexture = new Texture("Tiles/GrassMidnight.png");
    public static final Texture grassNightmareTexture = new Texture("Tiles/GrassNightmare.png");
    public static final Texture grassWinterTexture = new Texture("Tiles/GrassWinter.png");
    public static final Texture grassKungfu = new Texture("Tiles/GrassKungFu.png");
    public static final Texture Coin = new Texture("Point/Coin.png");
    public static final Music CoinSound = Gdx.audio.newMusic(Gdx.files.internal("Point/Coin.mp3"));
    public static final Music defaultSound = Gdx.audio.newMusic(Gdx.files.internal("Point/poit-94911.mp3"));
    public static final Music healthSound = Gdx.audio.newMusic(Gdx.files.internal("Point/magic_spell_10-39689.mp3"));
    public static final Texture Cherry = new Texture("Point/Cherry.png");
    public static final Texture potionTexture = new Texture("Potion/1.png");
    public static final Texture LongMetal = new Texture("Spikes/LongMetal/long_metal_spike.png");
    public static final Texture Bird = new Texture("Spikes/LongMetal/Bird/2.png");
}
