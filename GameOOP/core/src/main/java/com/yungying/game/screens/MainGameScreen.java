package com.yungying.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.yungying.game.Main;
import com.yungying.game.Player.CuteGirl;
import com.yungying.game.Player.OtherPlayer;
import com.yungying.game.Player.Player;
import com.yungying.game.gameInputHandler.gameInputHandler;
import com.yungying.game.map.BlockType;
import com.yungying.game.map.ItemType;
import com.yungying.game.map.Map;
import com.yungying.game.map.MapLoader;
import com.yungying.game.states.gameStates;
import org.json.JSONException;
import org.json.JSONObject;

public class MainGameScreen implements Screen {
    Player player;

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

    private final BitmapFont font;


    public MainGameScreen(Main game, String username) {
        this.game = game;
        player = new CuteGirl();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 400);
        inputHandler = new gameInputHandler();
        currentMap = new MapLoader("map/Level1.json", 0);
        nextMap = new MapLoader(currentMap.getNextMapPath(), currentMap.getLastTile().getEndX());
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
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {

        checkDeath();
        handleNextMap();
        input();
        logic();
        camera();
        draw();

        sendPlayerData();


    }

    public void checkDeath(){
        if(player.isDead()){
            game.setScreen(new MainMenuScreen(game));
            dispose();
        }
    }

    public void handleNextMap(){
        if(player.getPosition().x >= currentMap.getLastTile().getEndX()) {

            if(currentMap.getNextMap().equals("Level2")){
                currentMap = nextMap;
                nextMap = new MapLoader(currentMap.getNextMapPath(), currentMap.getLastTile().getEndX());
            }else if(currentMap.getNextMap().equals("Level1")){
                currentMap = nextMap;
                nextMap = new MapLoader(currentMap.getNextMapPath(), currentMap.getLastTile().getEndX());
            }

            player.setSpeed(currentMap.getMapSpeed());

            player.setPosition(currentMap.getFirstTile().getStartX(), currentMap.getFirstTile().getEndY());
        }
    }

    public void input(){
        //input Part
        inputHandler.handleInput(player);
    }

    public void logic(){

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
                player.setPosition(player.getPosition().x + player.getSpeed() * Gdx.graphics.getDeltaTime(), player.getPosition().y);
            }
        }

        //check if player is colliding with jelly
        int tempScore = currentMap.isCollectJelly(player.getPosition().x, player.getPosition().y);
        if(tempScore > 0){
            player.setScore(player.getScore() + tempScore);
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

        //draw other players
        if(Main.otherPlayer != null){
            for (java.util.Map.Entry<String, OtherPlayer> entry : Main.otherPlayer.entrySet()) {
                // Get the key and value
                OtherPlayer player = entry.getValue();

                game.batch.draw(player.getCurrentFrame(), player.getPosition().x, player.getPosition().y, 128,128);
                font.draw(game.batch, player.getUsername(), player.getPosition().x - 50, player.getPosition().y + 50, 100, 1, false);
            }
        }

        game.batch.draw(player.getCurrentFrame(), player.getPosition().x, player.getPosition().y);

        font.draw(game.batch, player.getUsername(), playerX - 50, playerY + 50);

        //draw score top right
        font.draw(game.batch, ""+player.getScore(), camera.position.x + 600, camera.position.y + 300, 200, 1, true);

        game.batch.end();
    }

    private void sendPlayerData(){
        try {
            //send data to server
            JSONObject data = new JSONObject();
            data.put("currentFrame", player.getCurrentFrame());
            data.put("x", player.getPosition().x);
            data.put("y", player.getPosition().y);
            data.put("stateTime", gameStates.stateTime);
            data.put("score", player.getScore());
            data.put("username", player.getUsername());
            data.put("playerType", player.getPlayerType().toString());
            game.getSocket().emit("playerMoved", data);

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void resize(int width, int height) {}

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        currentMap.dispose();
    }
}
