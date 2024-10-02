package com.yungying.game.gameInputHandler;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.yungying.game.Player.Player;
import com.yungying.game.screens.MainGameScreen;

public class gameInputHandler {
    public void handleInput(Player player){
        if(Gdx.input.isKeyJustPressed(Input.Keys.UP)){
            player.jump();
        }

        if(Gdx.input.isKeyPressed(Input.Keys.DOWN)){
            player.slide();
        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)){
            MainGameScreen.isMenuShow = !MainGameScreen.isMenuShow;
        }


    }
}
