package com.yungying.game.gameInputHandler;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.yungying.game.Player.Player;
import com.yungying.game.screens.MainGameScreen;

public class gameInputHandler {

    private boolean jumpButtonHeld = false;

    public void handleInput(Player player){
        if(Gdx.input.isKeyJustPressed(Input.Keys.UP)){
            player.jump();
        }

        if(Gdx.input.isKeyPressed(Input.Keys.DOWN)){
            player.slide();
        }else{
            player.stopSlide();
        }


        if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)){
            MainGameScreen.isMenuShow = !MainGameScreen.isMenuShow;
        }

        // Mobile touch input handling
        if (Gdx.input.isTouched()) {
            float touchX = Gdx.input.getX();
            float screenWidth = Gdx.graphics.getWidth();

            // Check if touch is in the left half of the screen for jump
            if (touchX < screenWidth / 2) {
                if (!jumpButtonHeld) {
                    player.jump(); // Jump action
                }
                jumpButtonHeld = true; // Set the flag if the jump button is held
            }

            // Check if touch is in the right half of the screen for slide
            if (touchX >= screenWidth / 2) {
                player.slide(); // Slide action
            }
        } else {
            player.stopSlide(); // Stop sliding when not touching
            jumpButtonHeld = false; // Reset jump button held flag
        }


    }
}
