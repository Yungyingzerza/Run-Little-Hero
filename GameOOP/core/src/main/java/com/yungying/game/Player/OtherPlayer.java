package com.yungying.game.Player;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.yungying.game.textureLoader.PLAYERS;
import com.yungying.game.textureLoader.playerTextureList;

public class OtherPlayer extends Player {

    private String currentFrame;

    public OtherPlayer() {
        super(PLAYERS.TEE);
        playerType = PLAYERS.TEE;
        this.currentFrame = "characters/Tee/TeeRunLeft.png";
    }

    public void setCurrentFrameString(String currentFrame) {
        this.currentFrame = currentFrame;
    }


}
