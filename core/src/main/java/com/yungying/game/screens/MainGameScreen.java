package com.yungying.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.yungying.game.Main;
import com.yungying.game.Player.Player;
import com.yungying.game.gameInputHandler.gameInputHandler;
import com.yungying.game.map.Map;
import com.yungying.game.map.MapLoader;
import com.yungying.game.states.gameStates;
import netscape.javascript.JSObject;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedList;

public class MainGameScreen implements Screen {
    Player player;
    public static Player otherPlayer;

    private float lastestSendTime = 0;

    //camera to follow player
    private final OrthographicCamera camera;
    Main game;
    gameInputHandler inputHandler;
    Map currentMap;
    Map nextMap;


    public MainGameScreen(Main game) {
        this.game = game;
        player = new Player();
        otherPlayer = new Player();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 400);
        inputHandler = new gameInputHandler();
        currentMap = new MapLoader("map/Level1.json", 0);
        nextMap = new MapLoader(currentMap.getNextMapPath(), currentMap.getLastTile().getEndX());
        player.setSpeed(currentMap.getMapSpeed());
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(Gdx.gl.GL_COLOR_BUFFER_BIT);

        if(player.isDead()){
            game.setScreen(new MainMenuScreen(game));
            dispose();
        }

        //+64 to get the middle of the player
        float playerX = player.getPosition().x + 64;

        //64 to get the middle of the player (bottom)
        float playerY = player.getPosition().y - 64;

        boolean isColliding = false;
        String type = "null";
        float endY = 0;
        float zoom = 1;

        if(currentMap.getCurrentTile(playerX) != null) {
            isColliding = currentMap.isColliding(playerX, playerY);
            endY = currentMap.getCurrentTile(playerX).getEndY();
            zoom = currentMap.getCurrentTile(playerX).getZoom();
            type = currentMap.getCurrentTile(playerX).getType();
        }

        if(playerX >= currentMap.getLastTile().getEndX()) {

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

        //input Part
        inputHandler.handleInput(player);

        //auto run to the right
        player.run(Gdx.graphics.getDeltaTime(), gameStates.stateTime);
        player.gravity(isColliding, endY, type);

        //check if player is colliding with jelly
        int tempScore = currentMap.isCollectJelly(player.getPosition().x, player.getPosition().y);
        if(tempScore > 0){
            player.setScore(player.getScore() + tempScore);
        }

//        System.out.println("Score: " + player.getScore());

        //state time
        gameStates.stateTime += Gdx.graphics.getDeltaTime();

        //smooth zoom
        camera.zoom -= (camera.zoom - zoom) * 0.025f;


        //camera Part
        camera.position.set(player.getPosition().x - 300 + camera.viewportWidth / 2 * camera.zoom, camera.viewportHeight / 2 * camera.zoom, 0);
        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        //render Part
        game.batch.begin();

        game.batch.draw(currentMap.getBackground(), camera.position.x - camera.viewportWidth / 2 * camera.zoom, camera.position.y - camera.viewportHeight / 2 * camera.zoom, 800 * camera.zoom, 400 * camera.zoom);

        //draw tiles
        for(int i = 0; i < currentMap.getTiles().size(); i++) {
            //skip null tiles
            if(currentMap.getTiles().elementAt(i).getType().equals("null")) continue;

            game.batch.draw(currentMap.getTileTextureAtIndex(i), currentMap.getTiles().elementAt(i).getStartX(), currentMap.getTiles().elementAt(i).getStartY(), 128, 128);
        }

        //draw jellies
        for(int i = 0; i < currentMap.getJellies().size(); i++) {

            if(currentMap.getJellies().elementAt(i).getType().equals("null")) continue;

            game.batch.draw(currentMap.getJellyTextureAtIndex(i), currentMap.getJellies().elementAt(i).getX(), currentMap.getJellies().elementAt(i).getY(), 64, 64);
        }

        //draw other players
        if(otherPlayer != null){
            game.batch.draw(otherPlayer.getTestTexture(), otherPlayer.getPosition().x, otherPlayer.getPosition().y);
        }



        game.batch.draw(player.getCurrentFrame(), player.getPosition().x, player.getPosition().y);



        game.batch.end();

        sendPlayerData();

//        if(gameStates.stateTime - lastestSendTime > 0.1){
//        }



    }

    private void sendPlayerData(){
        try {
            //send data to server
            JSONObject data = new JSONObject();
            data.put("currentFrame", player.getCurrentFrame());
            data.put("x", player.getPosition().x);
            data.put("y", player.getPosition().y);
            data.put("score", player.getScore());
            data.put("username", "yungying");
            game.getSocket().emit("playerMoved", data);
            lastestSendTime = gameStates.stateTime;
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void resize(int width, int height) {

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
        player.dispose();
        currentMap.dispose();
    }
}
