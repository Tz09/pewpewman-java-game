package com.pewpewman.model.attack;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.pewpewman.Settings;
import com.pewpewman.movement.*;

public class Bullet {

    private final int speed;
    float x,y;
    DIRECTION bulletDir;
    Hitbox hitbox;

    public boolean remove = false;
    private TextureAtlas.AtlasRegion bulletDirectionTexture;

    public Bullet (float x, float y, int speed, DIRECTION playerFacing, TextureAtlas.AtlasRegion bulletTexture)
    {
        this.x = x;
        this.y = y;
        this.speed  = speed;
        bulletDir = playerFacing;

        // TODO: width and height need to update with different enemy bullet size.
        float pewBulletWidth = 0.5f;
        float pewBulletHeight = 0.5f;

        this.hitbox = new Hitbox(x,y, pewBulletWidth, pewBulletHeight);

        if(bulletDirectionTexture == null) {
            bulletDirectionTexture = bulletTexture;
        }
    }

    public void update (float deltaTime) {

        if(bulletDir == DIRECTION.NORTH) { y += speed * deltaTime; }
        if(bulletDir == DIRECTION.EAST)  { x += speed * deltaTime; }
        if(bulletDir == DIRECTION.WEST)  { x -= speed * deltaTime; }
        if(bulletDir == DIRECTION.SOUTH) { y -= speed * deltaTime; }
        hitbox.move(x,y);
    }

    public void render (SpriteBatch batch) {
        batch.draw(bulletDirectionTexture, x* Settings.SCALED_TILE_SIZE,
                y*Settings.SCALED_TILE_SIZE,
                Settings.SCALED_TILE_SIZE,
                Settings.SCALED_TILE_SIZE);
    }

    public float getX() { return x; }

    public float getY() { return y; }

    public Hitbox getHitbox() {
        return hitbox;
    }

    public DIRECTION getBulletDir() {
        return bulletDir;
    }
}