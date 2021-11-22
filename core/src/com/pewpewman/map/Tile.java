package com.pewpewman.map;

import com.pewpewman.model.character.*;

public class Tile {

    private final TERRAIN terrain;
    private Player player;
    private Enemy enemy;

    public Tile(TERRAIN terrain) {
        this.terrain = terrain;
    }

    public TERRAIN getTerrain() {
        return terrain;
    }

    public Player getPlayer() {
        return player;
    }

    public Enemy getEnemy() {
        return enemy;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
    public void setEnemy(Enemy enemy) {
        this.enemy = enemy;
    }
}