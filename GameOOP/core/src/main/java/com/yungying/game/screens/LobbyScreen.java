package com.yungying.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.yungying.game.Main;
import com.yungying.game.hooks.Authentication;
import com.yungying.game.hooks.UseUser;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.concurrent.CountDownLatch;

public class LobbyScreen implements Screen {

    Main game;
    private Stage stage;
    private Viewport viewport;
    private OrthographicCamera camera;
    private Skin skin;
    private ImageButton startButton;
    private TextButton exitButton;
    private Texture backgroundTexture;
    private UseUser useUser;


    public LobbyScreen(Main game) {
        this.game = game;
        camera = new OrthographicCamera();
        viewport = new FitViewport(800, 400, camera); // Adjust the viewport size as needed
        skin = new Skin(Gdx.files.internal("uiskin.json")); // Assuming you have a "uiskin.json" for the button style
        backgroundTexture = new Texture(Gdx.files.internal("Background.png"));
        useUser = new UseUser();

        loginAndPrintUsername();

    }

    private void loginAndPrintUsername() {
        CountDownLatch latch = new CountDownLatch(1); // Initialize latch with count of 1

        System.out.println("Logging in...");
        Enum<Authentication> status = useUser.login("Yung", "1234", latch); // Pass the latch
        System.out.println("Status: " + status);
        // Wait for the login to complete
        new Thread(() -> {
            try {
                latch.await(); // Wait until the latch is counted down
                // Now that login is complete, print the username
                if(status.equals(Authentication.SUCCESS))
                    System.out.println("Username: " + UseUser.username);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start(); // Start the thread
    }

    @Override
    public void show() {
        Texture startTexture = new Texture(Gdx.files.internal("buttons/start.png"));
        TextureRegionDrawable startDrawable = new TextureRegionDrawable(startTexture);

        // Initialize the stage and set the input processor
        stage = new Stage(viewport, game.batch);
        Gdx.input.setInputProcessor(stage);

        // Create the "Start" button
        startButton = new ImageButton(startDrawable);
        startButton.setPosition(300, 200);  // Adjust the button's position
        startButton.setSize(200, 50);

        // Adjust the button's size
        startButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Transition to the next screen (MainMenuScreen)
                game.setScreen(new MainMenuScreen(game));
                dispose();  // Dispose of the current screen resources
            }

            @Override
            public void enter(InputEvent event,float x, float y,int pointer, Actor fromActor){
                startButton.setSize(300,100);
                startButton.setPosition(250, 200);
            }

            @Override
            public void exit(InputEvent event,float x, float y,int pointer, Actor fromActor){
                startButton.setSize(200,50);
                startButton.setPosition(300, 200);
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
        game.batch.begin();
        game.batch.draw(backgroundTexture,0,0,camera.viewportWidth,camera.viewportHeight);
        game.batch.end();
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
