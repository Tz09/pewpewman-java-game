package com.pewpewman.screen;

import com.badlogic.gdx.Screen;
import com.pewpewman.Pewpewman;

public abstract class AbstractScreen implements Screen {

    private final Pewpewman app;

    public AbstractScreen(Pewpewman app) {
        this.app = app;
    }

    @Override
    public abstract void render (float delta) ;

    @Override
    public abstract void resize (int width, int height) ;

    public abstract void update(float delta);

    @Override
    public abstract void show();

    @Override
    public abstract void hide();

    @Override
    public abstract void pause();

    @Override
    public abstract void resume();

    @Override
    public abstract void dispose();

    public Pewpewman getApp() {
        return app;
    }
}
