package com.yungying.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.yungying.game.Main;

import java.util.HashMap;

public class MainMenuScreen implements Screen {

    private final int BUTTON_WIDTH = Gdx.graphics.getWidth() / 4;
    private final int BUTTON_HEIGHT = Gdx.graphics.getHeight() / 4;

    Main game;

    Texture playButton;
    Texture playHover;
    Texture sakura;

    private OrthographicCamera camera;
    Viewport viewport;

    public MainMenuScreen(Main game) {
        this.game = game;
        playButton = new Texture("buttons/play.png");
        playHover = new Texture("buttons/play-hover.png");
        sakura = new Texture("characters/Sakura/SakuraRunRight.png");
    }

    @Override
    public void show() {
        // Initialize the camera with your desired viewport size
        camera = new OrthographicCamera();
        viewport = new FitViewport(800, 400, camera);
        viewport.apply(); // Apply the viewport settings

        // Set the camera position to be centered
        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
        camera.update(); // Ensure camera is updated


        // Ensure the viewport is updated to the current screen size
        viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
    }

    @Override
    public void render(float v) {
        Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
        Gdx.gl.glClear(Gdx.gl.GL_COLOR_BUFFER_BIT);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin();

        game.batch.draw(sakura, 0, 0, 128, 128);

        // Get the viewport dimensions
        float viewportWidth = viewport.getWorldWidth();
        float viewportHeight = viewport.getWorldHeight();

        // Calculate the button's position relative to the viewport
        float buttonX = viewportWidth / 2 - BUTTON_WIDTH / 2;
        float buttonY = viewportHeight / 2 - BUTTON_HEIGHT / 2;

        // Convert screen coordinates to viewport coordinates
        Vector3 mouseCoordinates = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        camera.unproject(mouseCoordinates);
        float mouseX = mouseCoordinates.x;
        float mouseY = mouseCoordinates.y;

        // Check if the mouse is over the button
        if (mouseX > buttonX && mouseX < buttonX + BUTTON_WIDTH &&
            mouseY > buttonY && mouseY < buttonY + BUTTON_HEIGHT) {

            // Draw the hover state of the button
            game.batch.draw(playHover, buttonX, buttonY, BUTTON_WIDTH, BUTTON_HEIGHT);

            // Handle button click
            if (Gdx.input.isTouched()) {
                game.setScreen(new MainGameScreen(game));
                dispose();
            }
        } else {
            // Draw the normal state of the button
            game.batch.draw(playButton, buttonX, buttonY, BUTTON_WIDTH, BUTTON_HEIGHT);
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
        sakura.dispose();
    }
}
