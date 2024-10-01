package com.yungying.game.Player;

import com.yungying.game.textureLoader.PlayerType;


public class OtherPlayer extends Player {

    private String currentFrame;

    public OtherPlayer(String playerType) {

        super(PlayerType.CUTEGIRL);

        this.currentFrame = "characters/CuteGirl/Run (1).png";
    }

    public void setCurrentFrameString(String currentFrame) {
        this.currentFrame = currentFrame;
    }


}
