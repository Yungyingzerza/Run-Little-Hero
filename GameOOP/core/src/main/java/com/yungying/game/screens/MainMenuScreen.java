package com.yungying.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
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
import com.yungying.game.hooks.UseUser;
import com.yungying.game.textureLoader.PlayerType;

public class MainMenuScreen implements Screen {

    Main game;

    private OrthographicCamera camera;
    Viewport viewport;

    Skin skin;
    private TextField usernameTextField;
    private ImageButton playButton;
    private ImageButton loginButton;
    boolean isInputClick = false;
    Stage stage;

    private Texture playTexture;
    private Texture hoverPlayTexture;

    private Texture loginTexture;
    private Texture hoverLoginTexture;
    TextureRegionDrawable loginDrawable;

    private PlayerType currentPlayerType;

    private final BitmapFont font;

    private Music music;

    private UseUser useUser;

    private float lastPollTime = 0;

    public MainMenuScreen(Main game) {
        this.game = game;
        TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("uiskin.atlas"));
        skin = new Skin(Gdx.files.internal("uiskin.json"));
        skin.addRegions(atlas);


        loginTexture = new Texture(Gdx.files.internal("buttons/Login/Login.png"));
        hoverLoginTexture = new Texture(Gdx.files.internal("buttons/Login/Hover.png"));
        loginDrawable = new TextureRegionDrawable(new TextureRegion(loginTexture));


        usernameTextField = new TextField("Username", skin);


        currentPlayerType = PlayerType.CUTEGIRL;
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Bungee-Regular.otf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 16;
        font = generator.generateFont(parameter);

        generator.dispose();

        music = LobbyScreen.music;

        useUser = new UseUser();

        useUser.getTopUsers();
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


        // Create a Label for Username
        Label usernameLabel = new Label("Username", skin);
        usernameLabel.setPosition(200, 320); // Set the position just above the TextField
        stage.addActor(usernameLabel);


        Texture backgroundTexture = new Texture(Gdx.files.internal("textbox/Fantasy_TextBox_A01-1_Red.png"));
        Drawable backgroundDrawable = new TextureRegionDrawable(backgroundTexture);

        TextField.TextFieldStyle textFieldStyle = new TextField.TextFieldStyle();
        textFieldStyle.font = font;
        textFieldStyle.fontColor = skin.getColor("white");
        textFieldStyle.background = backgroundDrawable;

        textFieldStyle.background.setLeftWidth(100);
        textFieldStyle.background.setRightWidth(70);
        textFieldStyle.background.setTopHeight(20);
        textFieldStyle.background.setBottomHeight(20);


        Texture cursorTexture = new Texture(Gdx.files.internal("test2.png")); // Replace with your own cursor image if needed
        TextureRegionDrawable cursorDrawable = new TextureRegionDrawable(new TextureRegion(cursorTexture));
        cursorDrawable.setMinHeight(50);
        cursorDrawable.setMinWidth(20);
        textFieldStyle.cursor = cursorDrawable;


        // Create a TextField
        usernameTextField = new TextField("", textFieldStyle);

        //if login is successful, set the username to the textfield
        if(UseUser.username != null){
            usernameTextField.setText(UseUser.username);
        }


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

                    music.stop();

                    if(username.isEmpty()){
                        game.setScreen(new MainGameScreen(game, "noName", currentPlayerType));
                        dispose();
                        return;
                    }

                    game.setScreen(new MainGameScreen(game, username, currentPlayerType));
                    dispose();
                }
            }
        });


        stage.addActor(usernameTextField);

        playTexture = new Texture(Gdx.files.internal("buttons/Play/Play.png"));
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

                    music.stop();

                    if(username.isEmpty()){
                        game.setScreen(new MainGameScreen(game, "noName", currentPlayerType));
                        dispose();
                        return;
                    }

                    game.setScreen(new MainGameScreen(game, username, currentPlayerType));
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
        loginButton = new ImageButton(loginDrawable);
        loginButton.setPosition(300, 50);
        loginButton.setSize(200, 50);
        stage.addActor(loginButton);

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

        //next character button
        TextButton nextCharacterButton = new TextButton("Next Character", skin);
        nextCharacterButton.setPosition(camera.viewportWidth / 2 + 150, camera.viewportHeight - 50);
        nextCharacterButton.setSize(150, 50);
        stage.addActor(nextCharacterButton);

        nextCharacterButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(currentPlayerType == PlayerType.CUTEGIRL){
                    currentPlayerType = PlayerType.TEE;
                }else if(currentPlayerType == PlayerType.TEE){
                    currentPlayerType = PlayerType.CUTEGIRL;
                }
            }
        });


        music.play();

    }


    @Override
    public void render(float v) {
        Gdx.gl.glClearColor(0.82f, 0.77f, 0.91f, 1); // Light pastel purple

        Gdx.gl.glClear(Gdx.gl.GL_COLOR_BUFFER_BIT);

        lastPollTime += Gdx.graphics.getDeltaTime();

        //long polling to get top users every 3 seconds
        if(lastPollTime > 3){
            useUser.getTopUsers();
            System.out.println("polling");
            lastPollTime = 0;
        }


        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        stage.act(v);
        stage.draw();


        game.batch.begin();

        font.draw(game.batch, "Current Character is: "+ currentPlayerType.toString(), camera.viewportWidth / 2 - 200, camera.viewportHeight - 20);

        font.draw(game.batch, "Highest Score", 0, camera.viewportHeight / 2 + 170);


        if(UseUser.topUsers != null){
            for(int i=0; i < UseUser.topUsers.size(); i++){
                font.draw(game.batch, UseUser.topUsers.get(i).getUsername() + ": " + UseUser.topUsers.get(i).getHighestScore(), 0, camera.viewportHeight / 2 - 50 * (i+1) + 170);
            }
        }


        game.batch.end();

    }


    @Override
    public void resize(int width, int height) {
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
