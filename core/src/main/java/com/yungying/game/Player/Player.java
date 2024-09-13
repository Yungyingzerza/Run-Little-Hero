package com.yungying.game.Player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Vector2;

import com.yungying.game.states.gameStates;

import java.math.BigInteger;

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
    private int score;
    private String username;
    private Texture testTexture;

    public Player(boolean isOtherPlayer) {
        this.runAnimation = new Animation<Texture>(0.1f, new Texture("characters/Tee/TeeRunLeft.png"), new Texture("characters/Tee/TeeAgainLeft.png"), new Texture("characters/Tee/TeeRunRight.png"), new Texture("characters/Tee/TeeAgainRight.png"), new Texture("characters/Tee/TeeAgainLeft.png"));
        this.jumpAnimation = new Animation<Texture>(0.1f, new Texture("characters/Tee/TeeJump.png"));
        this.slideAnimation = new Animation<Texture>(0.1f, new Texture("characters/Tee/TeeSlide.png"));
        this.position = new Vector2(0, 128);
    }

    public Player() {
        this.username = "yungying";
        position = new Vector2(0, 128);
        timeBeforeJump = 0;
        jumpCounter = 0;
        testTexture = new Texture("characters/Tee/TeeRunLeft.png");
        runAnimation = new Animation<Texture>(0.1f, new Texture("characters/Tee/TeeRunLeft.png"), new Texture("characters/Tee/TeeAgainLeft.png"), new Texture("characters/Tee/TeeRunRight.png"), new Texture("characters/Tee/TeeAgainRight.png"), new Texture("characters/Tee/TeeAgainLeft.png"));
        jumpAnimation = new Animation<Texture>(0.1f, new Texture("characters/Tee/TeeJump.png"));
        slideAnimation = new Animation<Texture>(0.1f, new Texture("characters/Tee/TeeSlide.png"));
        currentFrame = runAnimation.getKeyFrame(0, true);
        speed = 300;
        isJumping = false;
        isHighestJump = false;
        isSliding = false;
        isDead = false;
    }

    public Texture getTestTexture() {
        return testTexture;
    }

    public Player(String currentFrame, double x, double y, int score, String username) {
        this.username = username;
        position = new Vector2((float)x, (float)y);
        timeBeforeJump = 0;
        jumpCounter = 0;
        runAnimation = new Animation<Texture>(0.1f, new Texture("characters/Tee/TeeRunLeft.png"), new Texture("characters/Tee/TeeAgainLeft.png"), new Texture("characters/Tee/TeeRunRight.png"), new Texture("characters/Tee/TeeAgainRight.png"), new Texture("characters/Tee/TeeAgainLeft.png"));
        jumpAnimation = new Animation<Texture>(0.1f, new Texture("characters/Tee/TeeJump.png"));
        slideAnimation = new Animation<Texture>(0.1f, new Texture("characters/Tee/TeeSlide.png"));
        this.currentFrame = new Texture(currentFrame);
        testTexture = new Texture("characters/Tee/TeeRunLeft.png");
        speed = 300;
        isJumping = false;
        isHighestJump = false;
        isSliding = false;
        isDead = false;
        this.score = score;
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
            position.y += (gameStates.JUMP_SPEED) * Gdx.graphics.getDeltaTime();

            if(gameStates.stateTime - timeBeforeJump > 0.5f) {
                isHighestJump = true;
            }
            return;
        }

        //isJumping && isHighestJump
        if(isJumping) {
            position.y -= (gameStates.GRAVITY + position.y) * Gdx.graphics.getDeltaTime();
            currentFrame = jumpAnimation.getKeyFrame(gameStates.stateTime, true);

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

    public String getUsername() {
        return username;
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

    public void dispose() {
        runAnimation.getKeyFrames()[0].dispose();
        runAnimation.getKeyFrames()[1].dispose();
        runAnimation.getKeyFrames()[2].dispose();
        runAnimation.getKeyFrames()[3].dispose();
        runAnimation.getKeyFrames()[4].dispose();

        jumpAnimation.getKeyFrames()[0].dispose();
        slideAnimation.getKeyFrames()[0].dispose();
    }


}
