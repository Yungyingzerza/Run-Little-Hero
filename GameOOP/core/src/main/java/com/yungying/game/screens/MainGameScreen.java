package com.yungying.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Null;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.yungying.game.Main;
import com.yungying.game.Player.CuteGirl;
import com.yungying.game.Player.OtherPlayer;
import com.yungying.game.Player.Player;
import com.yungying.game.Player.Tee;
import com.yungying.game.gameInputHandler.gameInputHandler;
import com.yungying.game.hooks.UseUser;
import com.yungying.game.map.*;
import com.yungying.game.states.gameStates;
import com.yungying.game.textureLoader.PlayerType;
import com.yungying.game.textureLoader.playerTextureList;
import org.json.JSONException;
import org.json.JSONObject;

public class MainGameScreen implements Screen {
    Player player;

    float latestCheckHealth;

    //camera to follow player
    private final OrthographicCamera camera;
    Main game;
    gameInputHandler inputHandler;
    Map currentMap;
    Map nextMap;

    //zoom for camera
    private float zoom = 1;

    float playerX;
    float playerY;

    boolean isColliding;
    BlockType blockType;
    float blockYHighest;

    float speedBeforeHit;
    boolean isResetSpeed = true;
    float latestHitTime;

    private final BitmapFont font;

    public static boolean isMenuShow;
    private  ImageButton resumeButton;
    private  Texture hoverResumeTexture;
    private  Texture resumeTexture;
    TextureRegionDrawable resumeDrawable;

    private  ImageButton restartButton;
    private  Texture hoverRestartTexture;
    private  Texture restartTexture;
    TextureRegionDrawable restartDrawable;

    private  ImageButton exitButton;
    private  Texture hoverExitTexture;
    private  Texture exitTexture;
    TextureRegionDrawable exitDrawable;
    private Stage stage;
    private Viewport viewport;

    private UseUser useUser;

    // Define initial size
    float initialWidth = 400;
    float initialHeight = 200;

    Music currentMusic;
    Music nextMusic;

    //tempLastTile
    Tile tempLastTile;

    //temp nextMap background
    Texture nextMapBackground;



    public MainGameScreen(Main game, String username, PlayerType playerType) {
        this.game = game;

        if(playerType == PlayerType.CUTEGIRL){
            player = new CuteGirl();
        }else if(playerType == PlayerType.TEE) {
            player = new Tee();
        }else{
            player = new CuteGirl();
        }

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 400);
        inputHandler = new gameInputHandler();
        currentMap = new MapLoader("map/Level1.json", 0);
        nextMap = new MapLoader(currentMap.getNextMapPath(), currentMap.getLastTile().getEndX());

        tempLastTile = currentMap.getLastTile();

        //music
        currentMusic = currentMap.getMusic();
        nextMusic = nextMap.getMusic();

        nextMapBackground = nextMap.getBackground();


        player.setSpeed(currentMap.getMapSpeed());
        player.setUsername(username);
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Bungee-Regular.otf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 12;
        font = generator.generateFont(parameter);
        font.getData().setScale(2.5f);
        generator.dispose();

        //+64 to get the middle of the player
        playerX = player.getPosition().x + 64;
        //64 to get the middle of the player (bottom)
        playerY = player.getPosition().y - 64;

        isColliding = false;
        blockType = BlockType.Air;
        blockYHighest = 0;

        latestCheckHealth = 0;
        latestHitTime = 0;

        isMenuShow = false;
        resumeTexture = new Texture(Gdx.files.internal("buttons/Resume/Resume.png"));
        hoverResumeTexture = new Texture(Gdx.files.internal("buttons/Resume/Hover.png"));
        resumeDrawable = new TextureRegionDrawable(resumeTexture);

        exitTexture = new Texture(Gdx.files.internal("buttons/Exit/Exit.png"));
        hoverExitTexture = new Texture(Gdx.files.internal("buttons/Exit/Hover.png"));
        exitDrawable = new TextureRegionDrawable(exitTexture);

        restartTexture = new Texture(Gdx.files.internal("buttons/Restart/Restart.png"));
        hoverRestartTexture = new Texture(Gdx.files.internal("buttons/Restart/Hover.png"));
        restartDrawable = new TextureRegionDrawable(restartTexture);

        viewport = new FitViewport(800, 400, camera);
        viewport.apply(); // Apply the viewport settings

        useUser = new UseUser();

    }


    @Override
    public void show() {

        // Initialize the stage and set the input processor
        stage = new Stage(viewport, game.batch);
        Gdx.input.setInputProcessor(stage);


        // Create the "Start" button
        resumeButton = new ImageButton(resumeDrawable);
        resumeButton.setSize(250, 150);
        resumeButton.setPosition(Gdx.graphics.getWidth() / 2f, Gdx.graphics.getHeight() / 2f);  // Centering the button

        resumeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                isMenuShow = false;
            }

            @Override
            public void enter(InputEvent event, float x, float y, int pointer, @Null Actor fromActor) {
                // Change the style when the mouse enters
                ImageButton.ImageButtonStyle hoverStyle = new ImageButton.ImageButtonStyle(resumeButton.getStyle());
                hoverStyle.imageUp = new TextureRegionDrawable(hoverResumeTexture); // Set hover texture
                resumeButton.setStyle(hoverStyle); // Apply the new style
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, @Null Actor fromActor) {
                // Revert to the original style when the mouse exits
                ImageButton.ImageButtonStyle normalStyle = new ImageButton.ImageButtonStyle(resumeButton.getStyle());
                normalStyle.imageUp = new TextureRegionDrawable(resumeTexture); // Set normal texture
                resumeButton.setStyle(normalStyle); // Apply the original style
                updateMenuPosition();  // Keep the button's position centered
            }
        });
        stage.addActor(resumeButton);

        restartButton = new ImageButton(restartDrawable);
        restartButton.setSize(250, 150);
        restartButton.setPosition(Gdx.graphics.getWidth() / 2f, Gdx.graphics.getHeight() / 2f);  // Centering the button

        restartButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {

                if(!isMenuShow) return;

                dispose();
                // Create a new instance of MainGameScreen to reset the game state
                game.setScreen(new MainGameScreen(game, player.getUsername(), player.getPlayerType()));
            }

            @Override
            public void enter(InputEvent event, float x, float y, int pointer, @Null Actor fromActor) {
                // Change the style when the mouse enters
                ImageButton.ImageButtonStyle hoverStyle = new ImageButton.ImageButtonStyle(restartButton.getStyle());
                hoverStyle.imageUp = new TextureRegionDrawable(hoverRestartTexture); // Set hover texture
                restartButton.setStyle(hoverStyle); // Apply the new style
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, @Null Actor fromActor) {
                // Revert to the original style when the mouse exits
                ImageButton.ImageButtonStyle normalStyle = new ImageButton.ImageButtonStyle(restartButton.getStyle());
                normalStyle.imageUp = new TextureRegionDrawable(restartTexture); // Set normal texture
                restartButton.setStyle(normalStyle); // Apply the original style
                updateMenuPosition();  // Keep the button's position centered
            }
        });


        stage.addActor(restartButton);







        exitButton = new ImageButton(exitDrawable);
        exitButton.setSize(250, 150);
        exitButton.setPosition(Gdx.graphics.getWidth() / 2f, Gdx.graphics.getHeight() / 2f);  // Centering the button

        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {

                if(!isMenuShow) return;

                game.setScreen(new MainMenuScreen(game));
                dispose();
            }

            @Override
            public void enter(InputEvent event, float x, float y, int pointer, @Null Actor fromActor) {
                // Change the style when the mouse enters
                ImageButton.ImageButtonStyle hoverStyle = new ImageButton.ImageButtonStyle(exitButton.getStyle());
                hoverStyle.imageUp = new TextureRegionDrawable(hoverExitTexture); // Set hover texture
                exitButton.setStyle(hoverStyle); // Apply the new style
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, @Null Actor fromActor) {
                // Revert to the original style when the mouse exits
                ImageButton.ImageButtonStyle normalStyle = new ImageButton.ImageButtonStyle(exitButton.getStyle());
                normalStyle.imageUp = new TextureRegionDrawable(exitTexture); // Set normal texture
                exitButton.setStyle(normalStyle); // Apply the original style
                updateMenuPosition();  // Keep the button's position centered
            }
        });


        stage.addActor(exitButton);

        if (currentMusic != null && !currentMusic.isPlaying()) {
            currentMusic.setLooping(true);  // Set looping if required
            currentMusic.play();            // Only play if it's not already playing
        }

    }

    @Override
    public void render(float delta) {

        if(!player.isDead()){
            handleNextMap();
            input();
            logic();
            camera();
            draw();
        }else{
            drawDead();
        }



        sendPlayerData();


    }

    public void drawDead() {
        updateMenuPosition();
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(Gdx.gl.GL_COLOR_BUFFER_BIT);
        game.batch.begin();
        font.draw(game.batch, "YOU DIED", camera.position.x-185, camera.position.y +200, 500, 1, true);
        game.batch.end();
        isMenuShow = true;
        draw();
    }



    public void handleNextMap(){

        //check if player is at the end of the map
        if(player.getPosition().x >= tempLastTile.getEndX()) {

            currentMap.setBackground(nextMapBackground);

            //set speed to new map speed
            player.setSpeed(currentMap.getMapSpeed());

            // Stop the current music if it's playing
            currentMusic.stop();
            currentMusic.dispose();

            currentMusic = nextMusic;
            currentMusic.play();

            //update the last tile
            tempLastTile = currentMap.getLastTile();
        }

        //load next map
        if(player.getPosition().x + 2048 >= currentMap.getLastTile().getEndX()) {

                currentMap.mergeMap(nextMap);
                nextMusic = nextMap.getMusic();
                nextMapBackground = nextMap.getBackground();
                nextMap = new MapLoader(currentMap.getNextMapPath(), currentMap.getLastTile().getEndX());

        }
    }


    public void input(){
        //input Part
        inputHandler.handleInput(player);
    }

    public void logic(){

        //decrease every 1 second by conparing the game time and the latest check health time
        if(gameStates.stateTime - latestCheckHealth >= 1){
            latestCheckHealth = gameStates.stateTime;
            player.setHealth(player.getHealth() - 1);
        }

        updateMenuPosition();

        playerX = player.getPosition().x + 64;
        playerY = player.getPosition().y - 64;

        if(currentMap.getCurrentTile(playerX) != null) {
            isColliding = currentMap.isColliding(playerX, playerY);
            blockYHighest = currentMap.getCurrentTile(playerX).getEndY();
            zoom = currentMap.getCurrentTile(playerX).getZoom();
            blockType = currentMap.getCurrentTile(playerX).getType();
        }

        //auto run to the right
        player.run(Gdx.graphics.getDeltaTime(), gameStates.stateTime);
        player.gravity(isColliding, blockYHighest, blockType);

        //auto run to the right for other players
        if(Main.otherPlayer != null){
            for (java.util.Map.Entry<String, OtherPlayer> entry : Main.otherPlayer.entrySet()) {
                OtherPlayer player = entry.getValue();
                player.run();
            }
        }

        //check if player is colliding with jelly
        CollectJelly tempScore = currentMap.isCollectJelly(player.getPosition().x, player.getPosition().y);
        if(tempScore.value > 0){
            if(tempScore.jelly != ItemType.Potion){
                player.setScore(player.getScore() + tempScore.value);
            }else{
                player.setHealth(player.getHealth() + tempScore.value);
            }

        }

        //check if player is colliding with spike
        int tempHealth = currentMap.isCollidingSpike(player.getPosition().x, player.getPosition().y);
        if(tempHealth < 0){
            player.setHealth(player.getHealth() + tempHealth);
            latestHitTime = gameStates.stateTime;
            speedBeforeHit = player.getSpeed();
            //decide the speed of the player after hit
            player.setSpeed(speedBeforeHit - 200f);
            isResetSpeed = false;
        }

        //after 1 second, set the speed back to normal
        if(gameStates.stateTime - latestHitTime >= 1 && !isResetSpeed){
            player.setSpeed(speedBeforeHit);
            isResetSpeed = true;
        }

        //state time
        gameStates.stateTime += Gdx.graphics.getDeltaTime();
    }

    public void camera(){
        //smooth zoom
        camera.zoom -= (camera.zoom - zoom) * 0.025f;
        //camera Part
        camera.position.set(player.getPosition().x - 300 + camera.viewportWidth / 2 * camera.zoom, camera.viewportHeight / 2 * camera.zoom, 0);
        camera.update();
        game.batch.setProjectionMatrix(camera.combined);
    }

    private void updateMenuPosition(){

        resumeButton.setPosition(camera.position.x -185f, camera.position.y);
        restartButton.setPosition(camera.position.x -185f, camera.position.y-120f );
        exitButton.setPosition(camera.position.x -185f, camera.position.y -240f);
    }


    public void draw(){
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(Gdx.gl.GL_COLOR_BUFFER_BIT);



        //render Part
        game.batch.begin();

        game.batch.draw(currentMap.getBackground(), camera.position.x - camera.viewportWidth / 2 * camera.zoom, camera.position.y - camera.viewportHeight / 2 * camera.zoom, 800 * camera.zoom, 400 * camera.zoom);

        //draw tiles
        for(int i = 0; i < currentMap.getTiles().size(); i++) {
            //skip null tiles
            if(currentMap.getTiles().elementAt(i).getType().equals(BlockType.Air)) continue;

            game.batch.draw(currentMap.getTileTextureAtIndex(i), currentMap.getTiles().elementAt(i).getStartX(), currentMap.getTiles().elementAt(i).getStartY(), 128, 128);
        }

        //draw jellies
        for(int i = 0; i < currentMap.getJellies().size(); i++) {

            if(currentMap.getJellies().elementAt(i).getType().equals(ItemType.Air)) continue;

            game.batch.draw(currentMap.getJellyTextureAtIndex(i), currentMap.getJellies().elementAt(i).getX(), currentMap.getJellies().elementAt(i).getY(), 64, 64);
        }

        //draw spikes
        for(int i = 0; i < currentMap.getSpikes().size(); i++) {

            if(currentMap.getSpikes().elementAt(i).getType().equals(SpikeType.Air)) continue;

            game.batch.draw(currentMap.getSpikesTextureAtIndex(i), currentMap.getSpikes().elementAt(i).getX(), currentMap.getSpikes().elementAt(i).getY(), 64, 64);
        }

        //draw other players
        if(Main.otherPlayer != null){
            for (java.util.Map.Entry<String, OtherPlayer> entry : Main.otherPlayer.entrySet()) {
                // Get the key and value
                OtherPlayer otherPlayer = entry.getValue();

                //reduce opacity for other players
                game.batch.setColor(1, 1, 1, 0.5f);

                //check if player is sliding
                if(otherPlayer.isSliding()){
                    game.batch.draw(playerTextureList.getSlideTexture(otherPlayer.getPlayerType(), otherPlayer.getStateTime() + gameStates.stateTime), otherPlayer.getPosition().x, otherPlayer.getPosition().y, 128, 128);
                }else if(otherPlayer.isJumping()){
                    game.batch.draw(playerTextureList.getJumpTexture(otherPlayer.getPlayerType(), otherPlayer.getStateTime() + gameStates.stateTime), otherPlayer.getPosition().x, otherPlayer.getPosition().y, 128, 128);
                }else if(otherPlayer.isDead()){
                    game.batch.draw(playerTextureList.getSlideTexture(otherPlayer.getPlayerType(), otherPlayer.getStateTime() + gameStates.stateTime), otherPlayer.getPosition().x, otherPlayer.getPosition().y, 128, 128);
                }else{
                    game.batch.draw(playerTextureList.getRunTexture(otherPlayer.getPlayerType(), otherPlayer.getStateTime() + gameStates.stateTime), otherPlayer.getPosition().x, otherPlayer.getPosition().y, 128, 128);
                }

                //reset opacity
                game.batch.setColor(1, 1, 1, 1);


                font.draw(game.batch, otherPlayer.getUsername(), otherPlayer.getPosition().x - 50, otherPlayer.getPosition().y + 50, 100, 1, false);


            }
        }

        game.batch.draw(player.getCurrentFrame(), player.getPosition().x, player.getPosition().y);

        font.draw(game.batch, player.getUsername(), playerX - 50, playerY + 50);

        //draw score top right
        font.draw(game.batch, ""+player.getScore(), camera.position.x + 600, camera.position.y + 300, 200, 1, true);

        //draw health top left
        font.draw(game.batch, "Health: "+player.getHealth(), camera.position.x - 600, camera.position.y + 300, 200, 1, true);



        game.batch.end();

        if(isMenuShow) {
            stage.draw();

        }
    }

    private void sendPlayerData() {

            try {
                //send data to server
                JSONObject data = new JSONObject();
                data.put("x", player.getPosition().x);
                data.put("y", player.getPosition().y);
                data.put("stateTime", gameStates.stateTime);
                data.put("score", player.getScore());
                data.put("username", player.getUsername());
                data.put("playerType", player.getPlayerType().toString());
                data.put("isSliding", player.isSliding());
                data.put("isJumping", player.isJumping());
                data.put("isDead", player.isDead());

                game.getSocket().emit("playerMoved", data);

            } catch (JSONException e) {
                throw new RuntimeException(e);
            }

    }


    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height);
        resumeButton.setPosition(camera.position.x, camera.position.y);
        restartButton.setPosition(camera.position.x, camera.position.y);
        exitButton.setPosition(camera.position.x, camera.position.y);
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        useUser.postScore(player.getScore());
        currentMusic.dispose();
        currentMap.dispose();
    }
}
