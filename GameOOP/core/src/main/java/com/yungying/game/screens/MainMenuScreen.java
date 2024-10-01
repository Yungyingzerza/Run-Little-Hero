package com.yungying.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
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


public class MainMenuScreen implements Screen {

    Main game;

    private OrthographicCamera camera;
    Viewport viewport;

    Skin skin;
    private TextField usernameTextField;
    private ImageButton playButton;
    private TextButton loginButton;
    private Label idLabel;
    boolean isInputClick = false;
    Stage stage;

    private Texture playTexture;
    private Texture hoverPlayTexture;

    public MainMenuScreen(Main game) {
        this.game = game;
        TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("uiskin.atlas"));
        skin = new Skin(Gdx.files.internal("uiskin.json"));
        skin.addRegions(atlas);

        loginButton = new TextButton("Login",skin);
        usernameTextField = new TextField("Username", skin);


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

        stage = new Stage(viewport, game.batch);
        Gdx.input.setInputProcessor(stage);

        //create a Label for ID top left
        idLabel = new Label("ID: connecting to server", skin);
        idLabel.setPosition(0, 380);
        stage.addActor(idLabel);

        // Create a Label for Username
        Label usernameLabel = new Label("Username", skin);
        usernameLabel.setPosition(200, 320); // Set the position just above the TextField
        stage.addActor(usernameLabel);

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
        usernameTextField = new TextField("", textFieldStyle);


        usernameTextField.setPosition(200, 200);
        usernameTextField.setSize(400, 100);

        usernameTextField.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                stage.setKeyboardFocus(usernameTextField);
                isInputClick = true;
                return true;
            }

            @Override
            public void exit(InputEvent event,float x, float y,int pointer, Actor fromActor){
                isInputClick = false;
            }

        });

        //on enter key pressed on textfield
        usernameTextField.setTextFieldListener(new TextField.TextFieldListener() {
            @Override
            public void keyTyped(TextField textField, char inputKey) {
                if(inputKey == '\r' || inputKey == '\n'){
                    //start game
                    String username = usernameTextField.getText();

                    if(username.isEmpty()){
                        game.setScreen(new MainGameScreen(game, "noName"));
                        dispose();
                        return;
                    }

                    game.setScreen(new MainGameScreen(game, username));
                    dispose();
                }
            }
        });


        stage.addActor(usernameTextField);

        playTexture = new Texture(Gdx.files.internal("buttons/Play/Default.png"));
        hoverPlayTexture = new Texture(Gdx.files.internal("buttons/Play/Hover.png"));
        TextureRegionDrawable playDrawable = new TextureRegionDrawable(new TextureRegion(playTexture));


        // Create a Play button
        playButton = new ImageButton(playDrawable);
        playButton.setPosition(300, 100);
        playButton.setSize(200, 100);
        stage.addActor(playButton);

        // Add click listener to the Play button
        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                    String username = usernameTextField.getText();

                    if(username.isEmpty()){
                        game.setScreen(new MainGameScreen(game, "noName"));
                        dispose();
                        return;
                    }

                    game.setScreen(new MainGameScreen(game, username));
                    dispose();


            }

            @Override
            public void enter (InputEvent event, float x, float y, int pointer, @Null Actor fromActor){
                isInputClick = false;

                playButton.getStyle().imageUp = new TextureRegionDrawable(hoverPlayTexture);
            }

            @Override
            public void exit (InputEvent event, float x, float y, int pointer, @Null Actor fromActor){
                playButton.getStyle().imageUp = new TextureRegionDrawable(playTexture);
            }
        });

        // Create a Play button
        loginButton = new TextButton("Login", skin);
        loginButton.setPosition(300, 50);
        loginButton.setSize(200, 50);
        stage.addActor(loginButton);

        // Add click listener to the Play button
        loginButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new LoginScreen(game));
                dispose();
            }

            @Override
            public void enter (InputEvent event, float x, float y, int pointer, @Null Actor fromActor){
                isInputClick = false;
            }
        });

        stage.addListener(new ClickListener(){
            @Override
            public void clicked (InputEvent event, float x, float y) {
                if (isInputClick){
                    return;
                }
                stage.setKeyboardFocus(null);
            }

        });


    }


    @Override
    public void render(float v) {
        Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
        Gdx.gl.glClear(Gdx.gl.GL_COLOR_BUFFER_BIT);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        stage.act(v);
        stage.draw();

        if(game.getSocket() != null){
            idLabel.setText("ID: " + game.getSocket().id());
        }

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
        stage.dispose();
        skin.dispose();
    }
}
