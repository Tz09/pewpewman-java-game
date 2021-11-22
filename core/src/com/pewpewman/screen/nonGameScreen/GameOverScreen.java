package com.pewpewman.screen.nonGameScreen;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.pewpewman.Pewpewman;
import com.pewpewman.audio.*;
import com.pewpewman.dialogue.*;
import com.pewpewman.screen.*;
import com.pewpewman.screen.transition.*;
import com.pewpewman.util.*;

public class GameOverScreen extends AbstractScreen {

    SpriteBatch batch;
    Texture gameOverBanner;
    private final MusicManager musicManager = Pewpewman.musicManager;
    private final StoryBoxUI gameOverBox = new StoryBoxUI();

    public GameOverScreen(Pewpewman game)
    {
        super(game);
        batch = new SpriteBatch();
        gameOverBanner = new Texture("font/game_over.png");

        Pewpewman.currentScreen = SCREEN.GAMEOVER;
        gameOverBox.createStoryBoxUI("Oops Pewpewman,\n\n\n" +
                "It seems that you are too weak...\n\n\n" +
                "and you'll never be able to defeat me!!\n\n\n" +
                "Press X to restart!!!");
    }

    @Override
    public void render(float delta) {

        Gdx.gl.glClearColor(0,0,0,0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        int bannerWidth = 600;
        int bannerHeight = 200;

        batch.draw(gameOverBanner, Gdx.graphics.getWidth()/2 - bannerWidth /2, Gdx.graphics.getHeight()/2 - bannerHeight /2, bannerWidth, bannerHeight);

        batch.end();

            if (Gdx.input.isKeyPressed(Input.Keys.X) && gameOverBox.getStoryBox().isFinished()) {
                if (Pewpewman.currentScreen == SCREEN.LEVEL1) {
                    getApp().startTransition(
                            this,
                            getApp().getLevel1(),
                            new FadeOutTransition(0.3f, Color.BLACK, getApp().getAssetManager()),
                            new FadeInTransition(0.3f, Color.BLACK, getApp().getAssetManager()),
                            new Action()
                            {
                                @Override
                                public void action(){
                                    musicManager.playMusic(TRACK.LEVEL1);
                                }
                            });
                } else if (Pewpewman.currentScreen == SCREEN.LEVEL2) {
                    getApp().startTransition(
                            this,
                            getApp().getLevel2(),
                            new FadeOutTransition(0.3f, Color.BLACK, getApp().getAssetManager()),
                            new FadeInTransition(0.3f, Color.BLACK, getApp().getAssetManager()),
                            new Action()
                            {
                                @Override
                                public void action(){
                                    musicManager.playMusic(TRACK.LEVEL2);
                                }
                            });
                } else if (Pewpewman.currentScreen == SCREEN.LEVEL3) {
                    getApp().startTransition(
                            this,
                            getApp().getLevel3(),
                            new FadeOutTransition(0.3f, Color.BLACK, getApp().getAssetManager()),
                            new FadeInTransition(0.3f, Color.BLACK, getApp().getAssetManager()),
                            new Action()
                            {
                                @Override
                                public void action(){
                                    musicManager.playMusic(TRACK.LEVEL3);
                                }
                            });
                } else {
                    getApp().startTransition(
                            this,
                            getApp().getMainMenuScreen(),
                            new FadeOutTransition(0.3f, Color.BLACK, getApp().getAssetManager()),
                            new FadeInTransition(0.3f, Color.BLACK, getApp().getAssetManager()),
                            new Action()
                            {
                                @Override
                                public void action(){
                                    musicManager.playMusic(TRACK.MAINMENU);
                                }
                            });
                }
        }

        gameOverBox.getRoot().act(delta);
        gameOverBox.getStage().draw();
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void show() {


    }

    @Override
    public void hide() {

    }

    @Override
    public void pause() {


    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {


    }

    @Override
    public void update(float delta) {
        // TODO Auto-generated method stub

    }
    
}
