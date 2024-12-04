package com.yungying.game.gameInputHandler;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.utils.Array;
import com.yungying.game.Player.Player;
import com.yungying.game.screens.MainGameScreen;

public class gameInputHandler {

    boolean[] keys = new boolean[5];

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
        // Mobile touch input handling
        for (int i=0; i < 5; i++){
            if (Gdx.input.isTouched(i)) {
                float touchX = Gdx.input.getX(i);
                float screenWidth = Gdx.graphics.getWidth();
                // Check if touch is in the left half of the screen for jump
                if (touchX < screenWidth / 2 && !keys[i]) {
                    keys[i] = true;
                    player.jump(); // Jump action
                    break;
                }
                // Check if touch is in the right half of the screen for slide
                if (touchX >= screenWidth / 2) {
                    player.slide(); // Slide action
                }else{
                    player.stopSlide(); // Stop sliding when not touching
                }

            }else{
                keys[i] = false;
            }

        }



    }
}
