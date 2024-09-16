package com.yungying.game.Player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

import com.yungying.game.states.gameStates;
import com.yungying.game.textureLoader.PLAYERS;
import com.yungying.game.textureLoader.*;

public class Sakura implements Player {
    private final Vector2 position;
    private float timeBeforeJump;
    private int jumpCounter;
    private Texture currentFrame;
    private float speed;
    private boolean isJumping;
    private boolean isHighestJump;
    private boolean isSliding;
    private boolean isDead;
    private int score;
    float stateTime;
    Enum<PLAYERS> playerType;
    String username;

    public Sakura() {
        playerType = PLAYERS.SAKURA;
        position = new Vector2(0, 128);
        timeBeforeJump = 0;
        jumpCounter = 0;
        currentFrame = playerTextureList.getRunTexture(playerType, stateTime);
        speed = 300;
        isJumping = false;
        isHighestJump = false;
        isSliding = false;
        isDead = false;
    }


    public void run(float delta, float stateTime) {
        position.x += speed * delta;
        currentFrame = playerTextureList.getRunTexture(playerType, stateTime);
    }

    public Vector2 getPosition() {
        return position;
    }

    public void setPosition(float x, float y) {
        position.x = x;
        position.y = y;
    }

    public Texture getCurrentFrame() {
        return currentFrame;
    }

    public void setStateTime(float stateTime) {
        this.stateTime = stateTime;
    }

    public void jump() {
        if(isJumping && jumpCounter > 2) return;

        timeBeforeJump = gameStates.stateTime;
        currentFrame = playerTextureList.getJumpTexture(playerType, gameStates.stateTime);
        isJumping = true;
        jumpCounter++;
        isHighestJump = false;
    }

    public void slide() {
        if(isJumping) return;


        currentFrame = playerTextureList.getSlideTexture(playerType, gameStates.stateTime);
        isSliding = true;
    }

    public void gravity(boolean isColliding, float TopBorderOfTile, String blockType) {

        if(isSliding) {
            currentFrame = playerTextureList.getSlideTexture(playerType, gameStates.stateTime);
            isSliding = false;
        }

        if(isJumping && !isHighestJump && jumpCounter <= 2) {
            currentFrame = playerTextureList.getJumpTexture(playerType, gameStates.stateTime);
            position.y += (gameStates.JUMP_SPEED) * Gdx.graphics.getDeltaTime();

            if(gameStates.stateTime - timeBeforeJump > 0.5f) {
                isHighestJump = true;
            }
            return;
        }

        //isJumping && isHighestJump
        if(isJumping) {
            position.y -= (gameStates.GRAVITY + position.y) * Gdx.graphics.getDeltaTime();
            currentFrame = playerTextureList.getJumpTexture(playerType, gameStates.stateTime);

            //if the player is on the ground
            if(position.y/TopBorderOfTile <= 1.0f && (position.y + 64)/TopBorderOfTile >= 0.95f && !blockType.equals("null")) {
                isJumping = false;
                jumpCounter = 0;
                isHighestJump = false;
            }
            return;
        }

        if((isColliding || ( (position.y + 64) / TopBorderOfTile) >= 0.95f && (position.y + 64) / TopBorderOfTile <= 1f ) && !blockType.equals("null")) {
            position.y = TopBorderOfTile;
        }else{
            position.y -= (gameStates.GRAVITY + position.y) * Gdx.graphics.getDeltaTime();
        }
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public float getSpeed() {
        return speed;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getScore() {
        return score;
    }

    public boolean isDead() {
        if(position.y < -128) {
            isDead = true;
        }
        return isDead;
    }

    @Override
    public void setPlayerType(Enum<PLAYERS> playerType) {
        this.playerType = playerType;
    }

    @Override
    public Enum<PLAYERS> getPlayerType() {
        return playerType;
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
