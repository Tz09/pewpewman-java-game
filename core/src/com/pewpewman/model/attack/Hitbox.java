package com.pewpewman.model.attack;

public class Hitbox 
{
    private float x,y;
    private final float width;
    private final float height;

    public Hitbox (float x, float y, float width, float height)
    {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public void move(float x, float y)
    {
        this.x = x;
        this.y = y;
    }

    public boolean hit (Hitbox hbox)
    {
        return x<hbox.x + hbox.width &&y<hbox.y + hbox.height && x + width > hbox.x && y + height > hbox.y;
    }
}