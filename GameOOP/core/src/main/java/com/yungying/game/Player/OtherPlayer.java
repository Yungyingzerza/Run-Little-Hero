package com.yungying.game.Player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.yungying.game.textureLoader.PlayerType;
import com.yungying.game.states.gameStates;


public class OtherPlayer extends Player {

    private Vector2 targetPosition;

    public OtherPlayer(String playerType, float initialX, float initialY) {

        super(PlayerType.valueOf(playerType));

        setPosition(initialX, initialY);
        targetPosition = new Vector2(0, 0);
    }

    public void run() {

        float speedFactor = 5.0f;
        float delta = Gdx.graphics.getDeltaTime();

        Vector2 currentPosition = this.getPosition();

        //check if target and current position is too far
        if(Math.abs(targetPosition.x - currentPosition.x) > 300){
            setPosition(targetPosition.x, targetPosition.y);
            return;
        }

        currentPosition.x += (targetPosition.x - currentPosition.x) * speedFactor * delta;
        currentPosition.y += (targetPosition.y - currentPosition.y) * speedFactor * delta;

        //check y with gravity
        if (targetPosition.y - currentPosition.y < 0) {
            currentPosition.y -= gameStates.GRAVITY / 10f * delta;
        } else if(targetPosition.y - currentPosition.y > 0) {
            currentPosition.y += gameStates.GRAVITY / 10f * delta;
        }else{
            currentPosition.y = targetPosition.y;
        }

        setPosition(currentPosition.x, currentPosition.y);

    }

    public void setIsDead(boolean isDead) {
        this.isDead = isDead;
    }


    public void setTargetPosition(Vector2 targetPosition) {
        this.targetPosition = targetPosition;
    }


    public void setIsJumping(boolean isJumping) {
        this.isJumping = isJumping;
    }

    public void setIsSliding(boolean isSliding) {
        this.isSliding = isSliding;
    }

}
