package com.yungying.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.ScreenUtils;
import com.yungying.game.Main;

public class MainMenuScreen implements Screen {

    private final int BUTTON_WIDTH = Gdx.graphics.getWidth() / 4;
    private final int BUTTON_HEIGHT = Gdx.graphics.getHeight() / 4;

    Main game;

    Texture playButton;
    Texture playHover;

    public MainMenuScreen(Main game) {
        this.game = game;
        playButton = new Texture("buttons/play.png");
        playHover = new Texture("buttons/play-hover.png");
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float v) {
        Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
        Gdx.gl.glClear(Gdx.gl.GL_COLOR_BUFFER_BIT);

        game.batch.begin();

        if(Gdx.input.getX() < Gdx.graphics.getWidth() / 2 + BUTTON_WIDTH / 2 && Gdx.input.getX() > Gdx.graphics.getWidth() / 2 - BUTTON_WIDTH / 2 && Gdx.input.getY() < Gdx.graphics.getHeight() / 2 + BUTTON_HEIGHT / 2 && Gdx.input.getY() > Gdx.graphics.getHeight() / 2 - BUTTON_HEIGHT / 2) {
            game.batch.draw(playHover, Gdx.graphics.getWidth() / 2 - BUTTON_WIDTH / 2, Gdx.graphics.getHeight() / 2 - BUTTON_HEIGHT / 2, BUTTON_WIDTH, BUTTON_HEIGHT);
            if(Gdx.input.isTouched()) {
                game.setScreen(new MainGameScreen(game));
                dispose();
            }
        } else {
            game.batch.draw(playButton, Gdx.graphics.getWidth() / 2 - BUTTON_WIDTH / 2, Gdx.graphics.getHeight() / 2 - BUTTON_HEIGHT / 2, BUTTON_WIDTH, BUTTON_HEIGHT);
        }

        game.batch.end();
    }

    @Override
    public void resize(int i, int i1) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        playButton.dispose();
        playHover.dispose();
    }
}
