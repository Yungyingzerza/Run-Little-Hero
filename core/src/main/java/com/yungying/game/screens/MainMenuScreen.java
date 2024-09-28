package com.yungying.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.yungying.game.Main;


public class MainMenuScreen implements Screen {

    Main game;

    private OrthographicCamera camera;
    Viewport viewport;

    Skin skin;
    private TextField usernameTextField;
    private TextButton playButton;
    private Label usernameLabel;
    private Label idLabel;

    Stage stage;

    public MainMenuScreen(Main game) {
        this.game = game;
        TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("uiskin.atlas"));
        skin = new Skin(Gdx.files.internal("uiskin.json"));
        skin.addRegions(atlas);

        playButton = new TextButton("Play", skin);
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
        usernameLabel = new Label("Username", skin);
        usernameLabel.setPosition(200, 320); // Set the position just above the TextField
        stage.addActor(usernameLabel);

        // Create a TextField
        usernameTextField = new TextField("", skin);
        usernameTextField.setPosition(200, 200);
        usernameTextField.setSize(400, 100);
        stage.addActor(usernameTextField);

        usernameTextField.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                stage.setKeyboardFocus(usernameTextField);
                return true;
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


        // Create a Play button
        playButton = new TextButton("Play", skin);
        playButton.setPosition(300, 100);
        playButton.setSize(200, 50);
        stage.addActor(playButton);

        // Add click listener to the Play button
        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {

                stage.setKeyboardFocus(null);

                // Only process button click if the TextField is not focused
                if (stage.getKeyboardFocus() == null || stage.getKeyboardFocus() == playButton) {
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
