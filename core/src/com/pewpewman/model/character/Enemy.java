package com.pewpewman.model.character;

import com.pewpewman.*;
import com.pewpewman.map.*;
import com.pewpewman.model.attack.*;
import com.pewpewman.movement.*;
import com.pewpewman.util.AnimationSet;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.*;

public class Enemy {

    public int healthBar  ;
    
    private final TileMap map;
    private int x;
    private int y;
    private DIRECTION facing;
    private float worldX, worldY;
    private int srcX, srcY;
    private int destX, destY;

    private float animTimer;
    private float walkTimer;
    private boolean moveRequestThisFrame;
    private ENEMY_STATE state;
    private final AnimationSet animations;

    private ENEMY_TYPE enemyType;
    private boolean blocked = false;
    Hitbox hitbox;

    public Enemy(TileMap map, int x, int y, AnimationSet animations, ENEMY_TYPE enemyType) {

        if (enemyType == ENEMY_TYPE.NORMAL) {
            healthBar = Settings.ENEMY_HEALTH;
        } else {
            healthBar = Settings.BOSS_HEALTH;
        }
        this.enemyType = enemyType;
        this.map = map;
        this.x = x;
        this.y = y;
        this.worldX = x;
        this.worldY = y;
        this.animations = animations;
        map.getTile(x, y).setEnemy(this);
        this.state = ENEMY_STATE.STANDING;
        this.facing = DIRECTION.SOUTH;
        float width = 0.5f;
        float height = 0.5f;
        this.hitbox = new Hitbox((float)x,(float)y, width, height);
    }

    public enum ENEMY_STATE {
        WALKING,
        STANDING,
        REFACING
    }

    // Update the state of worldX & worldY when rendering (update every single frame)
    public void update(float delta) {       //delta - the time since last frame in seconds

        if (state == ENEMY_STATE.WALKING) {
            animTimer += delta;     //Update timer by adding delta
            walkTimer += delta;
            float WALK_TIME_PER_TILE;
            if (enemyType == ENEMY_TYPE.NORMAL) {
                WALK_TIME_PER_TILE = 0.5f;
            } else {
                WALK_TIME_PER_TILE = 0.25f;
            }
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

        if (state == Enemy.ENEMY_STATE.REFACING) {
            animTimer += delta;
            float REFACE_TIME = 0.1f;
            if (animTimer > REFACE_TIME) {
                state = Enemy.ENEMY_STATE.STANDING;
            }
        }

        moveRequestThisFrame = false;
        
        hitbox.move((float)x, (float)y);
    }

    public void reface(DIRECTION dir) {
        if (state != Enemy.ENEMY_STATE.STANDING || facing == dir) {
            return;
        }
        facing = dir;
        state = Enemy.ENEMY_STATE.REFACING;
        animTimer = 0f;
    }

    //dx & dy changed to dir.getDx & dir.getDy for walking animation!
    public boolean move(DIRECTION dir) {

        blocked = false;
        if (state == ENEMY_STATE.WALKING) {
            if (facing == dir) {
                moveRequestThisFrame = true;
            }
            return false;
        }

        // To avoid walk through player and enemies
        if (map.getTile(x+dir.getDx(), y+dir.getDy()).getPlayer() != null || (map.getTile(x+dir.getDx(), y+dir.getDy()).getEnemy() != null && !map.getTile(x+dir.getDx(), y+dir.getDy()).getEnemy().isDead())) {
            return false;
        }

        // To avoid walk through not walkable tiles
        if (map.getTile(x+dir.getDx(), y+dir.getDy()).getTerrain() == TERRAIN.WALL || map.getTile(x+dir.getDx(), y+dir.getDy()).getTerrain() == TERRAIN.OBSTACLE) {
            blocked = true;
            return false;
        }

        initializeMove(dir);
        map.getTile(x, y).setEnemy(null);
        x += dir.getDx();
        y += dir.getDy();
        map.getTile(x, y).setEnemy(this);

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
        state = ENEMY_STATE.WALKING;
    }

    //Set the state to be always standing when finish move
    private void finishMove() {
        state = ENEMY_STATE.STANDING;
        this.worldX = destX;
        this.worldY = destY;
        this.srcX = 0;
        this.srcY = 0;
        this.destX = 0;
        this.destY = 0;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    //Both of these are for smooth walking
    public float getWorldX() {
        return worldX;
    }
    public float getWorldY() {
        return worldY;
    }

    public DIRECTION getFacing() {
        return facing;
    }

    public TextureRegion getSprite() {
        if (state == ENEMY_STATE.WALKING) {
            return (TextureRegion) animations.getWalking(facing).getKeyFrame(walkTimer);
        } else if (state == ENEMY_STATE.STANDING) {
            return animations.getStanding(facing);
        } else if (state == Enemy.ENEMY_STATE.REFACING) {
            return (TextureRegion) animations.getWalking(facing).getKeyFrames()[0];
        }
        return animations.getStanding(DIRECTION.SOUTH);
    }

    public Hitbox getHitbox() {
        return hitbox;
    }

    public boolean isDead() {
        return healthBar == 0;
    }

    public boolean isBlocked() {
        return blocked;
    }

    public ENEMY_TYPE getEnemyType() {
        return enemyType;
    }
}
