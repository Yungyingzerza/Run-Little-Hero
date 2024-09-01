package com.yungying.game.Player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Vector2;

import com.yungying.game.states.gameStates;

public class Player {
    private final Vector2 position;
    private float timeBeforeJump;
    private int jumpCounter;
    private final Animation<Texture> runAnimation;
    private final Animation<Texture> jumpAnimation;
    private final Animation<Texture> slideAnimation;
    private Texture currentFrame;
    private float speed;
    private boolean isJumping;
    private boolean isHighestJump;
    private boolean isSliding;
    private boolean isDead;

    public Player() {
        position = new Vector2(0, 128);
        timeBeforeJump = 0;
        jumpCounter = 0;
        runAnimation = new Animation<Texture>(0.1f, new Texture("characters/Tee/TeeRunLeft.png"), new Texture("characters/Tee/TeeRunRight.png"));
        jumpAnimation = new Animation<Texture>(0.1f, new Texture("characters/Tee/TeeJump.png"));
        slideAnimation = new Animation<Texture>(0.1f, new Texture("characters/Tee/TeeSlide.png"));
        currentFrame = runAnimation.getKeyFrame(0, true);
        speed = 300;
        isJumping = false;
        isHighestJump = false;
        isSliding = false;
        isDead = false;
    }

    public void run(float delta, float stateTime) {
        position.x += speed * delta;
        if(!isJumping || !isSliding) currentFrame = runAnimation.getKeyFrame(stateTime, true);
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

    public void jump() {
        if(isJumping && jumpCounter > 2) return;

        timeBeforeJump = gameStates.stateTime;
        currentFrame = jumpAnimation.getKeyFrame(gameStates.stateTime, true);
        isJumping = true;
        jumpCounter++;
        isHighestJump = false;
    }

    public void slide() {
        if(isJumping) return;

        currentFrame = slideAnimation.getKeyFrame(gameStates.stateTime, true);
        isSliding = true;
    }

    public void gravity(boolean isColliding, float TopBorderOfTile, String blockType) {

        if(isSliding) {
            currentFrame = slideAnimation.getKeyFrame(gameStates.stateTime, true);
            isSliding = false;
        }

        if(isJumping && !isHighestJump && jumpCounter <= 2) {
            currentFrame = jumpAnimation.getKeyFrame(gameStates.stateTime, true);
            position.y += gameStates.GRAVITY * Gdx.graphics.getDeltaTime();

            if(gameStates.stateTime - timeBeforeJump > 0.5f) {
                isHighestJump = true;
            }
            return;
        }

        //isJumping && isHighestJump
        if(isJumping) {
            position.y -= gameStates.GRAVITY * Gdx.graphics.getDeltaTime();
            currentFrame = jumpAnimation.getKeyFrame(gameStates.stateTime, true);

            //if the player is on the ground
            if(position.y/TopBorderOfTile <= 1f && position.y/TopBorderOfTile >= 0.95f) {
                isJumping = false;
                jumpCounter = 0;
                isHighestJump = false;
            }
            return;
        }

        if(isColliding){
            position.y = TopBorderOfTile;
        }else{
            position.y -= gameStates.GRAVITY * Gdx.graphics.getDeltaTime();
        }
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public float getSpeed() {
        return speed;
    }

    public boolean isDead() {
        if(position.y < -128) {
            isDead = true;
        }
        return isDead;
    }

    public void dispose() {
        runAnimation.getKeyFrames()[0].dispose();
        runAnimation.getKeyFrames()[1].dispose();
        jumpAnimation.getKeyFrames()[0].dispose();
        slideAnimation.getKeyFrames()[0].dispose();
    }


}
