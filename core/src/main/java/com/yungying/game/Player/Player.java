package com.yungying.game.Player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Vector2;

import com.yungying.game.states.gameStates;

public class Player {
    private final Vector2 position;
    private final Vector2 positionBeforeJump;
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
        positionBeforeJump = new Vector2(0, 128);
        runAnimation = new Animation<Texture>(0.1f, new Texture("characters/Tee/TeeRunLeft.png"), new Texture("characters/Tee/TeeRunRight.png"));
        jumpAnimation = new Animation<Texture>(0.1f, new Texture("characters/Tee/TeeJump.png"));
        slideAnimation = new Animation<Texture>(0.1f, new Texture("characters/Tee/TeeSlide.png"));
        currentFrame = runAnimation.getKeyFrame(0, true);
        speed = 300;
        isJumping = false;
        isHighestJump = false;
        isSliding = false;
    }

    public void run(float delta, float stateTime) {
        position.x += speed * delta;
        if(!isJumping || !isSliding) currentFrame = runAnimation.getKeyFrame(stateTime, true);
    }

    public Vector2 getPosition() {
        return position;
    }

    public Texture getCurrentFrame() {
        return currentFrame;
    }

    public void jump() {
        if(isJumping) return;

        positionBeforeJump.x = position.x;
        positionBeforeJump.y = position.y;
        currentFrame = jumpAnimation.getKeyFrame(gameStates.stateTime, true);
        isJumping = true;
        isHighestJump = false;
    }

    public void slide() {
        if(isJumping) return;

        currentFrame = slideAnimation.getKeyFrame(gameStates.stateTime, true);
        isSliding = true;
    }

    public void gravity(boolean isColliding, float TopBorderOfTile, String blockType) {

        if(TopBorderOfTile == 0 && blockType.equals("null")) {
            isJumping = true;
            isHighestJump = true;
            position.y -= 10;
        }

        if(isSliding) {
            currentFrame = slideAnimation.getKeyFrame(gameStates.stateTime, true);
            isSliding = false;
        }

        if(isJumping && position.y < TopBorderOfTile + 128 && !isHighestJump && position.y < positionBeforeJump.y + 128) {
            position.y += 256 * Gdx.graphics.getDeltaTime();
            currentFrame = jumpAnimation.getKeyFrame(gameStates.stateTime, true);

            if(position.y >= positionBeforeJump.y + 128) {
                isHighestJump = true;
            }
            return;
        } else if (isHighestJump) {
            position.y -= gameStates.GRAVITY * Gdx.graphics.getDeltaTime();
            currentFrame = jumpAnimation.getKeyFrame(gameStates.stateTime, true);

            if(position.y > TopBorderOfTile) {
                position.y -= TopBorderOfTile * Gdx.graphics.getDeltaTime();
            }else if (position.y <= TopBorderOfTile && positionBeforeJump.y + 128 >= TopBorderOfTile && !blockType.equals("null") && (position.x - positionBeforeJump.x <= 256 || position.y + 64 >= TopBorderOfTile)) {
                position.y = TopBorderOfTile;
                isJumping = false;
                isHighestJump = false;
            }else if(position.y < positionBeforeJump.y && position.y < TopBorderOfTile) {
                isHighestJump = true;
                isJumping = true;
            }

            return;
        }

        if(isColliding){
            position.y = TopBorderOfTile;
            isJumping = false;

        }else{
            position.y -= gameStates.GRAVITY * Gdx.graphics.getDeltaTime();
        }
    }

    public void setSpeed(float speed) {
        this.speed = speed;
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
