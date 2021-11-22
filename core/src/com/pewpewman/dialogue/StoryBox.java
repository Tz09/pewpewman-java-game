package com.pewpewman.dialogue;

import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.*;

import java.lang.*;
import java.lang.StringBuilder;

public class StoryBox extends Table{

    private String targetText = "";
    private float animTimer = 0f;
    private float animationTotalTime = 0f;
    private STATE state = STATE.IDLE;

    private final Label textLabel;

    public StoryBox(Skin skin) {
        super(skin);
        this.setBackground("storybox");
        textLabel = new Label("\n", skin);
        this.add(textLabel).expand().align(Align.topLeft).pad(21f,30f,15f,30f);
    }

    private enum STATE {
        ANIMATING,
        IDLE
    }

    public void animateText(String text) {
        targetText = text;
        float TIME_PER_CHARACTER = 0.02f;
        animationTotalTime = text.length()* TIME_PER_CHARACTER;
        state = STATE.ANIMATING;
        animTimer = 0f;
    }

    public boolean isFinished() {
        return state == STATE.IDLE;
    }

    private void setText(String text) {
        if (!text.contains("\n")) {
            text += "\n";
        }
        this.textLabel.setText(text);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if (state == STATE.ANIMATING) {
            animTimer += delta;
            if (animTimer > animationTotalTime) {
                state = STATE.IDLE;
                animTimer = animationTotalTime;
            }
            java.lang.StringBuilder actuallyDisplayedText = new StringBuilder();
            int charactersToDisplay = (int)((animTimer/animationTotalTime)*targetText.length());
            for (int i = 0; i < charactersToDisplay; i++) {
                actuallyDisplayedText.append(targetText.charAt(i));
            }
            if (!actuallyDisplayedText.toString().equals(textLabel.getText().toString())) {
                setText(actuallyDisplayedText.toString());
            }
        }
    }

    @Override
    public float getPrefWidth() {
        return 750;
    }

    @Override
    public float getPrefHeight() {
        return 230f;
    }
}