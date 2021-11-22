package com.pewpewman.screen.nonGameScreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.pewpewman.Pewpewman;
import com.pewpewman.audio.*;
import com.pewpewman.screen.*;
import com.pewpewman.screen.transition.*;
import com.pewpewman.util.*;

public class MainMenuScreen extends AbstractScreen {

    private final MusicManager musicManager = Pewpewman.musicManager;
    private final SoundEffectManager soundEffectManager = Pewpewman.soundEffectManager;
    private int gameWidth = 1650;
    private int gameHeight= 900;

    private final SpriteBatch batch;
    Texture playButtonActive;
    Texture playButtonInactive;
    Texture exitButtonActive;
    Texture exitButtonInactive;
    Texture background;

    public MainMenuScreen(Pewpewman game) {
        super(game);
        Pixmap cursor = new Pixmap(Gdx.files.internal("menu/mouse.png"));
        Gdx.graphics.setCursor(Gdx.graphics.newCursor(cursor, 0, 0));
        cursor.dispose();
        batch = new SpriteBatch();
        playButtonActive    = new Texture("menu/activeplay.png");
        playButtonInactive  = new Texture("menu/inactiveplay.png");
        exitButtonActive    = new Texture("menu/activeexit.png");
        exitButtonInactive  = new Texture("menu/inactiveexit.png");
        background          = new Texture("menu/mainmenu.png");
    }

    @Override
    public void render(float delta) {

        Gdx.gl.glClearColor(0,0,0,0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        gameWidth = Gdx.graphics.getWidth();
        gameHeight = Gdx.graphics.getHeight();

        batch.begin();

        batch.draw(background,0,0,gameWidth,gameHeight);
        int exitButtonWidth = 400;
        int x = gameWidth/2 - exitButtonWidth /2;
        int exitButtonHeight = 150;
        int exitButtonY = 120;
        if (Gdx.input.getX()<x + exitButtonWidth && Gdx.input.getX()>x && gameHeight - Gdx.input.getY()< exitButtonY + exitButtonHeight && gameHeight - Gdx.input.getY()> exitButtonY) {
            batch.draw(exitButtonActive,x, exitButtonY, exitButtonWidth, exitButtonHeight);
            if(Gdx.input.isTouched()){
                soundEffectManager.playSoundEffect(SOUNDEFFECT.BUTTONCLICKED);
                getApp().startTransition(
                        this,
                        getApp().getMainMenuScreen(),
                        new FadeOutTransition(0.5f, Color.BLACK, getApp().getAssetManager()),
                        new FadeInTransition(0.5f, Color.BLACK, getApp().getAssetManager()),
                        new Action() {
                            @Override
                            public void action() {
                                Gdx.app.exit();
                            }
                        });
            }
        }
        else {
            batch.draw(exitButtonInactive,x, exitButtonY, exitButtonWidth, exitButtonHeight);
        }

        x = gameWidth/2 - exitButtonWidth /2;
        int playButtonWidth = 400;
        int playButtonHeight = 150;
        int playButtonY = 320;
        if (Gdx.input.getX()<x + playButtonWidth && Gdx.input.getX()>x && gameHeight - Gdx.input.getY()< playButtonY + playButtonHeight && gameHeight - Gdx.input.getY()> playButtonY) {
            batch.draw(playButtonActive,x, playButtonY, playButtonWidth, playButtonHeight);
            if(Gdx.input.isTouched()){
                soundEffectManager.playSoundEffect(SOUNDEFFECT.BUTTONCLICKED);
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
                            Pewpewman.currentScreen = SCREEN.LEVEL1;
                        }
                    });
            }
        }
        else {
            batch.draw(playButtonInactive,x, playButtonY, playButtonWidth, playButtonHeight);
        }
        batch.end();
    }

    @Override
    public void resize(int width, int height) {
        gameWidth = width;
        gameHeight = height;
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

    }

    @Override
    public void update(float delta) {

    }

    @Override
    public void show() {

    }
}
