package com.yungying.game.Player;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.yungying.game.textureLoader.PlayerType;
import com.yungying.game.textureLoader.playerTextureList;


public class OtherPlayer extends Player {

    private String currentFrame;

    public OtherPlayer(String playerType) {

        super(PlayerType.CUTEGIRL);
        this.currentFrame = "characters/CuteGirl/Run (1).png";
    }

    @Override
    public Texture getCurrentFrame() {
        return playerTextureList.getTextureByString(playerType, currentFrame);
    }

    public void setCurrentFrameString(String currentFrame) {
        this.currentFrame = currentFrame;
    }


}
