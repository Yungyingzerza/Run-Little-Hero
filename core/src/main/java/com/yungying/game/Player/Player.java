package com.yungying.game.Player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Vector2;

public class Player {
    private final Vector2 position;
    private final Animation<Texture> runAnimation;
    private final Animation<Texture> jumpAnimation;
    private final Animation<Texture> slideAnimation;
    private Texture currentFrame;
    private float speed;
    private boolean isJumping;
    private boolean isSliding;

    public Player() {
        position = new Vector2(0, 0);
        runAnimation = new Animation<Texture>(0.1f, new Texture("characters/Tee/TeeRunLeft.png"), new Texture("characters/Tee/TeeRunRight.png"));
        jumpAnimation = new Animation<Texture>(0.1f, new Texture("characters/Tee/TeeJump.png"));
        slideAnimation = new Animation<Texture>(0.1f, new Texture("characters/Tee/TeeSlide.png"));
        currentFrame = runAnimation.getKeyFrame(0, true);
        speed = 100;
        isJumping = false;
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

    public void jump(float stateTime) {
        if(isJumping) return;

        position.y += 5000 * Gdx.graphics.getDeltaTime();
        currentFrame = jumpAnimation.getKeyFrame(stateTime, true);
        isJumping = true;
    }

    public void slide(float stateTime) {
        if(isJumping) return;
        currentFrame = slideAnimation.getKeyFrame(stateTime, true);
        isSliding = true;
    }

    public void gravity(float stateTime) {
        if (position.y > 0) {
            position.y -= 200 * Gdx.graphics.getDeltaTime();
            currentFrame = jumpAnimation.getKeyFrame(stateTime, true);
            isJumping = true;
            isSliding = false;
        } else {
            //check slide
            if(Gdx.input.isKeyPressed(20)) return;

            position.y = 0;
            currentFrame = runAnimation.getKeyFrame(stateTime, true);
            isJumping = false;
        }
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }


}
