package com.yungying.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Null;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.yungying.game.Main;
import com.yungying.game.hooks.Authentication;
import com.yungying.game.hooks.UseUser;

import java.util.concurrent.CountDownLatch;

public class RegisterScreen implements Screen {

    Main game;
    private Stage stage;
    private Viewport viewport;
    private OrthographicCamera camera;
    private Skin skin;
    private TextField userNameTextField;
    private TextField passwordTextField;
    boolean isInputClick = false;
    private Label userNameLabel;
    private Label passwordLabel;
    private Label registerLabel;
    private TextButton registerButton;
    private UseUser useUser;
    private Texture backgroundTexture;

    public RegisterScreen(Main game){
        this.game = game;
        camera = new OrthographicCamera();
        viewport = new FitViewport(800, 400, camera); // Adjust the viewport size as needed
        skin = new Skin(Gdx.files.internal("uiskin.json"));
        useUser = new UseUser();
        backgroundTexture = new Texture(Gdx.files.internal("Background.png"));
    }


    @Override
    public void show() {

        stage = new Stage(viewport, game.batch);
        Gdx.input.setInputProcessor(stage);


        registerLabel = new Label("Register", skin);
        registerLabel.setPosition(200, 320);
        registerLabel.setFontScale(2);
        stage.addActor(registerLabel);

        userNameLabel = new Label("Username", skin);
        userNameLabel.setPosition(200, 290);
        stage.addActor(userNameLabel);

        passwordLabel = new Label("Password", skin);
        passwordLabel.setPosition(200, 160);
        stage.addActor(passwordLabel);

        Texture backgroundTexture = new Texture(Gdx.files.internal("test.jpg"));
        Drawable backgroundDrawable = new TextureRegionDrawable(backgroundTexture);

        TextField.TextFieldStyle textFieldStyle = new TextField.TextFieldStyle();
        textFieldStyle.font = skin.getFont("default-font");
        textFieldStyle.fontColor = skin.getColor("black");
        textFieldStyle.background = backgroundDrawable;


        Texture cursorTexture = new Texture(Gdx.files.internal("test2.png")); // Replace with your own cursor image if needed
        TextureRegionDrawable cursorDrawable = new TextureRegionDrawable(new TextureRegion(cursorTexture));
        cursorDrawable.setMinHeight(50);
        cursorDrawable.setMinWidth(20);
        textFieldStyle.cursor = cursorDrawable;


        // Create a TextField
        userNameTextField = new TextField("", textFieldStyle);


        userNameTextField.setPosition(200, 200);
        userNameTextField.setSize(400, 70);

        userNameTextField.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                stage.setKeyboardFocus(userNameTextField);
                isInputClick = true;
                return true;
            }

            @Override
            public void exit(InputEvent event,float x, float y,int pointer, Actor fromActor){
                isInputClick = false;
            }

        });

        stage.addActor(userNameTextField);

        // Create a TextField
        passwordTextField = new TextField("", textFieldStyle);


        passwordTextField.setPosition(200, 50);
        passwordTextField.setSize(400, 70);

        passwordTextField.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                stage.setKeyboardFocus(passwordTextField);
                isInputClick = true;
                return true;
            }

            @Override
            public void exit(InputEvent event,float x, float y,int pointer, Actor fromActor){
                isInputClick = false;
            }

        });

        stage.addActor(passwordTextField);

        // Create the "Register" button
        registerButton = new TextButton("Register",skin);
        registerButton.setPosition(200, 0);  // Adjust the button's position
        registerButton.setSize(200, 50);

        registerButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                String username = userNameTextField.getText();
                String password = passwordTextField.getText();

                CountDownLatch latch = new CountDownLatch(1); // Initialize latch with count of 1

                System.out.println("register...");
                Enum<Authentication> status = useUser.register(username, password, latch); // Pass the latch

                if(status.equals(Authentication.SUCCESS)) {
                    System.out.println("Username: " + UseUser.username);
                    game.setScreen(new MainMenuScreen(game));
                    dispose();
                }
                if(status.equals(Authentication.USER_ALREADY_EXISTS))
                    System.out.println("User NOT FOUND");


            }

            @Override
            public void enter (InputEvent event, float x, float y, int pointer, @Null Actor fromActor){
                isInputClick = false;
            }
        });

        stage.addActor(registerButton);


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
        stage.act(delta);  // Process input and actions
        stage.draw();

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
