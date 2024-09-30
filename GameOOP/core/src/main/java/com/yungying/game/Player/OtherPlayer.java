package com.yungying.game.Player;

import com.yungying.game.textureLoader.PlayerType;


public class OtherPlayer extends Player {

    private String currentFrame;

    public OtherPlayer() {
        super(PlayerType.TEE);
        this.currentFrame = "characters/Tee/TeeRunLeft.png";
    }

    public void setCurrentFrameString(String currentFrame) {
        this.currentFrame = currentFrame;
    }


}
