package com.yungying.game.Player;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.yungying.game.textureLoader.PLAYERS;
import com.yungying.game.textureLoader.playerTextureList;

public class OtherPlayer implements Player {
    private final Vector2 position;
    private String currentFrame;
    private float speed;
    private int score;
    float stateTime;
    private Enum<PLAYERS> playerType;
    String username;


    public OtherPlayer() {
        position = new Vector2(0, 128);
        speed = 300;
        playerType = PLAYERS.TEE;
    }

    @Override
    public void run(float delta, float stateTime) {
        position.x += speed * delta;
    }

    @Override
    public Vector2 getPosition() {
        return position;
    }

    @Override
    public void setPosition(float x, float y) {
        position.x = x;
        position.y = y;
    }

    @Override
    public Texture getCurrentFrame() {
        return playerTextureList.getTextureByString(playerType, currentFrame);
    }

    @Override
    public void setStateTime(float stateTime) {
        this.stateTime = stateTime;
    }

    public void setCurrentFrameString(String currentFrame) {
        this.currentFrame = currentFrame;
    }

    @Override
    public void jump() {}

    @Override
    public void slide() {}

    @Override
    public void gravity(boolean isColliding, float TopBorderOfTile, String blockType){}

    @Override
    public void setSpeed(float speed) {
        this.speed = speed;
    }

    @Override
    public float getSpeed() {
        return speed;
    }

    @Override
    public void setScore(int score) {
        this.score = score;
    }

    @Override
    public int getScore() {
        return score;
    }

    @Override
    public boolean isDead() {return false;}

    public Enum<PLAYERS> getPlayerType() {
        return playerType;
    }

    public void setPlayerType(Enum<PLAYERS> playerType) {
        this.playerType = playerType;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public void setUsername(String username) {
        this.username = username;
    }


}
