package com.yungying.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.yungying.game.Main;

public class LobbyScreen implements Screen {

    Main game;
    private Stage stage;
    private final Viewport viewport;
    private final OrthographicCamera camera;
    private final Skin skin;

    private final Texture backgroundTexture;


    private ImageButton startButton;
    private Texture startTexture;
    private Texture hoverStartTexture;

    private ImageButton exitButton;
    private Texture exitTexture;
    private Texture hoverExitTexture;

    // Define initial size and position
    float initialX = 400;
    float initialY = 250;
    float initialWidth = 200;
    float initialHeight = 50;

    // Variables for animation
    float currentWidth = initialWidth;
    float currentHeight = initialHeight;
    float targetWidth = initialWidth;
    float targetHeight = initialHeight;
    float animationSpeed = 5f;  // Controls how fast the button grows/shrinks

    public static Music music;


    public LobbyScreen(Main game) {
        this.game = game;
        camera = new OrthographicCamera();
        viewport = new FitViewport(800, 400, camera); // Adjust the viewport size as needed
        skin = new Skin(Gdx.files.internal("uiskin.json")); // Assuming you have a "uiskin.json" for the button style
        backgroundTexture = new Texture(Gdx.files.internal("Backgrounds/Lobby Screen.png"));
        music = Gdx.audio.newMusic(Gdx.files.internal("Songs/Whimsy Welcome.mp3"));


    }



    @Override
    public void show() {
        startTexture = new Texture(Gdx.files.internal("buttons/Start/Start.png"));
        hoverStartTexture = new Texture(Gdx.files.internal("buttons/Start/Hover.png"));

        exitTexture = new Texture(Gdx.files.internal("buttons/Exit/Exit.png"));
        hoverExitTexture = new Texture(Gdx.files.internal("buttons/Exit/Hover.png"));

        TextureRegionDrawable startDrawable = new TextureRegionDrawable(startTexture);
        TextureRegionDrawable exitDrawable = new TextureRegionDrawable(exitTexture);

        // Initialize the stage and set the input processor
        stage = new Stage(viewport, game.batch);
        Gdx.input.setInputProcessor(stage);


        // Create the "Start" button
        startButton = new ImageButton(startDrawable);
        startButton.setSize(initialWidth, initialHeight);
        startButton.setPosition(initialX - initialWidth / 2, initialY - initialHeight / 2);  // Centering the button

// Adjust the button's size on hover
        startButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Transition to the next screen (MainMenuScreen)
                game.setScreen(new MainMenuScreen(game));
                dispose();  // Dispose of the current screen resources
            }



            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                // Set target size to larger when the mouse enters
                targetWidth = 300;
                targetHeight = 150;

                // Change the button texture on hover
                startButton.getStyle().imageUp = new TextureRegionDrawable(hoverStartTexture);
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                // Set target size back to the original when the mouse exits
                targetWidth = initialWidth;
                targetHeight = initialHeight;

                // Change the button texture back to the original
                startButton.getStyle().imageUp = new TextureRegionDrawable(startTexture);
            }
        });
        stage.addActor(startButton);  // Add the Start button to the stage

        // Create the "Start" button
        exitButton = new ImageButton(exitDrawable);
        exitButton.setSize(200, 50);
        exitButton.setPosition(initialX - initialWidth / 2, 150);  // Centering the button

// Adjust the button's size on hover
        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Transition to the next screen (MainMenuScreen)
                game.setScreen(new MainMenuScreen(game));
                dispose();  // Dispose of the current screen resources
            }



            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                // Set target size to larger when the mouse enters
                exitButton.setSize(250, 90);
                exitButton.setPosition(275, 150);

                // Change the button texture on hover
                exitButton.getStyle().imageUp = new TextureRegionDrawable(hoverExitTexture);
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                // Set target size back to the original when the mouse exits
                exitButton.setSize(200, 50);
                exitButton.setPosition(initialX - initialWidth / 2, 150);  // Centering the button

                // Change the button texture back to the original
                exitButton.getStyle().imageUp = new TextureRegionDrawable(exitTexture);
            }
        });
        stage.addActor(exitButton);  // Add the Exit button to the stage

        music.setVolume(0f);
        music.setLooping(true);
        music.play();
    }

    final float fadeDuration = 10f;  // Duration for fade-in in seconds
    float fadeTime = 0f;


    @Override
    public void render(float delta) {

        if(fadeTime < fadeDuration){
            fadeTime += delta;
            float volume = Math.min(1f, fadeTime / fadeDuration);
            music.setVolume(volume);
        }

        renderAnimationStartButton(delta);

        // Clear the screen with a color
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(Gdx.gl.GL_COLOR_BUFFER_BIT);

        // Update the camera and set the projection matrix
        camera.update();
        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();
        game.batch.draw(backgroundTexture,0,0,camera.viewportWidth,camera.viewportHeight);
        game.batch.end();
        // Update and draw the stage (UI elements)
        stage.act(delta);  // Process input and actions
        stage.draw();      // Render the stage and its actors (buttons, etc.)
    }

    private void renderAnimationStartButton(float delta) {
        // Smoothly interpolate the width and height
        currentWidth += (targetWidth - currentWidth) * delta * animationSpeed;
        currentHeight += (targetHeight - currentHeight) * delta * animationSpeed;

        // Update the size and position to keep it centered
        startButton.setSize(currentWidth, currentHeight);
        startButton.setPosition(initialX - currentWidth / 2, initialY - currentHeight / 2);
    }

    @Override
    public void resize(int width, int height) {

        viewport.update(width, height);
        stage.getViewport().update(width, height);


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
