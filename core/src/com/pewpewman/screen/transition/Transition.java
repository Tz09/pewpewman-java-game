package com.pewpewman.screen.transition;

import com.badlogic.gdx.graphics.g2d.*;

public abstract class Transition {
    
    private float timer;
    private final float duration;
    private boolean isFinished = false;

    public Transition(float duration)
    {
        this.duration = duration;
        this.timer = 0f;
    }

    public void update(float delta)
    {
        timer += delta;
        if(timer>duration)
        {
            isFinished = true;
        }
    }

    public abstract void render(float delta, SpriteBatch batch);
	
	public boolean isFinished() {
		return isFinished;
	}
	
	public float getProgress() {
		return (timer/duration);
	}
}
