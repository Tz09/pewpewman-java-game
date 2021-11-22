package com.pewpewman.movement;

import com.badlogic.gdx.*;
import com.pewpewman.model.character.*;

//Its class(extends) not interface(implements)
public class PlayerController extends InputAdapter {

    public Player player;

    private final boolean[] buttonPress;
    private final float[] buttonTimer;
    private final float WALK_REFACE_THRESHOLD = 0.1f;

    public PlayerController(Player p) {
        this.player = p;
        buttonPress = new boolean[DIRECTION.values().length];
        buttonPress[DIRECTION.NORTH.ordinal()] = false;
        buttonPress[DIRECTION.SOUTH.ordinal()] = false;
        buttonPress[DIRECTION.EAST.ordinal()] = false;
        buttonPress[DIRECTION.WEST.ordinal()] = false;
        buttonTimer = new float[DIRECTION.values().length];
        buttonTimer[DIRECTION.NORTH.ordinal()] = 0f;
        buttonTimer[DIRECTION.SOUTH.ordinal()] = 0f;
        buttonTimer[DIRECTION.EAST.ordinal()] = 0f;
        buttonTimer[DIRECTION.WEST.ordinal()] = 0f;
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.UP || keycode == Input.Keys.W) {
            buttonPress[DIRECTION.NORTH.ordinal()] = true;
        }
        if (keycode == Input.Keys.DOWN || keycode == Input.Keys.S) {
            buttonPress[DIRECTION.SOUTH.ordinal()] = true;
        }
        if (keycode == Input.Keys.LEFT || keycode == Input.Keys.A) {
            buttonPress[DIRECTION.WEST.ordinal()] = true;
        }
        if (keycode == Input.Keys.RIGHT || keycode == Input.Keys.D) {
            buttonPress[DIRECTION.EAST.ordinal()] = true;
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        if (keycode == Input.Keys.UP || keycode == Input.Keys.W) {
            releaseDirection(DIRECTION.NORTH);
        }
        if (keycode == Input.Keys.DOWN || keycode == Input.Keys.S) {
            releaseDirection(DIRECTION.SOUTH);
        }
        if (keycode == Input.Keys.LEFT || keycode == Input.Keys.A) {
            releaseDirection(DIRECTION.WEST);
        }
        if (keycode == Input.Keys.RIGHT || keycode == Input.Keys.D) {
            releaseDirection(DIRECTION.EAST);
        }

        return false;
    }

    // Return so only one direction's instruction sent
    public void update(float delta) {
        if (buttonPress[DIRECTION.NORTH.ordinal()]) {
            updateDirection(DIRECTION.NORTH, delta);
            return;
        }
        if (buttonPress[DIRECTION.SOUTH.ordinal()]) {
            updateDirection(DIRECTION.SOUTH, delta);
            return;
        }
        if (buttonPress[DIRECTION.WEST.ordinal()]) {
            updateDirection(DIRECTION.WEST, delta);
            return;
        }
        if (buttonPress[DIRECTION.EAST.ordinal()]) {
            updateDirection(DIRECTION.EAST, delta);
        }
    }

    private void updateDirection(DIRECTION dir, float delta) {
        buttonTimer[dir.ordinal()] += delta;
        considerMove(dir);
    }

    private void releaseDirection(DIRECTION dir) {
        buttonPress[dir.ordinal()] = false;
        considerReface(dir);
        buttonTimer[dir.ordinal()] = 0f;
    }

    private void considerMove(DIRECTION dir) {
        if (buttonTimer[dir.ordinal()] > WALK_REFACE_THRESHOLD) {
            player.move(dir);
        }
    }

    private void considerReface(DIRECTION dir) {
        if (buttonTimer[dir.ordinal()] < WALK_REFACE_THRESHOLD) {
            player.reface(dir);
        }
    }
}
