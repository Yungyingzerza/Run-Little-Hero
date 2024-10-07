package com.yungying.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
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
import com.yungying.game.hooks.UseUser;
import com.yungying.game.textureLoader.PlayerType;
import com.yungying.game.states.gameStates;

import java.text.DecimalFormat;

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

    TextureRegionDrawable characterDrawable;
    private Texture characterTexture;
    private Texture hoverCharacterTexture;

    private final Texture loginTexture;
    private final Texture hoverLoginTexture;
    TextureRegionDrawable loginDrawable;

    private PlayerType currentPlayerType;

    private final BitmapFont font;

    private final Music music;

    private final UseUser useUser;

    private float lastPollTime = 0;

    private Texture volumeTexture;
    private Texture muteTexture;

    private Texture jellySoundTexture;
    private Texture muteJellySoundTexture;

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
        font = new BitmapFont(Gdx.files.internal("fonts/Bungee-Regular.fnt"));
        //set the font size
        font.getData().setScale(0.5f);

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

        //on enter key pressed on text field
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

                loginButton.getStyle().imageUp = new TextureRegionDrawable(hoverLoginTexture);
            }

            @Override
            public void exit (InputEvent event, float x, float y, int pointer, @Null Actor fromActor){
                loginButton.getStyle().imageUp = new TextureRegionDrawable(loginTexture);
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

        characterTexture = new Texture(Gdx.files.internal("buttons/Character/Character.png"));
        hoverCharacterTexture = new Texture(Gdx.files.internal("buttons/Character/Hover.png"));
        characterDrawable = new TextureRegionDrawable(new TextureRegion(characterTexture));

        //next character button
        ImageButton nextCharacterButton = new ImageButton(characterDrawable);
        nextCharacterButton.setPosition(camera.viewportWidth / 2 + 120, camera.viewportHeight - 50);
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

            @Override
            public void enter (InputEvent event, float x, float y, int pointer, @Null Actor fromActor){
                isInputClick = false;

                nextCharacterButton.getStyle().imageUp = new TextureRegionDrawable(hoverCharacterTexture);
            }

            @Override
            public void exit (InputEvent event, float x, float y, int pointer, @Null Actor fromActor){
                nextCharacterButton.getStyle().imageUp = new TextureRegionDrawable(characterTexture);
            }
        });

        volumeTexture = new Texture(Gdx.files.internal("buttons/Volume/volume.png"));
        muteTexture = new Texture(Gdx.files.internal("buttons/Volume/volume-mute.png"));
        TextureRegionDrawable volumeDrawable;
        if(gameStates.isMusicOn){
            volumeDrawable = new TextureRegionDrawable(new TextureRegion(volumeTexture));
        }else{
            volumeDrawable = new TextureRegionDrawable(new TextureRegion(muteTexture));
        }

        ImageButton volumeButton = new ImageButton(volumeDrawable);
        volumeButton.setPosition(camera.viewportWidth - 130, camera.viewportHeight - 50);
        volumeButton.setSize(50, 50);
        volumeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(music.isPlaying()){
                    music.stop();
                    gameStates.isMusicOn = false;
                    volumeButton.getStyle().imageUp = new TextureRegionDrawable(muteTexture);
                }else{
                    music.play();
                    gameStates.isMusicOn = true;
                    volumeButton.getStyle().imageUp = new TextureRegionDrawable(volumeTexture);
                }
            }
        });

        stage.addActor(volumeButton);

        jellySoundTexture = new Texture(Gdx.files.internal("buttons/SoundEffectSymbol/on.png"));
        muteJellySoundTexture = new Texture(Gdx.files.internal("buttons/SoundEffectSymbol/off.png"));
        TextureRegionDrawable jellySoundDrawable;
        if(gameStates.isJellySoundOn){
            jellySoundDrawable = new TextureRegionDrawable(new TextureRegion(jellySoundTexture));
        }else{
            jellySoundDrawable = new TextureRegionDrawable(new TextureRegion(muteJellySoundTexture));
        }

        ImageButton jellySoundButton = new ImageButton(jellySoundDrawable);
        jellySoundButton.setPosition(camera.viewportWidth - 75, camera.viewportHeight - 50);
        jellySoundButton.setSize(50, 50);
        jellySoundButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(gameStates.isJellySoundOn){
                    gameStates.isJellySoundOn = false;
                    jellySoundButton.getStyle().imageUp = new TextureRegionDrawable(muteJellySoundTexture);
                }else{
                    gameStates.isJellySoundOn = true;
                    jellySoundButton.getStyle().imageUp = new TextureRegionDrawable(jellySoundTexture);
                }
            }
        });

        stage.addActor(jellySoundButton);


        if(gameStates.isMusicOn) music.play();

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

        font.draw(game.batch, "Current Character is: ", camera.viewportWidth / 2 - 200, camera.viewportHeight - 20);

        font.setColor(0.7f, 0.2f, 0.2f, 1);


        font.draw(game.batch, currentPlayerType.toString(), camera.viewportWidth / 2 - 200 + 230, camera.viewportHeight - 20);

        font.setColor(1.0f, 1.0f, 1.0f, 1.0f);


        font.draw(game.batch, "Highest Score", 0, 200);

        font.setColor(1f, 0.3f, 0.2f, 1);

        if(UseUser.topUsers != null){
            DecimalFormat df = new DecimalFormat("#,###");
            for(int i=0; i < UseUser.topUsers.size(); i++){
                font.draw(game.batch, i+1+"."+UseUser.topUsers.get(i).getUsername() + ": " + df.format(UseUser.topUsers.get(i).getHighestScore()), 10, 200 - 20 * (i+1));
            }
        }

        //set color back to white
        font.setColor(1.0f, 1.0f, 1.0f, 1.0f);

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

        usernameTextField.remove();
        playButton.remove();
        loginButton.remove();

        playTexture.dispose();
        hoverPlayTexture.dispose();

        loginButton.remove();
        loginTexture.dispose();
        hoverLoginTexture.dispose();

        characterTexture.dispose();
        hoverCharacterTexture.dispose();

        volumeTexture.dispose();
        muteTexture.dispose();

        font.dispose();
    }
}
