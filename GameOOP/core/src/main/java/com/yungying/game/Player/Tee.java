package com.yungying.game.Player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.yungying.game.states.gameStates;
import com.yungying.game.textureLoader.PLAYERS;
import com.yungying.game.textureLoader.playerTextureList;

public class Tee extends Player {

    Enum<PLAYERS> playerType;

    public Tee() {
        super(PLAYERS.TEE);
        this.playerType = PLAYERS.TEE;
    }

}
