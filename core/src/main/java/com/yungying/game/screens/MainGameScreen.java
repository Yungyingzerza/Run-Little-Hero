package com.yungying.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.yungying.game.Main;
import com.yungying.game.Player.Player;

public class MainGameScreen implements Screen {
    float stateTime;
    Player player;

    //camera to follow player
    private OrthographicCamera camera;

    Main game;

    public MainGameScreen(Main game) {
        this.game = game;
        player = new Player();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 400);
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(Gdx.gl.GL_COLOR_BUFFER_BIT);

        //auto run to the right
        player.run(Gdx.graphics.getDeltaTime(), stateTime);

        if(Gdx.input.isKeyJustPressed(Input.Keys.UP)){
            player.jump(stateTime);
        }

        if(Gdx.input.isKeyPressed(Input.Keys.DOWN)){
            player.slide(stateTime);
        }



        player.gravity(stateTime);

        stateTime += Gdx.graphics.getDeltaTime();

        camera.position.set(player.getPosition().x + camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
        camera.update();

        game.batch.setProjectionMatrix(camera.combined);


        game.batch.begin();
        game.batch.draw(player.getCurrentFrame(), player.getPosition().x, player.getPosition().y, 150, 150);
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

        player.getCurrentFrame().dispose();


    }
}
