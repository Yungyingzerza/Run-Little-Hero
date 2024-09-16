package com.yungying.game.Player;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.yungying.game.textureLoader.PLAYERS;

public interface Player {

    void run(float delta, float stateTime);

    Vector2 getPosition();

    void setPosition(float x, float y);

    Texture getCurrentFrame();

    void setStateTime(float stateTime);

    void jump();

    void slide();

    void gravity(boolean isColliding, float TopBorderOfTile, String blockType);

    void setSpeed(float speed);

    float getSpeed();

    void setScore(int score);

    int getScore();

    boolean isDead();

    void setPlayerType(Enum<PLAYERS> playerType);

    Enum<PLAYERS> getPlayerType();

}
