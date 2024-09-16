package com.yungying.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.yungying.game.Main;
import com.yungying.game.Player.Player;
import com.yungying.game.gameInputHandler.gameInputHandler;
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

    private Animation<Texture> runAnimation;
    private  Animation<Texture> jumpAnimation;
    private  Animation<Texture> slideAnimation;

    public MainGameScreen(Main game) {
        this.game = game;
        player = new Player();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 400);
        inputHandler = new gameInputHandler();
        currentMap = new MapLoader("map/Level1.json", 0);
        nextMap = new MapLoader(currentMap.getNextMapPath(), currentMap.getLastTile().getEndX());
        player.setSpeed(currentMap.getMapSpeed());

        runAnimation = new Animation<Texture>(0.1f, new Texture("characters/Tee/TeeRunLeft.png"), new Texture("characters/Tee/TeeAgainLeft.png"), new Texture("characters/Tee/TeeRunRight.png"), new Texture("characters/Tee/TeeAgainRight.png"), new Texture("characters/Tee/TeeAgainLeft.png"));
        jumpAnimation = new Animation<Texture>(0.1f, new Texture("characters/Tee/TeeJump.png"));
        slideAnimation = new Animation<Texture>(0.1f, new Texture("characters/Tee/TeeSlide.png"));
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

        //auto run to the right for other players
        if(Main.otherPlayer != null){
            for (java.util.Map.Entry<String, Player> entry : Main.otherPlayer.entrySet()) {
                Player value = entry.getValue();
                value.setPosition(value.getPosition().x + value.getSpeed() * Gdx.graphics.getDeltaTime(), value.getPosition().y);
            }
        }

        //check if player is colliding with jelly
        int tempScore = currentMap.isCollectJelly(player.getPosition().x, player.getPosition().y);
        if(tempScore > 0){
            player.setScore(player.getScore() + tempScore);
        }

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
        if(Main.otherPlayer != null){
            for (java.util.Map.Entry<String, Player> entry : Main.otherPlayer.entrySet()) {
                // Get the key and value
                Player value = entry.getValue();


                game.batch.draw(getCurrentFrameByStateTime(value), value.getPosition().x, value.getPosition().y);
            }
        }



        game.batch.draw(player.getCurrentFrame(), player.getPosition().x, player.getPosition().y);



        game.batch.end();

        sendPlayerData();


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
            data.put("username", "Username001");
            game.getSocket().emit("playerMoved", data);

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    private Texture getCurrentFrameByStateTime(Player player){


        return runAnimation.getKeyFrame(gameStates.stateTime, true);
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
