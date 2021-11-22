package com.pewpewman.screen.transition;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class FadeOutTransition extends Transition {
    private Color color;
	private Texture white;

	public FadeOutTransition(float duration, Color color, AssetManager assetManager) {
		super(duration);
		this.color = color;
		white = assetManager.get("color/white.png", Texture.class);
	}

	@Override
	public void render(float delta, SpriteBatch batch) {
		batch.begin();
		batch.setColor(color.r, color.g, color.b, getProgress());
		batch.draw(white, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		batch.end();
	}
}
