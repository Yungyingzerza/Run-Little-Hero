package com.yungying.game.Player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.yungying.game.map.BlockType;
import com.yungying.game.states.gameStates;
import com.yungying.game.textureLoader.PlayerType;
import com.yungying.game.textureLoader.playerTextureList;

public class Player {
    private final Vector2 position;
    private final Vector2 velocity;
    private int jumpCounter;
    private Texture currentFrame;
    private float speed;
     boolean isJumping;
    private boolean isHighestJump;
     boolean isSliding;
     boolean isDead;
    private int score;
    private float stateTime;
    PlayerType playerType;
    String username;
    private int health;

    Music jumpSound;

    public Player(PlayerType playerType) {
        this.playerType = playerType;
        position = new Vector2(0, 128);
        velocity = new Vector2(0, 0);
        jumpCounter = 0;
        speed = 300;
        currentFrame = playerTextureList.getRunTexture(playerType, stateTime);
        isJumping = false;
        isHighestJump = false;
        isSliding = false;
        isDead = false;
        health = 100;
        stateTime = 0f;
        jumpSound = Gdx.audio.newMusic(Gdx.files.internal("characters/Jump.wav"));
    }

    public boolean isJumping() {
        return isJumping;
    }

    public boolean isSliding() {
        return isSliding;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getHealth() {
        return health;
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

    public float getStateTime() {
        return stateTime;
    }

    public void jump() {
        if(isJumping && jumpCounter >= 2) return;

        currentFrame = playerTextureList.getJumpTexture(playerType, gameStates.stateTime);
        isJumping = true;
        jumpCounter++;
        velocity.y = gameStates.JUMP_SPEED;
        isHighestJump = false;
        jumpSound.play();
    }

    public void slide() {
        if(isJumping) return;

        currentFrame = playerTextureList.getSlideTexture(playerType, gameStates.stateTime);
        isSliding = true;
    }

    public void stopSlide() {

        isSliding = false;
    }

    public void gravity(boolean isColliding, float TopBorderOfTile, BlockType blockType) {

        if(isSliding) {
            currentFrame = playerTextureList.getSlideTexture(playerType, gameStates.stateTime);
        }

        if(isJumping && !isHighestJump && jumpCounter <= 2) {
            currentFrame = playerTextureList.getJumpTexture(playerType, gameStates.stateTime);
            position.y += (velocity.y) * Gdx.graphics.getDeltaTime();
            velocity.y -= (gameStates.GRAVITY) * Gdx.graphics.getDeltaTime();


            if(velocity.y < 0) {
                isHighestJump = true;
                velocity.y = 0;
            }

            return;
        }

        //isJumping && isHighestJump
        if(isJumping) {
            position.y += (velocity.y) * Gdx.graphics.getDeltaTime();
            velocity.y -= (gameStates.GRAVITY) * Gdx.graphics.getDeltaTime();
            currentFrame = playerTextureList.getJumpTexture(playerType, gameStates.stateTime);

            //if the player is on the ground
            if(position.y/TopBorderOfTile <= 1.1f && (position.y + 64)/TopBorderOfTile >= 0.98f && !blockType.equals(BlockType.Air)) {
                isJumping = false;
                jumpCounter = 0;
                isHighestJump = false;
                velocity.y = 0;
            }
            return;
        }

        if((isColliding || ( (position.y + 64) / TopBorderOfTile) >= 0.98f && (position.y + 64) / TopBorderOfTile <= 1.1f ) && !blockType.equals(BlockType.Air)) {
            position.y = TopBorderOfTile;
        }else{
            position.y -= (gameStates.GRAVITY/3.25f) * Gdx.graphics.getDeltaTime();
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

        //if health is 0
        if(health <= 0){
            isDead = true;
        }

        //set current frame to dead texture
        if(isDead) {
            currentFrame = playerTextureList.getSlideTexture(playerType, gameStates.stateTime);
        }

        return isDead;
    }

    public void setPlayerType(PlayerType playerType) {
        this.playerType = playerType;
    }

    public PlayerType getPlayerType() {
        return playerType;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

}
