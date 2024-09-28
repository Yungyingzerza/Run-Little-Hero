package com.yungying.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.yungying.game.Main;

public class MainMenuScreen1 implements Screen {

    Main game;
    private Stage stage;
    private Viewport viewport;
    private OrthographicCamera camera;
    private Skin skin;
    private TextButton startButton;
    private TextButton exitButton;
    private Texture backgroundTexture;

    public MainMenuScreen1(Main game) {
        this.game = game;
        camera = new OrthographicCamera();
        viewport = new FitViewport(800, 400, camera); // Adjust the viewport size as needed
        skin = new Skin(Gdx.files.internal("uiskin.json")); // Assuming you have a "uiskin.json" for the button style
    }

    @Override
    public void show() {
        // Load the background texture
        backgroundTexture = new Texture(Gdx.files.internal("background.png"));

        // Initialize the stage and set the input processor
        stage = new Stage(viewport, game.batch);
        Gdx.input.setInputProcessor(stage);

        // Create the "Start" button
        startButton = new TextButton("Start", skin);
        startButton.setPosition(300, 200);  // Adjust the button's position
        startButton.setSize(200, 50);       // Adjust the button's size
        startButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Transition to the next screen (MainMenuScreen)
                game.setScreen(new MainMenuScreen(game));
                dispose();  // Dispose of the current screen resources
            }
        });
        stage.addActor(startButton);  // Add the Start button to the stage

        // Create the "Exit" button
        exitButton = new TextButton("Exit", skin);
        exitButton.setPosition(300, 150);  // Adjust the button's position
        exitButton.setSize(200, 50);       // Adjust the button's size
        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();  // This will close the game
            }
        });
        stage.addActor(exitButton);  // Add the Exit button to the stage
    }

    @Override
    public void render(float delta) {
        // Clear the screen with a color
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(Gdx.gl.GL_COLOR_BUFFER_BIT);

        // Update the camera and set the projection matrix
        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        // Update and draw the stage (UI elements)
        stage.act(delta);  // Process input and actions
        stage.draw();      // Render the stage and its actors (buttons, etc.)
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
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
        stage.dispose();
        skin.dispose();
    }
}
