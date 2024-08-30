package com.yungying.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.yungying.game.Main;

public class MainGameScreen implements Screen {

    float speed = 200;

    float x;
    float y;
    float stateTime;
    Animation<Texture> runAnimation;
    Animation<Texture> jumpAnimation;

    Texture currentFrame;

    //camera to follow player
    private OrthographicCamera camera;

    Main game;

    public MainGameScreen(Main game) {
        this.game = game;
        x = 0;
        y = 0;
        runAnimation = new Animation<Texture>(0.1f, new Texture("characters/Tee/TeeRunLeft.png"), new Texture("characters/Tee/TeeRunRight.png"));
        jumpAnimation = new Animation<Texture>(0.1f, new Texture("characters/Tee/TeeJump.png"));
        currentFrame = runAnimation.getKeyFrame(stateTime, true);
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
        x += speed * Gdx.graphics.getDeltaTime();

        if(Gdx.input.isKeyPressed(Input.Keys.UP)){
            y += 800 * Gdx.graphics.getDeltaTime();
            currentFrame = jumpAnimation.getKeyFrame(stateTime, true);
        }

        //gravity
        if(y > 0){
            y -= 150 * Gdx.graphics.getDeltaTime();

        }else{
            y = 0;
            currentFrame = runAnimation.getKeyFrame(stateTime, true);
        }

        stateTime += Gdx.graphics.getDeltaTime();

        camera.position.set(x + camera.viewportWidth / 2, 0 + camera.viewportHeight / 2, 0);
        camera.update();

        game.batch.setProjectionMatrix(camera.combined);


        game.batch.begin();
        game.batch.draw(currentFrame, x, y, 150, 150);
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
        runAnimation.getKeyFrames()[0].dispose();
        runAnimation.getKeyFrames()[1].dispose();

        jumpAnimation.getKeyFrames()[0].dispose();


    }
}
