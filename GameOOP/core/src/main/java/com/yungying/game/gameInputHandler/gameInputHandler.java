package com.yungying.game.gameInputHandler;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.yungying.game.Player.Player;
import com.yungying.game.screens.MainGameScreen;

public class gameInputHandler {

    public void handleInput(Player player){


        //Keyboard input
        if(Gdx.input.isKeyJustPressed(Input.Keys.UP) || Gdx.input.isKeyJustPressed(Input.Keys.SPACE) || Gdx.input.isKeyJustPressed(Input.Keys.W)){
            player.jump();
        }

        if(Gdx.input.isKeyPressed(Input.Keys.DOWN) || Gdx.input.isKeyPressed(Input.Keys.S)){
            player.slide();
            return;
        }else{
            player.stopSlide();
        }


        if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)){
            MainGameScreen.isMenuShow = !MainGameScreen.isMenuShow;
        }

        //Touch input
        if(Gdx.input.justTouched()){
            if(Gdx.input.getX() < Gdx.graphics.getWidth()/2){
                player.jump();
                player.stopSlide();
            }
        }

        if(Gdx.input.isTouched()) {
            if (Gdx.input.getX() > Gdx.graphics.getWidth() / 2) {
                player.slide();
            }else{
                player.stopSlide();
            }
        }


    }
}
