package com.yungying.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.yungying.game.Main;
import com.yungying.game.Player.Player;
import com.yungying.game.gameInputHandler.gameInputHandler;
import com.yungying.game.map.Map1;
import com.yungying.game.map.Tile;
import com.yungying.game.states.gameStates;

import java.util.Vector;

public class MainGameScreen implements Screen {
    Player player;

    //camera to follow player
    private final OrthographicCamera camera;
    Main game;
    gameInputHandler inputHandler;
    Map1 map1;
    Map1 map2;


    public MainGameScreen(Main game) {
        this.game = game;
        player = new Player();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 400);
        inputHandler = new gameInputHandler();
        map1 = new Map1(player.getPosition().x, 0, "Grass");
        map2 = new Map1(map1.getLastTile().getEndX(), 0, "GrassWinter");
        player.setSpeed(map1.getMapSpeed());
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

        if(map1.getCurrentTile(playerX) != null) {
            isColliding = map1.isColliding(playerX, playerY);
            endY = map1.getCurrentTile(playerX).getEndY();
            zoom = map1.getCurrentTile(playerX).getZoom();
            type = map1.getCurrentTile(playerX).getType();
        }

        if(playerX >= map1.getLastTile().getEndX()) {
            map1 = map2;

            //random between 0 and 1
            int random = (int) (Math.random() * 2);
            if(random == 0) {
                map2 = new Map1(map1.getLastTile().getEndX(), 0, "Grass");
            }else{
                map2 = new Map1(map1.getLastTile().getEndX(), 0, "GrassWinter");
            }

            player.setSpeed(map1.getMapSpeed());

            player.setPosition(map1.getFirstTile().getStartX(), map1.getFirstTile().getEndY());
        }

        //input Part
        inputHandler.handleInput(player);

        //auto run to the right
        player.run(Gdx.graphics.getDeltaTime(), gameStates.stateTime);
        player.gravity(isColliding, endY, type);

        //state time
        gameStates.stateTime += Gdx.graphics.getDeltaTime();

        //smooth zoom
        camera.zoom -= (camera.zoom - zoom) * 0.025f;


        //camera Part
        camera.position.set(player.getPosition().x + camera.viewportWidth / 2 * camera.zoom, camera.viewportHeight / 2 * camera.zoom, 0);
        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        //render Part
        game.batch.begin();

        game.batch.draw(map1.getBackground(), camera.position.x - camera.viewportWidth / 2 * camera.zoom, camera.position.y - camera.viewportHeight / 2 * camera.zoom, 800 * camera.zoom, 400 * camera.zoom);

        for (Tile tile : map1.getTiles()) {
            if(tile.getType().equals("Grass") || tile.getType().equals("GrassWinter")) {
                game.batch.draw(map1.getGrass(), tile.getStartX(), tile.getStartY(), 128, 128);
            }
        }




        game.batch.draw(player.getCurrentFrame(), player.getPosition().x, player.getPosition().y);



        game.batch.end();

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
        map1.dispose();
    }
}
