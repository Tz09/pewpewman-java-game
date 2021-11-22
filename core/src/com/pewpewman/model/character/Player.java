package com.pewpewman.model.character;

import com.pewpewman.*;
import com.pewpewman.map.*;
import com.badlogic.gdx.graphics.Texture;
import com.pewpewman.model.attack.*;
import com.pewpewman.movement.*;
import com.pewpewman.util.AnimationSet;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.*;

public class Player {

    private final TileMap map;
    private float worldX, worldY;

    private int x;
    private int y;
    private int healthBar = Settings.PLAYER_HEALTH;
    private final Texture healthBarTexture;

    private DIRECTION facing;
    private int srcX, srcY;
    private int destX, destY;
    private float animTimer;
    private float walkTimer;
    private boolean moveRequestThisFrame;

    private PLAYER_STATE state;
    private final AnimationSet animations;

    Hitbox hitbox;

    public Player(TileMap map, int x, int y, AnimationSet animations) {

        this.map = map;
        this.x = x;
        this.y = y;
        this.worldX = x;
        this.worldY = y;
        this.animations = animations;
        map.getTile(x, y).setPlayer(this);
        this.state = PLAYER_STATE.STANDING;
        this.facing = DIRECTION.SOUTH;
        float width = 0.5f;
        float height = 0.5f;
        this.hitbox = new Hitbox((float) x, (float) y, width, height);
        healthBarTexture = new Texture("unpacked/element/hp.png");
    }

    public enum PLAYER_STATE {
        WALKING,
        STANDING,
        REFACING
    }

    //Update the state of worldX & worldY when rendering (update every single frame)
    public void update(float delta) {       //delta - the time since last frame in seconds
        if (state == PLAYER_STATE.WALKING) {
            animTimer += delta;     //Update timer by adding delta
            walkTimer += delta;
            // 0.3f originally (ANIM_TIME)
            float WALK_TIME_PER_TILE = 0.15f;
            worldX = Interpolation.linear.apply(srcX, destX, animTimer / WALK_TIME_PER_TILE);    //Movement will follow
            worldY = Interpolation.linear.apply(srcY, destY, animTimer / WALK_TIME_PER_TILE);    //types of interpolation
            if (animTimer > WALK_TIME_PER_TILE) {
                float leftOverTime = animTimer - WALK_TIME_PER_TILE;
                finishMove();
                if (moveRequestThisFrame) {
                    if (move(facing)) {
                        animTimer += leftOverTime;
                        worldX = Interpolation.linear.apply(srcX, destX, animTimer / WALK_TIME_PER_TILE);    //Movement will follow
                        worldY = Interpolation.linear.apply(srcY, destY, animTimer / WALK_TIME_PER_TILE);    //types of interpolation
                    }
                } else {
                    walkTimer = 0f;
                }
            }
        }

        if (state == PLAYER_STATE.REFACING) {
            animTimer += delta;
            float REFACE_TIME = 0.1f;
            if (animTimer > REFACE_TIME) {
                state = PLAYER_STATE.STANDING;
            }
        }
        moveRequestThisFrame = false;

        hitbox.move((float)x, (float)y);
    }

    public void reface(DIRECTION dir) {
        if (state != PLAYER_STATE.STANDING) {
            return;
        }
        if (facing == dir) {
            return;
        }
        facing = dir;
        state = PLAYER_STATE.REFACING;
        animTimer = 0f;
    }

    //dx & dy changed to dir.getDx & dir.getDy for walking animation!
    public boolean move(DIRECTION dir) {
        if (state == PLAYER_STATE.WALKING) {
            if (facing == dir) {
                moveRequestThisFrame = true;
            }
            return false;
        }

        // To avoid walk through enemies
        if (map.getTile(x+dir.getDx(), y+dir.getDy()).getEnemy() != null && !map.getTile(x+dir.getDx(), y+dir.getDy()).getEnemy().isDead()) {
            reface(dir);
            return false;
        }

        // To avoid walk through not walkable tiles
        if (map.getTile(x+dir.getDx(), y+dir.getDy()).getTerrain() == TERRAIN.WALL ||
                map.getTile(x+dir.getDx(), y+dir.getDy()).getTerrain() == TERRAIN.OBSTACLE) {
            reface(dir);
            return false;
        }

        initializeMove(dir);
        map.getTile(x, y).setPlayer(null);
        x += dir.getDx();
        y += dir.getDy();
        map.getTile(x, y).setPlayer(this);
        
        return true;
    }

    //Set all of the variables for smooth movement
    private void initializeMove(DIRECTION dir) {
        this.facing = dir;
        this.srcX = x;
        this.srcY = y;
        this.destX = x+dir.getDx();
        this.destY = y+ dir.getDy();
        this.worldX = x;
        this.worldY = y;
        animTimer = 0f;
        state = PLAYER_STATE.WALKING;
    }

    //Set the state to be always standing when finish move
    private void finishMove() {
        state = PLAYER_STATE.STANDING;
        this.worldX = destX;
        this.worldY = destY;
        this.srcX = 0;
        this.srcY = 0;
        this.destX = 0;
        this.destY = 0;
    }

    public int getX() {return x;}
    public int getY() {return y;}

    //Both of these are for smooth walking
    public float getWorldX() {return worldX;}
    public float getWorldY() {return worldY;}

    public DIRECTION getFacing() {return facing;}

    public TextureRegion getSprite() {
        if (state == PLAYER_STATE.WALKING) {
            //ORIGINALLY NO (TextureRegion)
            return (TextureRegion) animations.getWalking(facing).getKeyFrame(walkTimer);
        } else if (state == PLAYER_STATE.STANDING) {
            return animations.getStanding(facing);
        } else if (state == PLAYER_STATE.REFACING) {
            return (TextureRegion) animations.getWalking(facing).getKeyFrames()[0];
        }
        return animations.getStanding(DIRECTION.SOUTH);
    }

    public Hitbox getHitbox()
    {
        return hitbox;
    }

    public boolean isDead()
    {
        return healthBar == 1;
    }

    public void isHit(int getHit)
    {
        healthBar = healthBar - getHit;
    }

    public void renderHP (SpriteBatch batch)
    {
        int hpX = 0;
        int hpY = 17;

        for(int i=0; i<healthBar; i++)
        {
            batch.draw(healthBarTexture,
            hpX *Settings.SCALED_TILE_SIZE,
            hpY *Settings.SCALED_TILE_SIZE,
            Settings.SCALED_TILE_SIZE,
            Settings.SCALED_TILE_SIZE);
            hpX++;
        }      
    }
}