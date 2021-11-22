package com.pewpewman;

import com.badlogic.gdx.*;
import com.badlogic.gdx.assets.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.pewpewman.audio.*;
import com.pewpewman.screen.*;
import com.pewpewman.screen.nonGameScreen.*;
import com.pewpewman.screen.gameScreen.*;
import com.pewpewman.screen.transition.*;
import com.pewpewman.util.*;

public class Pewpewman extends Game {

    public static MusicManager musicManager = new MusicManager();
    public static SoundEffectManager soundEffectManager = new SoundEffectManager();
    private AssetManager assetManager;

    private MainMenuScreen mainMenuScreen;
    private GameOverScreen gameOverScreen;
    private TransitionScreen transitionScreen;
    public static SCREEN currentScreen = SCREEN.LEVEL1;

    @Override
    public void create() {

        assetManager = new AssetManager();
        assetManager.load("packed/element/elementpack.atlas", TextureAtlas.class);
        assetManager.load("packed/enemy/textures.atlas", TextureAtlas.class);
        assetManager.load("packed/player/textures.atlas", TextureAtlas.class);
        assetManager.load("packed/boss/textures.atlas", TextureAtlas.class);
        assetManager.load("color/white.png", Texture.class);
        assetManager.finishLoading();

        musicManager.playMusic(TRACK.MAINMENU);

        gameOverScreen = new GameOverScreen(this);
		mainMenuScreen = new MainMenuScreen(this);
		transitionScreen = new TransitionScreen(this);
        this.setScreen(mainMenuScreen);
    }

    @Override
    public void render() {

        if(getScreen()instanceof AbstractScreen)
        {
            ((AbstractScreen)getScreen()).update(Gdx.graphics.getDeltaTime());
        }
        Gdx.gl.glClearColor(0f,0f,0f,1f);
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT); //These two lines creates rect to block previous frame
                                                    //(We will do it whenever screen changes)
        getScreen().render(Gdx.graphics.getDeltaTime());
    }

    public AssetManager getAssetManager() {
        return assetManager;
    }

    public Level1 getLevel1() {
        currentScreen = SCREEN.LEVEL1;
        return new Level1(this);
	}

	public Level2 getLevel2() {
        currentScreen = SCREEN.LEVEL2;
        return new Level2(this);
    }

    public Level3 getLevel3() {
        currentScreen = SCREEN.LEVEL3;
        return new Level3(this);
    }

    public GameOverScreen getGameOverScreen() {
		return gameOverScreen;
    }
    
	public MainMenuScreen getMainMenuScreen() {
		return mainMenuScreen;
	}

	public void startTransition(AbstractScreen from, AbstractScreen to, Transition out, Transition in, Action action) {
		transitionScreen.startTransition(from, to, out, in, action);
	}
}